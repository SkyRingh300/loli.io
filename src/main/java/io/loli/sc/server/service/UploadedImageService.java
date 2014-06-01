package io.loli.sc.server.service;

import io.loli.sc.server.dao.UploadedImageDao;
import io.loli.sc.server.entity.UploadedImage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

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

    public void update(UploadedImage image) {
        ud.update(image);
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

    private int maxResults = 20;

    /**
     * 分页查询出指定用户的截图列表
     * 
     * @param u_id 用户id
     * @param firstPosition 初始位置
     * @param maxResults 每页的最大数量
     * @return 截图列表
     */
    public List<UploadedImage> listByUId(int u_id, int firstPosition,
            int maxResults) {
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

    public List<UploadedImage> listTest(int firstPosition) {
        return ud.listTest(firstPosition);
    }

}
