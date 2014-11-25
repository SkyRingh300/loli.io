package io.loli.sc.server.service;

import io.loli.sc.server.dao.LoginStatusDao;
import io.loli.sc.server.dao.UserDao;
import io.loli.sc.server.entity.LoginStatus;
import io.loli.sc.server.entity.User;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class LoginStatusService {
    @Inject
    private LoginStatusDao loginStatusDao;

    @Inject
    private UserDao userDao;

    @Transactional
    public void save(LoginStatus ls) {
        loginStatusDao.save(ls);
    }

    public LoginStatus findByUId(int uid) {
        return loginStatusDao.findByUId(uid);
    }

    @Transactional
    public void update(LoginStatus ls, String newToken) {
        ls.setToken(newToken);
    }

    @Transactional
    public void update(LoginStatus ls) {
        loginStatusDao.update(ls);
    }

    public User findByToken(String value) {
        List<User> list = loginStatusDao.listByToken(value);
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public LoginStatus getLoginStatusByUId(int uid) {
        LoginStatus ls = this.findByUId(uid);
        if (ls == null) {
            ls = new LoginStatus();
            ls.setUser(userDao.findById(uid));
            String token = null;
            try {
                token = ls.generateToken();
            } catch (NoSuchAlgorithmException e) {
                token = ls.getUser().getEmail() + "/" + new Date().getTime();
            }

            ls.setToken(token);
            this.save(ls);
            return ls;
        } else {
            return ls;
        }
    }

    @Transactional
    public void updateDate(User user) {
        LoginStatus ls = this.findByUId(user.getId());
        ls.setLastLogin(new Date());
    }

}
