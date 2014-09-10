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

    @Transactional
    public int updateMd5(int id, String md5) {
        return fd.updateMd5(id, md5);
    }

    public FileEntity findById(Integer fileId) {
        return fd.findById(fileId);
    }

    public FileEntity findByMd5(String md5) {
        return fd.findByMd5(md5);
    }

}
