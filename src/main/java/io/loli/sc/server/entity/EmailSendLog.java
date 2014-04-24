package io.loli.sc.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries(value = {
        @NamedQuery(name = "EmailSendLog.findByEmail", query = "SELECT e FROM EmailSendLog e WHERE e.email=:email"),
        @NamedQuery(name = "EmailSendLog.findByToken", query = "SELECT e FROM EmailSendLog e WHERE e.token=:token") })
public class EmailSendLog implements Serializable {
    private static final long serialVersionUID = -2336148143458513036L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String email;

    @Column
    private boolean status;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column
    private String token;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
