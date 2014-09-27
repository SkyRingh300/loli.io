package io.loli.sc.server.social.parent;

/**
 * 应用信息类，保存了这个应用的id以及回调地址
 * 
 * @author choco
 *
 */
public class AuthInfo {
    private String id;
    private String secret;
    private String url;

    public AuthInfo(String id, String secret, String url) {
        this.id = id;
        this.secret = secret;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setKId(String id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}
