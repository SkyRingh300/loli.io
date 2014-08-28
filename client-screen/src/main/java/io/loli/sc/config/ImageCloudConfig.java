package io.loli.sc.config;

import java.util.Properties;

public class ImageCloudConfig {
    private String email;
    private String token;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    public void updateProperties(Properties prop) {
        prop.setProperty("imageCloud.email", email);
        prop.setProperty("imageCloud.token", token);
    }

    public void removeFromProperties(Properties prop) {
        prop.remove("imageCloud.email");
        prop.remove("imageCloud.token");
    }
}
