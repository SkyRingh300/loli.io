package io.loli.sc.server.dao.social;

import io.loli.sc.server.entity.Social;

import java.util.List;

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

    public List<Social> listByUserId(int id) {
        return em.createQuery("from Social where user_id=:id", Social.class).setParameter("id", id).getResultList();
    }

    public Social findByUserId(int userId, String type) {
        return em.createQuery("from Social where user.id=:id and type=:type", Social.class).setParameter("id", userId)
            .setParameter("type", type).getSingleResult();
    }

    public void delete(Social social) {
        em.remove(social);
    }

}
