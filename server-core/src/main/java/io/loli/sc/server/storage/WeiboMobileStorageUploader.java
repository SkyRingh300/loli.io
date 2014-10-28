package io.loli.sc.server.storage;

import io.loli.sc.server.util.Base64Util;
import io.loli.sc.server.util.HttpsClientFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class WeiboMobileStorageUploader extends StorageUploader {
    private final String cookie;

    private static final String UPLOAD_URL = "http://picupload.service.weibo.com/interface/pic_upload.php?&mime=image%2Fjpeg&data=base64&url=0&markpos=1&logo=&nick=0&marks=1&app=miniblog";

    private static final HttpClient client = HttpsClientFactory.getSimpleInstance();

    private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

    private WeiboMobileStorageUploader(String cookie) {
        service.schedule(() -> {
            try {
                client.execute(this.buildGet("http://weibo.com", cookie));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, TimeUnit.MINUTES);

        this.cookie = cookie;
    }

    private static WeiboMobileStorageUploader instance;

    public static synchronized WeiboMobileStorageUploader getInstance(String cookie) {
        if (instance == null) {
            instance = new WeiboMobileStorageUploader(cookie);
        }
        return instance;
    }

    @Override
    public String upload(File file) {
        return this.upload(file, null);
    }

    @Override
    public String upload(File file, String contentType) {
        String result = "";
        try {
            HttpPost post = this.buildPost(UPLOAD_URL, cookie);
            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("b64_data", Base64Util.encode(file)));
            post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
            HttpResponse response = client.execute(post);
            result = EntityUtils.toString(response.getEntity());
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(result);
            scanner.nextLine();
            JSONObject json = new JSONObject(scanner.nextLine());
            result = json.getJSONObject("data").getJSONObject("pics").getJSONObject("pic_1").getString("pid");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return pid2url(result, "large");
    }

    private static String pid2url(String pid, String type) {
        String url = "";
        if (StringUtils.isBlank(type))
            type = "bmiddle";
        if (pid.charAt(9) == 'w') {
            long zone = (crc32(pid) & 3) + 1;
            String ext = (pid.charAt(21) == 'g') ? "gif" : "jpg";
            url = "http://ww" + zone + ".sinaimg.cn/" + type + "/" + pid + "." + ext;
        }
        return url;
    }

    private static long crc32(String str) {
        CRC32 crc32 = new CRC32();
        crc32.update(str.getBytes());
        return Long.valueOf(crc32.getValue());
    }

    @Override
    public void delete(String file) {

    }

    public HttpPost buildPost(String url, String cookie) {
        RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).build();
        HttpPost post = new HttpPost(url);
        post.setConfig(config);
        post.addHeader("Cookie", cookie);
        post.addHeader("Host", "picupload.service.weibo.com");
        return post;
    }

    public HttpGet buildGet(String url, String cookie) {
        HttpGet get = new HttpGet(url);
        get.addHeader("Cookie", cookie);
        return get;
    }

}
