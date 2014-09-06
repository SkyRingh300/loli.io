package io.loli.sc.server.dao;

import io.loli.sc.server.entity.User;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class UserDao {
    @PersistenceContext
    private EntityManager em;

    /**
     * 把User实体持久化
     * 
     * @param user
     */
    public void save(User user) {
        em.persist(user);
    }

    /**
     * 根据指定的email查询出User, 如果无此User则返回null, 如果有多个, 则返回第一个
     * 
     * @param email
     * @return 查询出的User实体
     */
    public User findByEmail(String email) {
        List<User> resultList = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email).getResultList();
        User result = null;
        if (resultList.size() >= 1) {
            result = resultList.get(0);
        }
        return result;
    }

    /**
     * 更新User实体
     * 
     * @param user 需要更新的实体
     * @return 更新后的实体
     */
    public User update(User user) {
        return em.merge(user);
    }

    /**
     * 根据指定的id(User的主键)查询出User
     * 
     * @param id
     * @return 如果查询不出, 返回null
     */
    public User findById(int id) {
        User result = em.find(User.class, id);
        return result;
    }

    public void merge(User user) {
        em.merge(user);
    }
    
    public void refresh(User user) {
        em.refresh(user);
    }
}
