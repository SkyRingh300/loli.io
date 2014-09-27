package io.loli.sc.server.social.weibo;

import io.loli.sc.server.social.parent.AuthInfo;
import io.loli.sc.server.social.parent.AuthManager;
import io.loli.sc.server.social.parent.UserInfo;
import io.loli.util.bean.Pair;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WeiboAuthManager extends AuthManager {
    private String authUrl = null;
    private String tokenUrl = null;

    public WeiboAuthManager(AuthInfo info) {
        this.info = info;
        this.authUrl = "https://api.weibo.com/oauth2/authorize?client_id=" + info.getId()
            + "&response_type=code&redirect_uri=" + info.getUrl();

    }

    @Override
    public String getAuthUrl() {
        return authUrl;
    }

    @Override
    public Pair<String, Long> getAccessToken(String code) {
        this.tokenUrl = "https://api.weibo.com/oauth2/access_token?client_id=" + info.getId() + "&client_secret="
            + info.getSecret() + "&grant_type=authorization_code&redirect_uri=" + info.getUrl() + "&code=" + code;
        String result = "";
        String accessToken = "";
        long timeout = 0l;
        try {
            result = this.post(tokenUrl, new ArrayList<>());
            if (StringUtils.isNotBlank(result)) {
                JSONObject obj = new JSONObject(result);
                try {
                    // 如果获取accession_token成功了，下面这句一定会抛出异常
                    String error = obj.getString("error");
                    logger.info("Result is not valid: result=[" + result + "], error is" + error);
                    throw new NullPointerException("Error occurred: " + error);
                } catch (JSONException e) {
                    accessToken = obj.getString("access_token");
                    timeout = obj.getLong("expires_in");
                }
            } else {
                throw new NullPointerException("Result is null while getting token");
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
        // TODO Auto-generated method stub
        return null;
    }

}
