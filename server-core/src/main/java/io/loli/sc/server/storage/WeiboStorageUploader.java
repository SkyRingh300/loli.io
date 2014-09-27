package io.loli.sc.server.storage;

import io.loli.sc.server.util.HttpsClientFactory;
import io.loli.util.bean.Pair;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeiboStorageUploader extends StorageUploader {

    public static final String UPDATE_URL = "https://upload.api.weibo.com/2/statuses/upload.json";

    private final String ACCESSION_TOKEN;
    private static final HttpClient client = HttpsClientFactory.getInstance();

    public WeiboStorageUploader(String accessionToken) {
        ACCESSION_TOKEN = accessionToken;
    }

    @Override
    public String upload(File file) {
        return this.upload(file, null);
    }

    @Override
    public String upload(File file, String contentType) {
        try {
            return httpsPost(UPDATE_URL, file);
        } catch (ParseException | IOException | JSONException e) {
            return null;
        }
    }

    @Override
    public void delete(String file) {

    }

    private String httpsPost(String url, final File file) throws ParseException, IOException, JSONException {
        HttpPost hp = new HttpPost(UPDATE_URL);
        MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
        // 可以直接addBinary
        multiPartEntityBuilder.addPart("pic",
            new FileBody(file, ContentType.create("application/octet-stream", Consts.UTF_8), "pic"));
        multiPartEntityBuilder.setCharset(Consts.UTF_8);
        // 可以直接addText
        multiPartEntityBuilder.addPart("access_token",
            new StringBody(ACCESSION_TOKEN, ContentType.create("text/plain", Consts.UTF_8)));
        multiPartEntityBuilder.addPart("status",
            new StringBody(String.valueOf(new Date().getTime()), ContentType.create("text/plain", Consts.UTF_8)));

        hp.setEntity(multiPartEntityBuilder.build());
        HttpResponse response = client.execute(hp);
        String result = EntityUtils.toString(response.getEntity());

        JSONObject json = new JSONObject(result);
        return (String) json.get("original_pic");
    }

    public Pair<Integer, Integer> getLimit() {
        String url = "https://api.weibo.com/2/account/rate_limit_status.json?access_token=" + ACCESSION_TOKEN;
        HttpGet get = new HttpGet(url);
        String result = null;
        HttpResponse response = null;
        try {
            response = client.execute(get);
            result = EntityUtils.toString(response.getEntity());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            JSONObject json = new JSONObject(result);
            JSONArray array = json.getJSONArray("api_rate_limits");
            JSONObject obj = array.getJSONObject(0);
            JSONObject obj2 = array.getJSONObject(5);
            return new Pair<>((int) obj.get("remaining_hits"), (int) obj2.get("remaining_hits"));

        } catch (JSONException e) {
            e.printStackTrace();
            return new Pair<>(0, 0);
        }

    }
}
