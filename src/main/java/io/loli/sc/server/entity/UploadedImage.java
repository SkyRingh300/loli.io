package io.loli.sc.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries(value = { @NamedQuery(name = "UploadedImage.listByCId", query = "SELECT u FROM UploadedImage u WHERE u.cat.id=:cid") })

public class UploadedImage  implements Serializable {

    private static final long serialVersionUID = 1398371509051853854L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    /**
     * 分类
     */
    @ManyToOne(cascade={CascadeType.REFRESH})
    @JoinColumn
    private Cat cat;
    @Column
    private Date date;
    /**
     * 图片描述显示在alt标签中
     */
    @Column
    private String description;
    @Column
    private String path;
    /**
     * 原始名字显示在title标签中
     */
    @Column
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
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
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
