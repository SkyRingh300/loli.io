package io.loli.sc.server.service;

import io.loli.sc.server.dao.UserDao;
import io.loli.sc.server.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named
public class UserService {
    @Inject
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(User user) {
        userDao.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public User update(User user) {
        return userDao.update(user);
    }

    public User findByEmail(String email) {
        User result = null;
        try {
            result = userDao.findByEmail(email);
        } catch (NoResultException e) {
            e.printStackTrace();
        } catch (NonUniqueResultException e) {
            e.printStackTrace();
        }
        return result;
    }

    public User findById(int id) {
        User result = userDao.findById(id);
        return result;
    }
}
