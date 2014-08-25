package io.loli.sc.server.dao.pan;

import java.util.List;

import io.loli.sc.server.entity.pan.FileEntity;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class FileDao {

    @PersistenceContext
    private EntityManager em;

    public void save(FileEntity file) {
        em.persist(file);
    }

    public List<FileEntity> listByUserIdAndFolderId(int userId, int folderId) {
        return em.createNamedQuery("FileEntity.listByUserIdAndFolderId", FileEntity.class)
            .setParameter("userId", userId).setParameter("folderId", folderId).getResultList();
    }

}
