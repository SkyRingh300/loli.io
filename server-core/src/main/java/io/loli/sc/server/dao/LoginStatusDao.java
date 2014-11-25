package io.loli.sc.server.dao;

import io.loli.sc.server.entity.LoginStatus;
import io.loli.sc.server.entity.User;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class LoginStatusDao {

    @PersistenceContext
    private EntityManager em;

    public LoginStatus findByUId(int uid) {

        List<LoginStatus> list = em.createNamedQuery("LoginStatus.findByUId", LoginStatus.class)
            .setParameter("uid", uid).getResultList();
        LoginStatus result = null;
        if (list.size() >= 1) {
            result = list.get(0);
        }
        return result;
    }

    public void save(LoginStatus ls) {
        em.persist(ls);
    }

    public List<User> listByToken(String value) {
        return em.createNamedQuery("User.listByToken", User.class).setParameter("token", value).getResultList();
    }

    public void update(LoginStatus ls) {
        em.merge(ls);
    }
}
