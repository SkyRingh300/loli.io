package io.loli.sc.server.service;

import io.loli.sc.server.dao.UserDao;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.exception.DBException;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named("userService")
public class UserService {
    @Inject
    private UserDao userDao;

    /**
     * 把User实体持久化
     * 
     * @see UserDao#save(User)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(User user) {
        if (this.findByEmail(user.getEmail()) == null) {
            userDao.save(user);
        } else {
            throw new DBException("已经存在相同的邮箱");
        }
    }

    /**
     * 更新User实体
     * 
     * @param user 需要更新的实体
     * @return 更新后的实体
     * @see UserDao#update(User)
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public User update(User user) {
        return userDao.update(user);
    }

    /**
     * 根据指定的email查询出User, 如果无此User则返回null, 如果有多个, 则返回第一个
     * 
     * @param email
     * @return 查询出的User实体
     * @see UserDao#findByEmail(String)
     */
    public User findByEmail(String email) {
        User result = null;
        result = userDao.findByEmail(email);
        return result;
    }

    /**
     * 根据指定的id(User的主键)查询出User
     * 
     * @param id
     * @return 如果查询不出, 返回null
     * @see UserDao#findById(int)
     */
    public User findById(int id) {
        User result = userDao.findById(id);
        return result;
    }
}
