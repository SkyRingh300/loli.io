package io.loli.sc.server.dao.pan;

import io.loli.sc.server.entity.pan.FileEntity;
import io.loli.sc.server.entity.pan.LinkEntity;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class LinkDao {
    @PersistenceContext
    private EntityManager em;

    public void save(LinkEntity link) {
        em.persist(link);
    }

    public LinkEntity findPermLinkByFileId(int fileId) {
        List<LinkEntity> results = em
            .createQuery("from LinkEntity where file.id=:f_id and type=:type", LinkEntity.class)
            .setParameter("f_id", fileId).setParameter("type", LinkEntity.TYPE_PERM).getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    public LinkEntity findById(int id) {
        return em.find(LinkEntity.class, id);
    }

    public boolean checkPermLinkExistsByFileId(int fileId) {
        List<Integer> results = em
            .createQuery("select id from LinkEntity where file.id=:f_id and type=:type", Integer.class)
            .setParameter("f_id", fileId).setParameter("type", LinkEntity.TYPE_PERM).getResultList();
        if (results.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    public FileEntity findfileByPath(String path) {
        return em.createQuery("select file from LinkEntity where path=:path", FileEntity.class)
            .setParameter("path", path).getSingleResult();
    }
}
