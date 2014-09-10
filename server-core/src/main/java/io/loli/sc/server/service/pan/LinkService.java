package io.loli.sc.server.service.pan;

import io.loli.sc.server.dao.pan.LinkDao;
import io.loli.sc.server.entity.pan.FileEntity;
import io.loli.sc.server.entity.pan.LinkEntity;
import io.loli.util.string.MD5Util;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.log4j.Logger;

@Named
public class LinkService {

    private static final Logger logger = Logger.getLogger(LinkService.class);

    @Inject
    private LinkDao linkDao;

    @Transactional
    public void save(LinkEntity link) {
        linkDao.save(link);
    }

    @Transactional
    public LinkEntity getPermLinkByFileId(FileEntity file) {
        if (!linkDao.checkPermLinkExistsByFileId(file.getId())) {
            LinkEntity link = new LinkEntity();
            link.setFile(file);
            link.setCreateDate(new Date());
            String newPath = null;
            try {
                newPath = MD5Util.hash(file.getMd5() + System.nanoTime());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                logger.error(e);
                newPath = String.valueOf(System.nanoTime());
            }
            link.setPath(newPath);
            link.setTime(0);
            link.setType(LinkEntity.TYPE_PERM);
            linkDao.save(link);
        }
        return linkDao.findPermLinkByFileId(file.getId());

    }

    public FileEntity findFileByPath(String path) {
        return linkDao.findfileByPath(path);
    }
}
