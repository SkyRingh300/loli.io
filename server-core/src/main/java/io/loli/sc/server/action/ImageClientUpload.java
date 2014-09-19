package io.loli.sc.server.action;

import io.loli.sc.server.entity.ClientToken;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.BucketService;
import io.loli.sc.server.service.ClientTokenService;
import io.loli.sc.server.service.FileFetchService;
import io.loli.sc.server.service.UploadedImageService;
import io.loli.sc.server.service.UserService;
import io.loli.sc.server.storage.StorageUploader;
import io.loli.util.string.MD5Util;
import io.loli.util.string.ShortUrl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * 负责客户端登录验证和图片上传的类
 * 
 * @author choco
 * 
 */
@Named
@RequestMapping(value = { "/api" })
public class ImageClientUpload {
    @Inject
    private ClientTokenService cts;

    @Inject
    @Named("userService")
    private UserService us;

    @Inject
    private UploadedImageService uic;

    @Inject
    @Named("fileFetchService")
    private FileFetchService ffs;

    private Logger logger = Logger.getLogger(ImageClientUpload.class);

    private static final String LOCAL_HOST = "127.0.0.1";

    @RequestMapping(value = { "/token" }, method = { RequestMethod.GET, RequestMethod.POST })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ClientToken requestToken(@RequestParam(required = true) String email,
        @RequestParam(required = true) String password) {
        User trueUser = us.findByEmail(email);
        String token = null;
        ClientToken ct = null;
        // 验证密码是否正确
        if (trueUser != null && trueUser.getPassword().equalsIgnoreCase(password)) {
            ct = cts.findByEmail(email);

            if (ct != null) {
                // 当已有该email的token时，把token返回
                token = ct.getToken();
                logger.info(email + "已有token，将已经存在的token返回");
            } else {
                // 当没有该email的token时，新建一个token保存至数据库，然后返回
                ct = new ClientToken();
                // 用于md5加密的密文
                String word = trueUser.getEmail() + new java.util.Date().getTime();
                try {
                    token = MD5Util.hash(word);
                } catch (NoSuchAlgorithmException e) {
                    logger.error(e);
                }
                ct.setToken(token);
                ct.setUser(trueUser);
                cts.save(ct);
                logger.info(email + "生成新token");
            }
            return ct;
        }
        return new ClientToken();
    }

    @Inject
    private BucketService bucketService;
    @Inject
    private UserService userService;

    @RequestMapping(value = { "/upload" }, method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody UploadedImage upload(@RequestParam(value = "token", required = false) String token,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "desc", required = false) String desc,
        @RequestParam(value = "image", required = true) MultipartFile imageFile, HttpServletRequest request,
        @RequestParam(value = "type", required = false) String type

    ) {

        UploadedImage imageObj = new UploadedImage();
        imageObj.setDate(new Date());

        if (email == null && desc == null && token == null) {
            imageObj.setDesc(imageFile.getOriginalFilename());
        } else {
            if (!cts.checkTokenBelongToUser(token, email)) {
                logger.info(email + "使用错误的token上传");
                return new UploadedImage();
            } else {
                imageObj.setUser(userService.findByEmail(email));
            }
            imageObj.setOriginName(desc);
        }
        User user = null;
        if ((user = (User) request.getSession().getAttribute("user")) != null) {
            imageObj.setUser(user);
        }
        String ip = request.getRemoteAddr();
        if (ip != null && LOCAL_HOST.equals(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        imageObj.setIp(ip);
        imageObj.setUa(request.getHeader("user-agent"));
        String fileName = "";
        fileName = getFileName(imageFile);

        File file = saveImage(imageFile, fileName);
        String ref = null;
        if (StringUtils.isNotBlank(type) && "weibo".equals(type)) {
            imageObj.setStorageBucket(bucketService.weiboBucket());
        } else if ((ref = request.getHeader("REFERER")) != null) {
            if (ref.contains("file")) {
                imageObj.setStorageBucket(bucketService.randomFileBucket());
            } else {
                imageObj.setStorageBucket(bucketService.randomImageBucket());
            }
        } else {
            imageObj.setStorageBucket(bucketService.randomImageBucket());
        }

        StorageUploader uploader = StorageUploader.newInstance(imageObj.getStorageBucket());
        imageObj.setPath(uploader.upload(file));
        imageObj.setOriginName(imageFile.getOriginalFilename());

        imageObj.setGeneratedName(file.getName());
        imageObj.setRedirectCode(file.getName());
        imageObj.setGeneratedCode(file.getName().contains(".") ? file.getName().substring(0,
            file.getName().indexOf(".")) : file.getName());
        if (StringUtils.isNotBlank(type) && "weibo".equals(type)) {
            imageObj.setInternalPath(imageObj.getPath());
        } else
            imageObj.setInternalPath(imageObj.getStorageBucket().getInternalUrl() + "/" + file.getName());

        uic.save(imageObj);
        if (imageObj.getUser() == null) {
            logger.info("匿名上传文件:" + imageObj.getOriginName() + ", 链接为" + imageObj.getPath());
        } else {
            logger.info(imageObj.getUser().getEmail() + "上传文件:" + imageObj.getOriginName() + ", 链接为"
                + imageObj.getPath());
        }
        return imageObj;
    }

    private String getFileName(MultipartFile imageFile) {
        String str = null;

        try {
            String[] urls = ShortUrl.shortText(new Date().getTime() + imageFile.getOriginalFilename(), 6);
            for (String url : urls) {
                if (!uic.checkExists(url)) {
                    return url;
                }
            }

            str = MD5Util.hash(String.valueOf(System.nanoTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private ExecutorService service = Executors.newFixedThreadPool(50);

    @ResponseBody
    @RequestMapping(value = { "/fetch" }, method = { RequestMethod.POST })
    public String fetch(@RequestParam(value = "path") String path, HttpServletRequest request) {
        if (StringUtils.isBlank(path)) {
            return "{\"origin\":\"" + path + "\",\"error\":\"" + "图片url不能为空" + "\",\"redirect\":\"" + "\"}";
        }
        File file = null;
        Future<File> future = service.submit(() -> {
            return ffs.fetch(path);
        });
        try {
            file = future.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            logger.error("Fetch image timeout:" + e);
            return "{\"origin\":\"" + path + "\",\"error\":\"" + e.getMessage() + "\",\"redirect\":\"" + "\"}";
        }

        if (file == null) {
            return "";
        }
        try {
            User user = null;
            UploadedImage imageObj = new UploadedImage();
            if ((user = (User) request.getSession().getAttribute("user")) != null) {
                imageObj.setUser(user);
            }
            String ip = request.getRemoteAddr();
            if (ip != null && ip.equals("127.0.0.1")) {
                ip = request.getHeader("X-Real-IP");
            }
            imageObj.setIp(ip);
            imageObj.setUa(request.getHeader("user-agent"));
            String ref = null;
            if ((ref = request.getHeader("REFERER")) != null) {
                if (ref.contains("file")) {
                    imageObj.setStorageBucket(bucketService.randomFileBucket());
                } else {
                    imageObj.setStorageBucket(bucketService.randomImageBucket());
                }
            } else {
                imageObj.setStorageBucket(bucketService.randomImageBucket());
            }

            StorageUploader uploader = StorageUploader.newInstance(imageObj.getStorageBucket());
            imageObj.setPath(uploader.upload(file));
            imageObj.setOriginName(file.getName());

            imageObj.setGeneratedName(file.getName());
            imageObj.setRedirectCode(file.getName());
            imageObj.setGeneratedCode(file.getName().contains(".") ? file.getName().substring(0,
                file.getName().indexOf(".")) : file.getName());

            imageObj.setInternalPath(imageObj.getStorageBucket().getInternalUrl() + "/" + file.getName());

            uic.save(imageObj);
            if (imageObj.getUser() == null) {
                logger.info("匿名上传文件:" + imageObj.getOriginName() + ", 链接为" + imageObj.getPath());
            } else {
                logger.info(imageObj.getUser().getEmail() + "上传文件:" + imageObj.getOriginName() + ", 链接为"
                    + imageObj.getPath());
            }
            return "{\"origin\":\"" + path + "\",\"error\":\"" + "\",\"redirect\":\"" + imageObj.getRedirectCode()
                + "\"}";
        } catch (Exception e) {
            return "{\"origin\":\"" + path + "\",\"error\":\"" + e.getMessage() + "\",\"redirect\":\"" + "\"}";
        }
    }

    /**
     * 将一个图片保存起来
     * 
     * @param image
     * @return 保存后的图片File对象
     */
    private File saveImage(MultipartFile image, String fileName) {
        if (image.getOriginalFilename().contains(".")) {
            // 获取图片扩展名，jpg,png
            fileName += "."
                + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
        }

        File file = new File(System.getProperty("java.io.tmpdir"), fileName);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileUtils.writeByteArrayToFile(file, image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return file;
    }
}
