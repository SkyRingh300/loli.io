package io.loli.sc.server.dao;

import io.loli.sc.server.entity.Tag;
import io.loli.sc.server.entity.User;

import java.util.Date;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class TagDao {

    @PersistenceContext
    private EntityManager em;

    public void save(Tag tag) {
        tag.setDate(new Date());
        em.persist(tag);
    }

    public void refresh(Tag tag) {
        em.refresh(tag);
    }

    public Tag findByNameAndUser(String name, User user) {
        return em.createNamedQuery("Tag.findByNameAndUser", Tag.class).setParameter("name", name)
                .setParameter("userId", user.getId()).getSingleResult();
    }

    public Tag getById(int id) {
        return em.find(Tag.class, id);
    }
}
