package io.loli.sc.server.service;

import io.loli.sc.server.dao.MailDao;
import io.loli.sc.server.entity.EmailSendLog;
import io.loli.util.MD5Util;
import io.loli.util.mail.MailSenderInfo;
import io.loli.util.mail.SimpleMailSender;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;

import org.springframework.transaction.annotation.Transactional;

@Named
public class MailService {
    @Inject
    private MailDao mailDao;

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

    public EmailSendLog findById(int id) {
        return mailDao.findById(id);
    }

    public boolean exists(String email) {
        EmailSendLog log = null;
        try {
            log = this.findByEmail(email);
        } catch (NoResultException e) {
        }
        return log != null;
    }

    @Transactional
    public void update(EmailSendLog log) {
        mailDao.update(log);
    }

    @Inject
    private SimpleMailSender sender;

    private MailSenderInfo buildInfo() {
        MailSenderInfo info = new MailSenderInfo();
        info.setMailServerHost("smtp.gmail.com");
        info.setMailServerPort("465");
        info.setValidate(true);
        info.setUserName("uzumakitenye@gmail.com");
        info.setPassword("password");// 您的邮箱密码
        info.setFromAddress("noreply@screenshot.pics");
        info.setSenderNickName("screenshot.pics");
        info.setSubject("感谢注册, 这是您的验证码");
        return info;
    }

    @Transactional
    public void save(String mail) {
        if (this.exists(mail)) {
            EmailSendLog log = this.findByEmail(mail);
            log.setToken(MD5Util.hash(mail + new Date().getTime()));
            this.update(log);
        } else {
            EmailSendLog log = new EmailSendLog();
            log.setDate(new Date());
            log.setEmail(mail);
            log.setStatus(false);
            log.setToken(MD5Util.hash(mail + new Date().getTime()));
            mailDao.save(log);
            MailSenderInfo info = this.buildInfo();
            info.setToAddress(mail);
            info.setContent(log.getToken());
            try {
                sender.sendTextMail(info);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("无法发送邮件: " + e.getMessage());
            }
        }
    }

    @Transactional
    public boolean verify(String token) {
        EmailSendLog log = null;
        try {
            log = this.findByToken(token);
            if (!log.isStatus()) {
                log.setStatus(true);
            } else {
                log = null;
            }
        } catch (NoResultException e) {
        }
        return log == null;
    }

    public EmailSendLog findByEmail(String mail) {
        return mailDao.findByEmail(mail);
    }

    public EmailSendLog findByToken(String token) {
        return mailDao.findByToken(token);
    }
}
