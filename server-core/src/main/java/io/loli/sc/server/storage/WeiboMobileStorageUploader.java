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

    public static void main(String[] args) {
        System.out
            .println(new WeiboMobileStorageUploader(
                "SINAGLOBAL=4454861709382.385.1384874481326; __utma=182865017.1370609561.1403104756.1403104756.1403104756.1; __utmz=182865017.1403104756.1.1.utmcsr=club.weibo.com|utmccn=(referral)|utmcmd=referral|utmcct=/; sgsa_id=weibo.com|1403508100187533; tma=15428400.53569089.1403522406308.1403522406308.1403522406308.1; tmd=1.15428400.53569089.1403522406308.; login_sid_t=1270235c76eaf149862046c6dbe6c72c; _s_tentry=login.sina.com.cn; Apache=4109970608260.4824.1411134135439; ULV=1411134135448:132:6:3:4109970608260.4824.1411134135439:1410799344983; appkey=; user_active=201409192212; user_unver=00382a5a486d613b61734c767fe52087; myuid=1775848881; UOR=os.51cto.com,widget.weibo.com,login.sina.com.cn; SUS=SID-1775848881-1411199883-XD-uoq21-e445aba05de8b74daff16bf75ee5fb71; SUE=es%3D390ac7fa18efb02d6994cb1569dd8708%26ev%3Dv1%26es2%3Dfcbdd975c2d304f866d0ee02687355ec%26rs0%3DcHSaneWjsVP%252F7jahHNX29KGkWmubNNf0RNRh3zM52DIcDiNzuvzdZaKsZ9sKC0rgPPyCxz5pOUweV7V%252FzRNvW2OsI70P11vJT8ryKRdAY3hJ7l8u2WZYUwKQNsNCZjGjfd%252BYUQ378xLVTh9yU6I4eSG%252BJXLTaMCuSVVVuH83Lhc%253D%26rv%3D0; SUP=cv%3D1%26bt%3D1411199883%26et%3D1411286283%26d%3Dc909%26i%3Dfb71%26us%3D1%26vf%3D0%26vt%3D0%26ac%3D43%26st%3D0%26uid%3D1775848881%26name%3Duzumakitenye%2540qq.com%26nick%3Duzumakitenye%26fmp%3D%26lcp%3D2012-02-28%252019%253A59%253A05; SUB=_2AkMjQby8a8NlrAJWn_0dyGPqZIxH-jyQljBKAn7uJhIyHRh-7kkOqSUiIej6MKf6CGeKO8iwyYfaEv9htg..; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WFo7CmyreaLfxW7bBGNAGrm5JpX5K2t; ALF=1442735883; SSOLoginState=1411199883; un=uzumakitenye@qq.com; wvr=5")
                .upload(new File("/home/choco/images/bg/tumblr_mm2qkzi6e21snii83o1_500.jpg")));
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
