package io.loli.sc.server.dao;

import io.loli.sc.server.entity.ClientToken;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class ClientTokenDao {
    @PersistenceContext
    private EntityManager em;

    public ClientToken findByUId(int uid) {
        return em.createNamedQuery("ClientToken.findByUId", ClientToken.class)
                .setParameter("uid", uid).getSingleResult();
    }

    public void save(ClientToken ct) {
        // TODO Auto-generated method stub

    }

}
