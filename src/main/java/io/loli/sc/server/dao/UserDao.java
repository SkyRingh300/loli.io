package io.loli.sc.server.dao;

import io.loli.sc.server.entity.User;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;

@Named
public class UserDao {
    @PersistenceContext
    private EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public User findByEmail(String email) throws NoResultException,
            NonUniqueResultException {
        User result = em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email).getSingleResult();
        return result;
    }

    public User update(User user) {
        return em.merge(user);
    }

    public User findById(int id) {
        User result = em.find(User.class, id);
        return result;
    }
}
