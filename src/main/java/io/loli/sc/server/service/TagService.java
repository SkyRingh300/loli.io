package io.loli.sc.server.service;

import io.loli.sc.server.dao.TagDao;
import io.loli.sc.server.entity.Tag;
import io.loli.sc.server.entity.User;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class TagService {
    @Inject
    private TagDao tagDao;

    @Transactional
    public void save(Tag tag) {
        tagDao.save(tag);
    }

    public void refresh(Tag tag) {
        tagDao.refresh(tag);
    }

    public Tag findByNameAndUser(String name, User user) {
        return tagDao.findByNameAndUser(name, user);
    }

    public Tag getById(int id) {
        return tagDao.getById(id);
    }

}
