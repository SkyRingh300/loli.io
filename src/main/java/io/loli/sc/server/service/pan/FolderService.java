package io.loli.sc.server.service.pan;

import io.loli.sc.server.dao.pan.FolderDao;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.entity.pan.FolderEntity;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class FolderService {
    @Inject
    private FolderDao folderDao;

    @Transactional
    public void save(FolderEntity folder) {
        folderDao.save(folder);
    }

    @Transactional
    public List<FolderEntity> listByUserAndPath(User user, int pid) {
        List<FolderEntity> result;
        if (pid == 0) {
            result = folderDao.listByUserAndPath(user.getId(), "/");
        } else {
            result = folderDao.listByUserAndParent(user.getId(), pid);
        }
        if (result.isEmpty()) {
            FolderEntity f = new FolderEntity();
            f.setParent(null);
            f.setUser(user);
            f.setCreateDate(new Date());
            f.setFullPath("/");
            f.setName("");
            folderDao.save(f);
        } else {
            if (pid == 0) {
                result = folderDao.listByUserAndPath(user.getId(), "/");
            } else {
                result = folderDao.listByUserAndParent(user.getId(), pid);
            }
        }

        return result;
    }

    public FolderEntity findById(int id) {
        return folderDao.findById(id);
    }

    // @Transactional
    // public FolderEntity findByUser(User user) {
    // List<FolderEntity> list = folderDao.listByUserAndPath(user.getId(), "/");
    //
    // if (!list.isEmpty()) {
    //
    // }
    // }

}
