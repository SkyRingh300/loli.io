package io.loli.sc.server.entity;

import io.loli.util.string.MD5Util;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "login_status")
@NamedQueries(value = { @NamedQuery(name = "LoginStatus.findByUId", query = "SELECT c FROM LoginStatus c WHERE c.user.id=:uid") })
public class LoginStatus implements Serializable {
    private static final long serialVersionUID = -8086904075643989154L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    @JoinColumn(name = "u_id")
    private User user;
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String generateToken() throws NoSuchAlgorithmException {
        String str = this.getUser().getEmail() + new Date().getTime();
        return MD5Util.hash(str);
    }

}
