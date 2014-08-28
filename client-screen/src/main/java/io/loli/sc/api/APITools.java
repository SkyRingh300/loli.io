package io.loli.sc.api;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class APITools {
    /**
     * 发出post请求
     */
    public String post(String postUrl, List<NameValuePair> params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost hp = new HttpPost(postUrl);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            hp.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            response = httpclient.execute(hp);
            result = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public String get(String getUrl){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet hp = new HttpGet(getUrl);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            response = httpclient.execute(hp);
            result = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
