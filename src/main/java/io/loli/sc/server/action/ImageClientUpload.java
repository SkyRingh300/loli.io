package io.loli.sc.server.action;

import io.loli.sc.server.entity.ClientToken;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.ClientTokenService;
import io.loli.sc.server.service.UserService;
import io.loli.util.MD5Util;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 负责客户端登陆验证和图片上传的类
 * 
 * @author choco
 * 
 */
@Named
@RequestMapping("/api")
public class ImageClientUpload {
    @Inject
    private ClientTokenService cts;

    @Inject
    private UserService us;

    @RequestMapping(value = { "/token" }, method = RequestMethod.POST)
    public String requestToken(@RequestParam(required = true) String email,
            @RequestParam(required = true) String password) {
        User trueUser = us.findByEmail(email);
        String token = null;
        //登陆成功后
        if (trueUser.getPassword().equals(password)) {
            ClientToken ct = cts.findByEmail(email);

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

        return token;
    }
}
