package io.loli.sc.config;

import java.util.Properties;

public class DropboxConfig {
    private String accessToken;
    private String uid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void updateProperties(Properties prop) {
        prop.setProperty("dropbox.accessToken", accessToken);
        prop.setProperty("dropbox.uid", uid);
    }

    public void removeFromProperties(Properties prop) {
        prop.remove("dropbox.accessToken");
        prop.remove("dropbox.uid");
    }
}
