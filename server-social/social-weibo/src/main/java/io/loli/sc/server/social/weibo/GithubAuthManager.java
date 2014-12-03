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

public class GithubAuthManager extends AuthManager {

    private String authUrl;
    private String tokenUrl;

    public GithubAuthManager(AuthInfo info) {
        this.authUrl = "https://github.com/login/oauth/authorize?client_id=" + info.getId() + "&redirect_uri="
            + info.getUrl();
        this.tokenUrl = "https://github.com/login/oauth/access_token?client_id=" + info.getId() + "&client_secret="
            + info.getSecret() + "&redirect_uri=" + info.getUrl();
    }

    @Override
    public String getAuthUrl() {
        return authUrl;
    }

    @Override
    public boolean cancel(String accessToken) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Pair<String, Long> getAccessToken(String code) {
        this.tokenUrl = tokenUrl + "&code=" + code;
        String result = "";
        String accessToken = "";
        long timeout = 0l;
        try {
            result = this.post(tokenUrl, new ArrayList<>());
            if (StringUtils.isNotBlank(result)) {
                if (result.contains("access_token")) {
                    accessToken = result.substring(result.indexOf("access_token") + 13, result.indexOf("&"));
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
        return getUserInfoByUid(accessToken);
    }

    private UserInfo getUserInfoByUid(String accessToken) {
        String url = "https://api.github.com/user?access_token=" + accessToken;
        String result = "";
        try {
            result = this.get(url);
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
                info.setId(String.valueOf(obj.getInt("id")));
                info.setUsername(obj.getString("login"));
            }
        } else {
            throw new NullPointerException("id is null");
        }
        return info;
    }
}
