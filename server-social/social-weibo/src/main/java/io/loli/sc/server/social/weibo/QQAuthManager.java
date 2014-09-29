package io.loli.sc.server.social.weibo;

import io.loli.sc.server.social.parent.AuthInfo;
import io.loli.sc.server.social.parent.AuthManager;
import io.loli.sc.server.social.parent.UserInfo;
import io.loli.util.bean.Pair;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class QQAuthManager extends AuthManager {
    private String authUrl = null;
    private String tokenUrl = null;

    public QQAuthManager(AuthInfo info) {
        this.info = info;
        this.authUrl = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=" + info.getId()
            + "&redirect_uri=" + info.getUrl();
        this.tokenUrl = "https://graph.qq.com/oauth2.0/token?client_id=" + info.getId() + "&client_secret="
            + info.getSecret() + "&grant_type=authorization_code&redirect_uri=" + info.getUrl();
    }

    @Override
    public String getAuthUrl() {
        return authUrl;
    }

    @Override
    public Pair<String, Long> getAccessToken(String code) {
        this.tokenUrl = tokenUrl + "&code=" + code;
        String result = "";
        String accessToken = "";
        long timeout = 0l;
        try {
            result = this.get(tokenUrl);
            if (StringUtils.isNotBlank(result)) {
                if (result.contains("access_token") && result.contains("expires_in")) {
                    accessToken = result.substring(result.indexOf("access_token") + 13, result.indexOf("&expires_in"));
                    timeout = Long.valueOf(result.substring(result.indexOf("expires_in") + 11,
                        result.indexOf("&refresh_token")));
                }
            }

        } catch (IOException e) {
            logger.warning("Failed to get accesstoken, url=[" + tokenUrl + "], code=[" + code + "], error is: \n" + e);
        }

        return new Pair<>(accessToken, timeout);
    }

    @Override
    public String refresh(String token) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserInfo getUserInfo(String accessToken) {
        String uid = this.getUid(accessToken);
        return getUserInfoByUid(accessToken, uid);
    }

    private String getUid(String accessToken) {
        String userinfoUrl = "https://graph.qq.com/oauth2.0/me";
        userinfoUrl += "?access_token=" + accessToken;
        String result = "";
        try {
            result = this.get(userinfoUrl);
        } catch (IOException e) {
            logger.warning("Error occurred while get user info: " + e);
        }
        String id = "";
        if (StringUtils.isNotBlank(result)) {
            result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);
                // 如果获取accession_token成功了，下面这句一定会抛出异常
                String error = obj.getString("error");
                logger.info("Result is not valid: result=[" + result + "], error is" + error);
                throw new NullPointerException("Error occurred: " + error);
            } catch (JSONException e) {
                id = String.valueOf(obj.getString("openid"));
            }
        } else {
            throw new NullPointerException("id is null");
        }
        return id;
    }

    private UserInfo getUserInfoByUid(String accessToken, String uid) {
        String userinfoUrl = "https://graph.qq.com/user/get_user_info";
        userinfoUrl += "?access_token=" + accessToken + "&openid=" + uid + "&oauth_consumer_key=" + info.getId();
        String result = "";
        try {
            result = this.get(userinfoUrl);
        } catch (IOException e) {
            logger.warning("Error occurred while get user info: " + e);
        }
        UserInfo info = new UserInfo();
        if (StringUtils.isNotBlank(result)) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(result);

                // 如果获取accession_token成功了，下面这句一定会抛出异常
                String error = obj.getString("error");
                logger.info("Result is not valid: result=[" + result + "], error is" + error);
                throw new NullPointerException("Error occurred: " + error);
            } catch (JSONException e) {
                info.setId(uid);
                info.setUsername(obj.getString("nickname"));
            }
        } else {
            throw new NullPointerException("id is null");
        }
        return info;
    }

}
