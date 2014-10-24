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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "uploaded_image")
@NamedQueries(value = {
    @NamedQuery(name = "UploadedImage.listByUId", query = "SELECT u FROM UploadedImage u  WHERE u.user.id=:u_id and u.delFlag=false order by u.date desc"),
    @NamedQuery(name = "UploadedImage.listByUIdAndFileName", query = "SELECT u FROM UploadedImage u  WHERE u.originName like :file_name and u.user.id=:u_id and u.delFlag=false order by u.date desc"),
    @NamedQuery(name = "UploadedImage.listByUIdAndFileNameAndTag", query = "SELECT u FROM UploadedImage u  WHERE u.originName like :file_name and u.user.id=:u_id and u.delFlag=false and u.tag.id=:tag_id order by u.date desc") })
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

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "generated_code")
    private String generatedCode;

    public String getGeneratedCode() {
        return generatedCode;
    }

    public void setGeneratedCode(String generatedCode) {
        this.generatedCode = generatedCode;
    }

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

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

    @Column(name = "redirect_code")
    private String redirectCode;

    @Column
    private String path;

    @Column(name = "smallPath")
    private String smallPath;

    @Column(name = "small_square_path")
    private String smallSquarePath;

    @Column(name = "middlePath")
    private String middlePath;

    @Column(name = "largePath")
    private String largePath;

    @Column(name = "generated_name")
    private String generatedName;

    @Column(name = "internal_path")
    private String internalPath;

    @Column(name = "del_flag")
    private Boolean delFlag = false;

    @Column
    private Boolean share = false;

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

    public String getRedirectCode() {
        return redirectCode;
    }

    public void setRedirectCode(String redirectCode) {
        this.redirectCode = redirectCode;
    }

    public String getGeneratedName() {
        return generatedName;
    }

    public void setGeneratedName(String generatedName) {
        this.generatedName = generatedName;
    }

    public String getInternalPath() {
        return internalPath;
    }

    public void setInternalPath(String internalPath) {
        this.internalPath = internalPath;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getShare() {
        return share;
    }

    public void setShare(Boolean share) {
        this.share = share;
    }

    public String getSmallPath() {
        return smallPath;
    }

    public void setSmallPath(String smallPath) {
        this.smallPath = smallPath;
    }

    public String getMiddlePath() {
        return middlePath;
    }

    public void setMiddlePath(String middlePath) {
        this.middlePath = middlePath;
    }

    public String getLargePath() {
        return largePath;
    }

    public void setLargePath(String largePath) {
        this.largePath = largePath;
    }
}
