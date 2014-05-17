package io.loli.sc.server.action;

import io.loli.sc.server.entity.ClientToken;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.BucketService;
import io.loli.sc.server.service.ClientTokenService;
import io.loli.sc.server.service.UploadedImageService;
import io.loli.sc.server.service.UserService;
import io.loli.sc.server.storage.StorageUploader;
import io.loli.util.string.MD5Util;
import io.loli.util.string.ShortUrl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

/**
 * 负责客户端登陆验证和图片上传的类
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

    private Logger logger = Logger.getLogger(ImageClientUpload.class);

    @RequestMapping(value = { "/token" }, method = { RequestMethod.GET,
            RequestMethod.POST })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ClientToken requestToken(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) String password) {
        User trueUser = us.findByEmail(email);
        String token = null;
        ClientToken ct = null;
        // 验证密码是否正确
        if (trueUser != null
                && trueUser.getPassword().equalsIgnoreCase(password)) {
            ct = cts.findByEmail(email);

            if (ct != null) {
                // 当已有该email的token时，把token返回
                token = ct.getToken();
                logger.info(email + "已有token，将已经存在的token返回");
            } else {
                // 当没有该email的token时，新建一个token保存至数据库，然后返回
                ct = new ClientToken();
                // 用于md5加密的密文
                String word = trueUser.getEmail()
                        + new java.util.Date().getTime();
                try {
                    token = MD5Util.hash(word);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
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
    public @ResponseBody UploadedImage upload(
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "image", required = true) MultipartFile imageFile,
            HttpServletRequest request) {

        UploadedImage imageObj = new UploadedImage();
        imageObj.setDate(new Date());

        if (email == null && desc == null && token == null) {
        } else {
            if (!cts.checkTokenBelongToUser(token, email)) {
                logger.info(email + "使用错误的token上传");
                return new UploadedImage();
            } else {
                imageObj.setUser(userService.findByEmail(email));
            }
            imageObj.setDesc((null == desc || desc.isEmpty()) ? "" : desc);
        }
        User user = null;
        if ((user = (User) request.getSession().getAttribute("user")) != null) {
            imageObj.setUser(user);
        }
        File file = saveImage(imageFile);
        imageObj.setStorageBucket(bucketService.randomBucket());
        StorageUploader uploader = StorageUploader.newInstance(imageObj
                .getStorageBucket());
        imageObj.setPath(uploader.upload(file));
        imageObj.setOriginName(imageFile.getOriginalFilename());
        uic.save(imageObj);
        if (imageObj.getUser() == null) {
            logger.info("匿名上传文件:" + imageObj.getOriginName() + ", 链接为"
                    + imageObj.getPath());
        } else {
            logger.info(imageObj.getUser().getEmail() + "上传文件:"
                    + imageObj.getOriginName() + ", 链接为" + imageObj.getPath());
        }
        return imageObj;
    }

    /**
     * 将一个图片保存起来
     * 
     * @param image
     * @return 保存后的图片File对象
     */
    private File saveImage(MultipartFile image) {
        File file = new File(System.getProperty("java.io.tmpdir"),
                ShortUrl.shortText(new Date().getTime()
                        + image.getOriginalFilename())[0]
                        + "."
                        // 获取图片扩展名，jpg,png
                        + image.getOriginalFilename()
                                .substring(
                                        image.getOriginalFilename()
                                                .lastIndexOf(".") + 1));
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileUtils.writeByteArrayToFile(file, image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            // TODO
        }
        return file;
    }

}
