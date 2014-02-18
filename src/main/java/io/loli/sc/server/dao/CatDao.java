package io.loli.sc.server.dao;

import io.loli.sc.server.entity.Cat;

import java.util.List;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Named
public class CatDao {
    @PersistenceContext
    private EntityManager em;

    public void save(Cat cat) {
        em.persist(cat);
    }

    public void update(Cat cat) {
        em.merge(cat);
    }

    /**
     * 根据Cat的id查询出Cat
     * 
     * @param id
     * @return 如果不存在则返回null，如果存在则将此cat返回
     */
    public Cat findById(int id) {
        return em.find(Cat.class, id);
    }

    /**
     * 根据用户的id查询出属于该用户的Cat列表
     * @param uid
     * @return Cat列表
     */
    public List<Cat> listByUId(int uid) {
        return em.createNamedQuery("Cat.listByUId", Cat.class)
                .setParameter("uid", uid).getResultList();
    }
    
}
