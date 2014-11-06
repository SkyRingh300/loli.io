package io.loli.sc.server.dao;

import java.util.List;

import io.loli.sc.server.entity.Gallery;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class GalleryDao {

    @PersistenceContext
    private EntityManager em;

    public void save(Gallery g) {
        em.persist(g);
    }

    public void update(Gallery g) {
        em.merge(g);
    }

    public List<Gallery> listByUserId(int uid) {
        return em.createQuery("from Gallery where delFlag=false and user.id=:uid", Gallery.class)
            .setParameter("uid", uid).getResultList();
    }

    public Gallery findById(int gid) {
        return em.createQuery("from Gallery where delFlag=false and id=:id", Gallery.class).setParameter("id", gid)
            .getSingleResult();
    }

    public List<Gallery> listByUserIdReversed(int uid) {
        return em.createQuery("from Gallery where delFlag=false and user.id=:uid order by id desc", Gallery.class)
            .setParameter("uid", uid).getResultList();
    }

}
