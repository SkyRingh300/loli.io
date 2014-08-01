package io.loli.sc.server.entity.pan;

import io.loli.sc.server.entity.User;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "category")
@Entity
public class Category implements Serializable {
    private static final long serialVersionUID = -8167152924335568746L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 直属于该Cat的文件列表，需要加条件判断
    @OneToMany(mappedBy = "category")
    private List<UserFile> fileList;

    // 直属于该Cat的文件夹，需要加条件判断
    @OneToMany(mappedBy = "category")
    private List<Folder> folderList;

    // Cat种类，分为文件、图片等
    @ManyToOne
    @JoinColumn(name = "cat_type_id")
    private CategoryType categoryType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<UserFile> fileList) {
        this.fileList = fileList;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public List<Folder> getFolderList() {
        return folderList;
    }

    public void setFolderList(List<Folder> folderList) {
        this.folderList = folderList;
    }
}
