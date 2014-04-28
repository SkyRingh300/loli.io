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
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
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
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "email", required = true) String email,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "image", required = true) MultipartFile imageFile,
            HttpServletRequest request) {

        UploadedImage imageObj = new UploadedImage();
        
        if (!cts.checkTokenBelongToUser(token, email)) {
            return new UploadedImage();
        } else{
            imageObj.setUser(userService.findByEmail(email));
        }
        imageObj.setDate(new Date());
        imageObj.setDesc((null == desc || desc.isEmpty()) ? "" : desc);

        File file = saveImage(imageFile);
        imageObj.setStorageBucket(bucketService.randomBucket());
        StorageUploader uploader = StorageUploader.newInstance(imageObj
                .getStorageBucket());
        imageObj.setPath(uploader.upload(file));
        try {
            System.out.println(new String(imageFile.getOriginalFilename().getBytes("ISO8859-1"),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        imageObj.setOriginName(imageFile.getOriginalFilename());
        uic.save(imageObj);
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
