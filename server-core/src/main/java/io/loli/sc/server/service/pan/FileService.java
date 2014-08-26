package io.loli.sc.server.service.pan;

import io.loli.sc.server.dao.pan.FileDao;
import io.loli.sc.server.entity.pan.FileEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class FileService {
    @Inject
    private FileDao fd;

    @Transactional
    public void save(FileEntity file) {
        fd.save(file);
    }

    public List<FileEntity> listByUserIdAndFolderId(int userId, int folderId, int startIndex, int maxCount) {

        return fd.listByUserIdAndFolderId(userId, folderId, startIndex, maxCount);
    }

}
