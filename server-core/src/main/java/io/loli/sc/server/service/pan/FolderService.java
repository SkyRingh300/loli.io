package io.loli.sc.server.service.pan;

import io.loli.sc.server.config.FileListConfig;
import io.loli.sc.server.dao.pan.FolderDao;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.entity.pan.FolderEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.collections4.ListUtils;

import com.google.common.collect.Lists;

@Named
public class FolderService {
    @Inject
    private FolderDao folderDao;

    @Transactional
    public void save(FolderEntity folder) {
        folderDao.save(folder);
    }

    @Transactional
    public List<FolderEntity> listByUserAndPath(User user, int pid, int startIndex, int maxCount) {
        List<FolderEntity> result = null;
        if (pid == 0) {
            result = folderDao.listByUserAndPath(user.getId(), "/");
            if (result.isEmpty()) {
                FolderEntity f = new FolderEntity();
                f.setParent(null);
                f.setUser(user);
                f.setCreateDate(new Date());
                f.setFullPath("/");
                f.setName("");
                folderDao.save(f);
            }
        }

        if (pid == 0) {
            result = folderDao.listByUserAndParent(user.getId(), this.findRootByUser(user).getId(), startIndex,
                maxCount);
        } else {
            result = folderDao.listByUserAndParent(user.getId(), pid, startIndex, maxCount);
        }

        return result;
    }

    public FolderEntity findById(int id) {
        return folderDao.findById(id);
    }

    @Transactional
    public FolderEntity findRootByUser(User user) {
        List<FolderEntity> list = folderDao.listByUserAndPath(user.getId(), "/");
        FolderEntity root = null;
        if (!list.isEmpty()) {
            root = list.get(0);
        } else {
            FolderEntity f = new FolderEntity();
            f.setParent(null);
            f.setUser(user);
            f.setCreateDate(new Date());
            f.setFullPath("/");
            f.setName("");
            folderDao.save(f);
            root = f;
        }
        return root;
    }

    public List<FolderEntity> findParentsByFolder(Integer pid) {
        FolderEntity child = this.findById(pid);
        List<FolderEntity> list = Lists.newArrayList();
        boolean flag = true;
        FolderEntity temp = child;
        while (flag) {
            if (temp == null) {
                flag = false;
            } else {
                list.add(temp);
                temp = temp.getParent();
            }
        }
        Collections.reverse(list);
        return list;

    }

}
