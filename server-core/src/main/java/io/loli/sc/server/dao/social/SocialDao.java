package io.loli.sc.server.dao.social;

import io.loli.sc.server.entity.Social;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class SocialDao {
    @PersistenceContext
    private EntityManager em;

    public void save(Social s) {
        em.persist(s);
    }

    public Social findByUserIdAndType(String userId, String type) {
        return em.createQuery("from Social where uid=:userId and type=:type", Social.class)
            .setParameter("userId", userId).setParameter("type", type).getSingleResult();
    }

}
