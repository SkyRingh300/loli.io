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

    public List<FileEntity> listByUserIdAndFolderId(int userId, int folderId, int startIndex, int maxCount) {
        return em.createNamedQuery("FileEntity.listByUserIdAndFolderId", FileEntity.class)
            .setParameter("userId", userId).setParameter("folderId", folderId).setFirstResult(startIndex)
            .setMaxResults(maxCount).getResultList();
    }

    public int updateMd5(int id, String md5) {
        return em.createQuery("update FileEntity set md5=:md5 where id=:id").setParameter("md5", md5)
            .setParameter("id", id).executeUpdate();
    }

    public FileEntity findById(Integer fileId) {
        return em.find(FileEntity.class, fileId);
    }

    public FileEntity findByMd5(String md5) {
        return em.createQuery("from FileEntity where md5=:md5 and delFlag=:delFlag", FileEntity.class)
            .setParameter("md5", md5).setParameter("delFlag", false).getSingleResult();

    }

}
