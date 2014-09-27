package io.loli.sc.server.social.parent;

/**
 * 应用信息类，保存了这个应用的key和secret
 * 
 * @author choco
 *
 */
public class AuthInfo {
    private String key;
    private String secret;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
