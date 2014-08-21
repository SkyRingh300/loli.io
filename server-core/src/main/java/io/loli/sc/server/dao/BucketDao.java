package io.loli.sc.server.dao;

import io.loli.sc.server.entity.StorageBucket;

import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
@Singleton
public class BucketDao {
    @PersistenceContext
    private EntityManager em;

    public List<StorageBucket> list() {
        return em.createNamedQuery("StorageBucket.list", StorageBucket.class)
                .getResultList();
    }
}
