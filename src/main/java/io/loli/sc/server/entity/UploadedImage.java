package io.loli.sc.server.entity;

import java.util.Date;

public class UploadedImage {
    private int id;
    /**
     * 分类
     */
    private Cat cat;
    private Date date;
    /**
     * 图片描述显示在alt标签中
     */
    private String desc;
    private String path;
    /**
     * 原始名字显示在title标签中
     */
    private String originName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }
}
