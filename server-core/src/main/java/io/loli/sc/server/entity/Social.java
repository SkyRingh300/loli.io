package io.loli.sc.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "social")
@Entity
public class Social implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_WEIBO = "weibo";

    public static final String TYPE_QQ = "qq";

    public static final String TYPE_GITHUB = "github";
    
    
    public static final String[] TYPES = { TYPE_WEIBO, TYPE_QQ, TYPE_GITHUB };


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String type;
    @Column(name = "access_token")
    private String accessToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String uid;
    private String name;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    private long expired;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public long getExpired() {
        return expired;
    }

    public void setExpired(long expired) {
        this.expired = expired;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
