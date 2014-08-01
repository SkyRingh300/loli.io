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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Table(name = "folder")
@Entity
public class Folder implements Serializable {

    private static final long serialVersionUID = 1369695803049170073L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    // 所属的Cat，如果此folder为子目录，则为其父目录的Cat
    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Category category;
    // 和上面的category相对，如果是直属于Cat的文件，则parent为空
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Folder parent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    // 直属子文件夹
    @OneToMany(mappedBy = "parent")
    private List<Folder> children;

    // 直属子文件
    @OneToMany(mappedBy = "folder")
    private List<UserFile> fileList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Folder getParent() {
        return parent;
    }

    public void setParent(Folder parent) {
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

    public List<Folder> getChildren() {
        return children;
    }

    public void setChildren(List<Folder> children) {
        this.children = children;
    }

    public List<UserFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<UserFile> fileList) {
        this.fileList = fileList;
    }
}
