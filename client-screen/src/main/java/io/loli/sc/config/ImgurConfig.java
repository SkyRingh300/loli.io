package io.loli.sc.config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ImgurConfig {
    private Date date;
    private String accessToken;
    private String refreshToken;
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
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
    public void updateProperties(Properties prop){
        prop.setProperty("imgur.date", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        prop.setProperty("imgur.accessToken", accessToken);
        prop.setProperty("imgur.refreshToken", refreshToken);
    }
    public void removeFromProperties(Properties prop){
        prop.remove("imgur.date");
        prop.remove("imgur.accessToken");
        prop.remove("imgur.refreshToken");
    }
}
