package io.loli.sc.config;

import java.util.Properties;

public class GDriveConfig {
    private String accessToken;
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateProperties(Properties prop) {
        prop.setProperty("gdrive.accessToken", accessToken);
        prop.setProperty("gdrive.refreshToken", refreshToken);
    }

    public void removeFromProperties(Properties prop) {
        prop.remove("gdrive.accessToken");
        prop.remove("gdrive.refreshToken");
    }
}
