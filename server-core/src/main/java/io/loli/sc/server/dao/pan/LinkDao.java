package io.loli.sc.server.dao.pan;

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
            .createQuery("from LinkEntity where file.id=:f_id where type='permanent'", LinkEntity.class)
            .setParameter("f_id", fileId).getResultList();
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
            .createQuery("select id from LinkEntity where file.id=:f_id where type='permanent'", Integer.class)
            .setParameter("f_id", fileId).getResultList();
        if (results.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }
}
