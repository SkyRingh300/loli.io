package io.loli.sc.server.service;

import io.loli.sc.server.dao.UploadedImageDao;
import io.loli.sc.server.entity.UploadedImage;
import io.loli.sc.server.storage.StorageUploader;
import io.loli.util.image.ThumbnailUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;
import org.springframework.transaction.annotation.Transactional;

@Named("imageService")
public class UploadedImageService {

    @Inject
    @Named("imageDao")
    private UploadedImageDao ud;

    @Transactional
    public void save(UploadedImage image) {
        ud.save(image);
    }

    @Transactional
    public void update(UploadedImage image) {
        ud.update(image);
    }

    @Transactional
    public void delete(int id) {
        UploadedImage image = this.findById(id);
        // StorageBucket sb = image.getStorageBucket();
        // StorageUploader.newInstance(sb).delete(image.getPath().substring(image.getPath().lastIndexOf("/")
        // + 1));
        // System.out.println(image.getPath().substring(image.getPath().indexOf("/")
        // + 1));
        image.setDelFlag(true);
    }

    /**
     * 根据图片id将查询出此图片的信息
     * 
     * @param id
     * @return 查询出的图片，如果没有此id的图片，则返回null
     */
    public UploadedImage findById(int id) {
        return ud.findById(id);
    }

    private int maxResults = 24;

    /**
     * 分页查询出指定用户的截图列表
     * 
     * @param u_id 用户id
     * @param firstPosition 初始位置
     * @param maxResults 每页的最大数量
     * @return 截图列表
     */
    public List<UploadedImage> listByUId(int u_id, int firstPosition, int maxResults) {
        return ud.listByUId(u_id, firstPosition, maxResults);
    }

    /**
     * 不包含分页参数的查询，默认查询出20行
     * 
     * @param u_id 用户id
     * @param firstPosition 开始位置
     * @return 截图列表
     */
    public List<UploadedImage> listByUId(int u_id, int firstPosition) {
        return this.listByUId(u_id, firstPosition, maxResults);
    }

    public List<UploadedImage> listByUIdAndFileName(int u_id, String fileName, int firstPosition, Integer tag) {
        System.out.println(tag);
        return this.listByUIdAndFileName(u_id, fileName, firstPosition, maxResults, tag);
    }

    public List<UploadedImage> listByUIdAndFileName(int u_id, String fileName, int firstPosition, int maxResults,
        Integer tag) {
        if (tag == 0 || tag == null) {
            return ud.listByUIdAndFileName(u_id, fileName, firstPosition, maxResults);
        } else {
            return ud.listByUIdAndFileName(u_id, fileName, firstPosition, maxResults, tag);
        }
    }

    public int countByUId(int u_id) {
        return ud.countByUId(u_id);
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int countByUIdAndFileName(int u_id, String fileName) {
        return ud.countByUIdAndFileName(u_id, fileName);
    }

    public int countByUIdAndFileName(int u_id, String fileName, Integer tag) {
        if (tag == 0 || tag == null) {
            return ud.countByUIdAndFileName(u_id, fileName);
        } else {
            return ud.countByUIdAndFileName(u_id, fileName, tag);
        }
    }

    public boolean checkExists(String code) {
        List<UploadedImage> result = ud.checkExists(code);
        if (result.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public UploadedImage findByCode(String redirectCode) {
        List<UploadedImage> images = ud.findByCode(redirectCode);
        if (images.isEmpty() || images.size() > 1) {
            throw new IllegalArgumentException("Invalid code:" + redirectCode);
        } else {
            return images.get(0);
        }
    }

    @Transactional
    public void updateThumbnail(UploadedImage image, File file, StorageUploader uploader) {
        String format = file.getName().contains(".") ? file.getName().substring(file.getName().lastIndexOf(".") + 1)
            : "png";

        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            File f0 = new File(tempDir, image.getGeneratedCode() + "q." + format);
            toFile(ThumbnailUtil.cutSqureWithResizeSmall(new BufferedInputStream(new FileInputStream(file)), format),
                f0);
            uploader.upload(f0, image.getContentType());
            image.setSmallSquareName(f0.getName());

            File f1 = new File(tempDir, image.getGeneratedCode() + "s." + format);
            toFile(ThumbnailUtil.resizeSmall(new BufferedInputStream(new FileInputStream(file)), format), f1);
            uploader.upload(f1, image.getContentType());
            image.setSmallName(f1.getName());

            File f2 = new File(tempDir, image.getGeneratedCode() + "m." + format);
            toFile(ThumbnailUtil.resizeMiddle(new BufferedInputStream(new FileInputStream(file)), format), f2);
            uploader.upload(f2, image.getContentType());
            image.setMiddleName(f2.getName());

            File f3 = new File(tempDir, image.getGeneratedCode() + "l." + format);
            toFile(ThumbnailUtil.resizeBig(new BufferedInputStream(new FileInputStream(file)), format), f3);
            uploader.upload(f3, image.getContentType());
            image.setLargeName(f3.getName());

            this.update(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void toFile(OutputStream os, File file) {
        try {
            FileUtils.writeByteArrayToFile(file, ((ByteArrayOutputStream) os).toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageType(File file) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file);) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (!iter.hasNext()) {
                return "";
            }

            ImageReader reader = iter.next();

            return reader.getFormatName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // The image could not be read
        return "";
    }
}
