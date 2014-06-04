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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "uploaded_image")
@NamedQueries(value = { @NamedQuery(name = "UploadedImage.listByUId", query = "SELECT u FROM UploadedImage u  WHERE u.user.id=:u_id and u.delFlag=false order by u.date desc") })
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

    private String ip;
    private String ua;

    /**
     * 图片描述显示在alt标签中
     */
    @Column
    private String description;
    /**
     * 图片存储在哪里
     */
    @OneToOne
    @JoinColumn(name = "bucket_id")
    @JsonIgnore
    private StorageBucket storageBucket;
    /**
     * 原始名字显示在title标签中
     */
    @Column(name = "origin_name")
    private String originName;

    @Column
    private String path;

    @Column(name = "del_flag")
    private Boolean delFlag = false;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public StorageBucket getStorageBucket() {
        return storageBucket;
    }

    public void setStorageBucket(StorageBucket storageBucket) {
        this.storageBucket = storageBucket;
    }

    @JsonIgnore
    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    @JsonIgnore
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonIgnore
    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }
}
