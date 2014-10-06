package io.loli.sc.server.dao.oauth2;

import io.loli.sc.server.entity.oauth2.AccessToken;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class TokenDao {
    @PersistenceContext
    private EntityManager em;

    public void save(AccessToken token) {
        em.persist(token);
    }
}
