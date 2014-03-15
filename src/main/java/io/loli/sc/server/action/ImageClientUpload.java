package io.loli.sc.server.action;

import io.loli.sc.server.entity.ClientToken;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.ClientTokenService;
import io.loli.sc.server.service.UploadedImageService;
import io.loli.sc.server.service.UserService;
import io.loli.util.MD5Util;

import java.io.File;
import java.io.IOException;
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
    public @ResponseBody
    ClientToken requestToken(@RequestParam(required = true) String email,
            @RequestParam(required = true) String password) {
        User trueUser = us.findByEmail(email);
        String token = null;
        ClientToken ct = null;
        // 验证密码是否正确
        if (trueUser.getPassword().equalsIgnoreCase(password)) {
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
                token = MD5Util.hash(word);
                ct.setToken(token);
                ct.setUser(trueUser);
                cts.save(ct);
            }
        }
        return ct;
    }

    @RequestMapping(value = { "/upload" }, method = { RequestMethod.POST })
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    UploadedImage upload(
            @RequestParam(value = "token", required = true) String token,
            @RequestParam(value = "u_id", required = true) int u_id,
            @RequestParam(value = "desc", required = false) String desc,
            @RequestParam(value = "image", required = true) MultipartFile imageFile) {

        if (!cts.checkTokenBelongToUser(token, u_id)) {
            //TODO 当token不属于此用户时
        }

        UploadedImage imageObj = new UploadedImage();
        imageObj.setDate(new Date());
        imageObj.setDesc((null == desc || desc.isEmpty()) ? "" : desc);

        // 当上传的是图片文件时，保存图片
        if (validateImage(imageFile)) {
            File file = saveImage(imageFile);
            imageObj.setPath(file.getPath());
            imageObj.setOriginName(imageFile.getOriginalFilename());
            uic.save(imageObj);
        } else {
            // TODO 当上传的不是图片文件时
        }

        return imageObj;
    }

    /**
     * 判断上传的一个文件是否是图片文件<br/>
     * 只允许png和jpg/jpeg类型的图片
     * 
     * @param image
     * @return 如果是图片返回true，不是图片返回false
     */
    private boolean validateImage(MultipartFile image) {
        String contentType = image.getContentType();
        return contentType.equals("image/jpeg")
                || contentType.equals("image/jpg")
                || contentType.equals("image/png");
    }

    @Inject
    private HttpServletRequest request;

    /**
     * 将一个图片保存起来
     * 
     * @param image
     * @return 保存后的图片File对象
     */
    private File saveImage(MultipartFile image) {
        File file = new File(request.getSession().getServletContext()
                .getRealPath("")
                + File.separator
                + "img"
                + File.separator
                + MD5Util.hash(
                        new Date().getTime() + image.getOriginalFilename())
                        .substring(26) + "."
                // 获取图片扩展名，如image/jpg
                + image.getContentType().substring(6));
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
