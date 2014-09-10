package io.loli.sc.server.entity.pan;

import io.loli.sc.server.entity.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "storage_file")
@Entity
@NamedQueries(value = { @NamedQuery(name = "FileEntity.listByUserIdAndFolderId", query = "SELECT f FROM FileEntity f WHERE f.user.id=:userId and f.folder.id=:folderId and f.delFlag=:delFlag") })
public class FileEntity implements Serializable {

    private static final long serialVersionUID = -2369576302405913467L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "file_name")
    private String originName;

    @Column(name = "sharing_status")
    private String sharingStatus;

    public static final String SHARING = "sharing";
    public static final String PRIVATE = "private";

    @Column(name = "new_name")
    private String newName;

    @Column(name = "file_key")
    private String key;

    @Column(name = "length")
    private Long length;

    @Column(name = "del_flag")
    private Boolean delFlag = false;

    @OneToMany(mappedBy = "file")
    private List<LinkEntity> links;

    public List<LinkEntity> getLinks() {
        return links;
    }

    public void setLinks(List<LinkEntity> links) {
        this.links = links;
    }

    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Transient
    private String size;

    public String getSize() {
        if (length == null) {
            return "";
        }
        String size = "";
        if (length < 1024) {
            size = length + " Byte";
        }
        if (1024 <= length && length < 1024 * 1024) {
            size = String.format("%.2f", (double) length / 1024) + "KB";
        }
        if (1024 * 1024 <= length && length < 1024 * 1024 * 1024) {
            size = String.format("%.2f", (double) length / 1024 / 1024) + "MB";
        }

        if (1024 * 1024 * 1024 <= length && length < 1024 * 1024 * 1024 * 1024) {
            size = String.format("%.2f", (double) length / 1024 / 1024) + "GB";
        }
        return size;
    }

    @Column
    private String md5;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    // 所属的父文件夹
    @ManyToOne
    @JoinColumn(name = "folder_id")
    private FolderEntity folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public FolderEntity getFolder() {
        return folder;
    }

    public void setFolder(FolderEntity folder) {
        this.folder = folder;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSharingStatus() {
        return sharingStatus;
    }

    public void setSharingStatus(String sharingStatus) {
        this.sharingStatus = sharingStatus;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
