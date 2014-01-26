package io.loli.sc.server.dao;

import io.loli.sc.server.entity.ClientToken;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class ClientTokenDao {
    @PersistenceContext
    private EntityManager em;

    public ClientToken findByUId(int uid) {
        List<ClientToken> list = em
                .createNamedQuery("ClientToken.findByUId", ClientToken.class)
                .setParameter("uid", uid).getResultList();
        ClientToken result = null;
        if (list.size() >= 1) {
            result = list.get(0);
        }
        return result;
    }

    public void save(ClientToken ct) {
        em.persist(ct);
    }

}
