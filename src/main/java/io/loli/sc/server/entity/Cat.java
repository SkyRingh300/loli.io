package io.loli.sc.server.entity;

import java.io.Serializable;
import java.util.Set;

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
import javax.persistence.OneToMany;

@Entity
@NamedQueries(value = { @NamedQuery(name = "Cat.listByUId", query = "SELECT c FROM Cat c WHERE c.user.id=:uid") })
public class Cat implements Serializable {

    private static final long serialVersionUID = -144390763136412872L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn
    private User user;
    @Column
    private String name;

    @OneToMany(mappedBy = "cat", cascade = { CascadeType.REFRESH,
            CascadeType.PERSIST })
    private Set<UploadedImage> imageSet;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<UploadedImage> getImageSet() {
        return imageSet;
    }

    public void setImageSet(Set<UploadedImage> imageSet) {
        this.imageSet = imageSet;
    }
}
