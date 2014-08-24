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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table(name = "storage_folder")
@Entity
@NamedQueries(value = {
        @NamedQuery(name = "FolderEntity.listByUserAndPath", query = "SELECT f FROM FolderEntity f WHERE f.user.id=:userId and f.fullPath=:path"),
        @NamedQuery(name = "FolderEntity.listByUserAndParent", query = "SELECT f FROM FolderEntity f WHERE f.user.id=:userId and f.parent.id=:parentId") })
public class FolderEntity implements Serializable {

    private static final long serialVersionUID = 1369695803049170073L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private FolderEntity parent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @Column
    private String name;

    @Column(name = "full_path")
    private String fullPath;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    // 直属子文件夹
    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<FolderEntity> children;

    // 直属子文件
    @OneToMany(mappedBy = "folder")
    @JsonIgnore
    private List<FileEntity> fileList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FolderEntity getParent() {
        return parent;
    }

    public void setParent(FolderEntity parent) {
        this.parent = parent;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<FolderEntity> getChildren() {
        return children;
    }

    public void setChildren(List<FolderEntity> children) {
        this.children = children;
    }

    public List<FileEntity> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileEntity> fileList) {
        this.fileList = fileList;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }
}
