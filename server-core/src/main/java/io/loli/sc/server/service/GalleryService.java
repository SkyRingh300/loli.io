package io.loli.sc.server.service;

import io.loli.sc.server.dao.GalleryDao;
import io.loli.sc.server.dao.UploadedImageDao;
import io.loli.sc.server.entity.Gallery;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.entity.User;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
public class GalleryService {

    @Inject
    private GalleryDao gd;

    @Inject
    private UploadedImageDao uid;

    @Transactional
    public void save(Gallery g) {
        g.setDate(new Date());
        gd.save(g);
    }

    @Transactional
    public void update(Gallery g) {
        gd.update(g);
    }

    @Transactional
    public void delete(Gallery g) {
        g.setDelFlag(false);
    }

    public Gallery findById(int gid) {
        return gd.findById(gid);
    }

    @Transactional
    public void addImageToGallery(int imageId, int gid) {
        Gallery g = this.findById(gid);

        UploadedImage img = uid.findById(imageId);
        g.getImages().add(img);

        gd.update(g);
    }

    @Transactional
    public void removeImageFromGallery(int imageId, int gid) {
        Gallery g = this.findById(gid);

        UploadedImage img = uid.findById(imageId);
        UploadedImage imgToRemove = null;
        for (UploadedImage image : g.getImages()) {
            if (image.getId() == img.getId()) {
                imgToRemove = image;
                break;
            }
        }
        if (imgToRemove != null) {
            g.getImages().remove(imgToRemove);
            gd.update(g);
        }

    }

    public List<Gallery> listByUser(User user) {
        return gd.listByUserId(user.getId());
    }

    public List<Gallery> listByUser(int uid) {
        return gd.listByUserId(uid);
    }

    public List<Gallery> listByUserReversed(int uid) {
        return gd.listByUserIdReversed(uid);
    }

    @Transactional
    public void update(Integer gid, String title, String description, User user) {
        Gallery g = this.findById(gid);
        if (g.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("该相册不属于你");
        }
        g.setTitle(title);
        g.setDescription(description);
    }

    /**
     * 根据gallery的id，类型，以及用户实体类删除
     * 
     * @param gid 需要删除的相册id
     * @param type 类型，如果为image则表示连同图片删除
     * @param user 用户实体类，用于判断该相册是否属于该用户
     * @throws IllegalArgumentException 当该相册不属于该用户时抛出此异常
     */
    @Transactional
    public void delete(Integer gid, String type, User user) {
        Gallery g = this.findById(gid);
        if (g.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("该相册不属于你");
        }
        g.setDelFlag(true);
        if ("image".equals(type)) {
            for (UploadedImage image : g.getImages()) {
                image.setDelFlag(true);
                image.setGallery(null);
            }
        } else {
            for (UploadedImage image : g.getImages()) {
                image.setGallery(null);
            }
        }
    }
}
