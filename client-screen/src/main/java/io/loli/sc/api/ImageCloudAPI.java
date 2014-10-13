package io.loli.sc.api;

import io.loli.sc.config.Config;
import io.loli.util.MD5Util;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageCloudAPI extends APITools implements API {

    private Config config;
    private String email;
    private String tokenStr;
    private Integer id;

    public ImageCloudAPI() {
    }

    public ImageCloudAPI(Config config) {
        this.config = config;
    }

    @Override
    public String upload(File fileToUpload) throws UploadException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost hp = new HttpPost(UPLOAD_URL);
        CloseableHttpResponse response = null;
        String result = null;

        String token = config.getImageCloudConfig().getToken();
        String email = config.getImageCloudConfig().getEmail();

        try {
            MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder.create();
            multiPartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            // 可以直接addBinary
            multiPartEntityBuilder.addPart("image",
                new FileBody(fileToUpload, ContentType.create("image/png", Consts.UTF_8), fileToUpload.getName()));
            multiPartEntityBuilder.setCharset(Consts.UTF_8);
            // 可以直接addText
            multiPartEntityBuilder.addPart("token",
                new StringBody(token, ContentType.create("text/plain", Consts.UTF_8)));
            multiPartEntityBuilder.addPart("email",
                new StringBody(email, ContentType.create("text/plain", Consts.UTF_8)));
            multiPartEntityBuilder.addPart("desc",
                new StringBody(fileToUpload.getName(), ContentType.create("text/plain", Consts.UTF_8)));

            hp.setEntity(multiPartEntityBuilder.build());
            response = httpclient.execute(hp);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new UploadException(e);
        }
        System.out.println(result);
        JSONObject obj = new JSONObject(result);
        return "http://r.loli.io/" + obj.getString("redirectCode");
    }

    private static final String BASE_URL = "http://loli.io/";
    private static final String TOKEN_URL = BASE_URL + "api/token";
    private static final String UPLOAD_URL = BASE_URL + "api/upload";

    @Override
    public void auth() throws UploadException {
        Map<String, String> authStr = this.login(null);
        String email = authStr.get("user");
        String passwd = authStr.get("pass");
        if (email == null || email.trim().equals("")) {
            return;
        }
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.addAll(Arrays.asList(new NameValuePair[] { new BasicNameValuePair("email", email),
            new BasicNameValuePair("password", MD5Util.hash(passwd)) }));
        String result = post(TOKEN_URL, params);

        JSONObject obj = new JSONObject(result);
        try {
            JSONObject user = obj.getJSONObject("user");
            this.id = obj.getInt("id");
            this.email = user.getString("email");
            this.tokenStr = obj.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "用户名密码错误");
        }
    }

    public Hashtable<String, String> login(JFrame frame) {
        Hashtable<String, String> logininformation = new Hashtable<String, String>();

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel label = new JPanel(new GridLayout(0, 1, 2, 2));
        label.add(new JLabel("E-Mail", SwingConstants.RIGHT));
        label.add(new JLabel("Password", SwingConstants.RIGHT));
        panel.add(label, BorderLayout.WEST);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField username = new JTextField(10);
        username.requestFocus();
        controls.add(username);
        JPasswordField password = new JPasswordField(10);
        controls.add(password);
        panel.add(controls, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(frame, panel, "login", JOptionPane.QUESTION_MESSAGE);
        logininformation.put("user", username.getText());
        logininformation.put("pass", new String(password.getPassword()));
        return logininformation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenStr() {
        return tokenStr;
    }

    public void setTokenStr(String tokenStr) {
        this.tokenStr = tokenStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
