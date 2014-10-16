package io.loli.sc.server.social.parent;

import io.loli.util.bean.Pair;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

/**
 * 所有认证类都必须继承这个抽象类<br>
 * 
 * @author choco
 *
 */
public abstract class AuthManager {

    protected static Logger logger = Logger.getLogger(AuthManager.class.getCanonicalName());

    protected AuthInfo info;

    /**
     * 获取认证用的URL地址，用于用户点击授权
     * 
     * @return 返回本应用的授权页面URL
     */
    public abstract String getAuthUrl();

    /**
     * 取消授权
     * 
     * @return 是否取消成功
     */
    public abstract boolean cancel(String accessToken);

    /**
     * 用户授权后会返回一个code，通过此code能获取到该用户的accessToken
     * 
     * @param code 用户授权后的code
     * @return 返回通过此code获取到的accessToken, 如果是永久的 则值为0
     */
    public abstract Pair<String, Long> getAccessToken(String code);

    /**
     * 用旧的accessToken获取新的accessToken
     * 
     * @param token 旧的accessToken
     * @return 返回新的accessToken
     */
    public abstract String refresh(String token);

    public AuthInfo getAuthInfo() {
        return info;
    }

    protected String post(String target, List<NameValuePair> params) throws ClientProtocolException, IOException {
        HttpClient client = HttpsClientFactory.getInstance();
        HttpPost hp = new HttpPost(target);
        hp.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = client.execute(hp);
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }

    protected String get(String target) throws ClientProtocolException, IOException {
        HttpClient httpclient = HttpsClientFactory.getSimpleInstance();
        HttpGet hp = new HttpGet(target);
        HttpResponse response = httpclient.execute(hp);
        String result = EntityUtils.toString(response.getEntity());
        return result;
    }

    public abstract UserInfo getUserInfo(String accessToken);
}
