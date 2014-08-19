package io.loli.sc.server.dao.pan;

import io.loli.sc.server.entity.pan.FolderEntity;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class FolderDao {

    @PersistenceContext
    private EntityManager em;

    public void save(FolderEntity folder) {
        em.persist(folder);
    }

    public List<FolderEntity> listByUserAndPath(int userId, String path) {
        return em.createNamedQuery("FolderEntity.listByUserAndPath", FolderEntity.class)
                .setParameter("userId", userId).setParameter("path", path).getResultList();

    }

    public FolderEntity findById(int id) {
        return em.find(FolderEntity.class, id);
    }

    public List<FolderEntity> listByUserAndParent(int userId, int pid) {
        return em.createNamedQuery("FolderEntity.listByUserAndParent", FolderEntity.class)
                .setParameter("userId", userId).setParameter("parentId", pid).getResultList();
    }
}
