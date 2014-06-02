package io.loli.sc.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "storage_bucket")
@NamedQueries(value = { @NamedQuery(name = "StorageBucket.list", query = "SELECT s FROM StorageBucket s") })
public class StorageBucket implements Serializable {

    private static final long serialVersionUID = -2588112869005265911L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String name;
    @Column(name = "endpoint")
    private String endPoint;
    @Column(name = "access_key_id")
    private String accessKeyId;
    @Column(name = "access_key_secret")
    private String accessKeySecret;
    @Column
    private String type;

    @Column(name = "upload_url")
    private String uploadUrl;

    public static final String ALI_TYPE = "ali";
    public static final String QN_TYPE = "qn";

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

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

}
