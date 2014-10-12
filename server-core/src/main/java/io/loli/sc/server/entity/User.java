package io.loli.sc.server.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
@NamedQueries(value = { @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email=:email"),
    @NamedQuery(name = "User.listByToken", query = "SELECT u FROM User u WHERE u.loginStatus.token=:token") })
public class User implements Serializable {

    private static final long serialVersionUID = -6060393006470256261L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String email;
    @Column(name = "reg_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
    @Column
    @JsonIgnore
    private String password;

    @Column
    private String name;

    @Column
    private String type;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Social> socials;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private LoginStatus loginStatus;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<UploadedImage> imageList;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Tag> tagList;

    @Column
    private Boolean vip = true;

    public Boolean getVip() {
        return vip;
    }

    public void setVip(Boolean vip) {
        this.vip = vip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UploadedImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<UploadedImage> imageList) {
        this.imageList = imageList;
    }

    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Social> getSocials() {
        return socials;
    }

    public void setSocials(List<Social> socials) {
        this.socials = socials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
