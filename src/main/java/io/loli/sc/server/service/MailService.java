package io.loli.sc.server.service;

import io.loli.util.mail.MailSenderInfo;
import io.loli.util.mail.SimpleMailSender;
import io.loli.util.string.MD5Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;

@Named
public class MailService {
    /**
     * 检查邮箱格式
     * 
     * @param mail 要检查的邮箱名
     * @return 邮箱格式是否正确
     */
    public boolean checkMailFormat(String mail) {
        if (null != null && !mail.trim().equals("")) {
            return mail.matches(".+@\\..+");
        } else {
            return false;
        }
    }


    private MailSenderInfo buildInfo() {
        MailSenderInfo info = new MailSenderInfo();
        // 或者 ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream in = this.getClass().getResourceAsStream("/mail.properties");
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        info.setMailServerHost(p.getProperty("smtp"));
        info.setMailServerPort(p.getProperty("port"));
        info.setUserName(p.getProperty("username"));
        info.setPassword(p.getProperty("password"));
        info.setFromAddress(p.getProperty("from"));
        info.setSenderNickName("nickname");
        info.setValidate(true);
        info.setSubject("感谢注册, 这是您的验证码");
        return info;
    }

    public String save(String mail) {
        SimpleMailSender sender = new SimpleMailSender();
        String token = null;
        try {
            token = MD5Util.hash(mail + new Date().getTime());
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        MailSenderInfo info = this.buildInfo();
        info.setToAddress(mail);
        info.setContent(token);
        try {
            sender.sendTextMail(info);
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return token;
    }

}
