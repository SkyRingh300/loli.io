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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries(value = { @NamedQuery(name = "UploadedImage.listByUId", query = "SELECT u FROM UploadedImage u WHERE u.user.id=:u_id") })
public class UploadedImage implements Serializable {

    private static final long serialVersionUID = 1398371509051853854L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JoinColumn
    @ManyToOne(cascade = CascadeType.REFRESH)
    private User user;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    /**
     * 图片描述显示在alt标签中
     */
    @Column
    private String description;
    /**
     * 图片存储在哪里
     */
    @OneToOne
    @JoinColumn(name = "storage_id")
    private ImageStorage imageStorage;
    /**
     * 原始名字显示在title标签中
     */
    @Column(name="origin_name")
    private String originName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageStorage getImageStorage() {
        return imageStorage;
    }

    public void setImageStorage(ImageStorage imageStorage) {
        this.imageStorage = imageStorage;
    }
}
