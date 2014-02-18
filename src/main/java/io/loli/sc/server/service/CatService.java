package io.loli.sc.server.service;

import io.loli.sc.server.dao.CatDao;
import io.loli.sc.server.entity.Cat;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Named
public class CatService {
    @Inject
    private CatDao catDao;
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Cat cat) {
        catDao.save(cat);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Cat cat) {
        catDao.update(cat);
    }
    public Cat findById(int id){
        return catDao.findById(id);
    }
    
    public List<Cat> listByUId(int uid){
        return catDao.listByUId(uid);
    }
    
}
