package io.loli.sc.server.dao.oauth2;

import io.loli.sc.server.entity.oauth2.Application;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class AppDao {

    @PersistenceContext
    private EntityManager em;

    public boolean verify(String key, String secret) {

        return em
            .createQuery("select count(a) from Application a where a.appKey=:key and a.appSecret=:secret", Long.class)
            .setParameter("key", key).setParameter("secret", secret).getSingleResult() != 0;
    }

    public boolean checkExist(String key) {
        return em.createQuery("select count(a) from Application a where a.appKey=:key", Long.class)
            .setParameter("key", key).getSingleResult() != 0;
    }

    public Application findbyKey(String clientId) {
        return em.createQuery("select a from Application a where a.appKey=:key", Application.class)
            .setParameter("key", clientId).getSingleResult();
    }
}
