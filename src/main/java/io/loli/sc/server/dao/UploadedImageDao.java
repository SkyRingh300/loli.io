package io.loli.sc.server.dao;

import io.loli.sc.server.entity.UploadedImage;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class UploadedImageDao {
    @PersistenceContext
    private EntityManager em;

    public void save(UploadedImage image) {
        em.persist(image);
    }

    public void update(UploadedImage image) {
        em.merge(image);
    }

    /**
     * 根据图片id将查询出此图片的信息
     * 
     * @param id
     * @return 查询出的图片，如果没有此id的图片，则返回null
     */
    public UploadedImage findById(int id) {
        return em.find(UploadedImage.class, id);
    }

    /**
     * 根据Cat的id查询出该cat下的所有图片
     * 
     * @param cid
     * @return 该cat下的所有图片的列表
     */
    public List<UploadedImage> listByCId(int cid) {
        return em
                .createNamedQuery("UploadedImage.listByCId",
                        UploadedImage.class).setParameter("cid", cid)
                .getResultList();
    }
}
