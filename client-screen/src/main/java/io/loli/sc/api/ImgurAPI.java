package io.loli.sc.api;

import io.loli.sc.config.Config;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

public class ImgurAPI extends APITools implements API {
    private static final String CLIENT_ID = "497cc6f6f81c581";
    private static final String CLIENT_SECRET = "4946a4382470ccee887599bc5bc506df3267f881";
    private static final String AUTH_URL_PIN = "https://api.imgur.com/oauth2/authorize?client_id="
            + CLIENT_ID + "&response_type=pin";
    private static final String PIN_TO_TOKEN_URL = "https://api.imgur.com/oauth2/token";
    private static final String UPLOAD = "https://api.imgur.com/3/upload";
    private static final String REFRESH_TOKEN_URL = "https://api.imgur.com/oauth2/token";
    private Config config;

    public void auth() throws UploadException {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(AUTH_URL_PIN));
        } catch (IOException | URISyntaxException e) {
            throw new UploadException(e);
        }
    }

    public String upload(File fileToUpload) throws UploadException {
        if (!fileToUpload.exists()) {
            JOptionPane.showMessageDialog(null, "图片文件不存在");
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        AccessToken token = null;
        // 如果token过期则重新获取
        Date old = config.getImgurConfig().getDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(old);
        cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
        Date now = new Date();
        if (cal.getTime().before(now)) {
            token = this
                    .refreshToken(config.getImgurConfig().getRefreshToken());
        } else {
            token = new AccessToken();
            token.setAccess_token(config.getImgurConfig().getAccessToken());
        }

        // post上传图片
        String imageString = postFile(UPLOAD, fileToUpload,
                token.getAccess_token());
        ImageInfo imageInfo = null;
        try {
            imageInfo = mapper.readValue(imageString, ImageInfo.class);
        } catch (IOException e) {
            throw new UploadException(e);
        }
        return imageInfo.getData().getLink();
    }

    /**
     * 通过pin码获取token
     * 
     * @param pin pin码
     * @return token对象
     * @throws UploadException
     */
    public AccessToken pinToToken(String pin) throws UploadException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.addAll(Arrays.asList(new NameValuePair[] {
                new BasicNameValuePair("client_id", CLIENT_ID),
                new BasicNameValuePair("client_secret", CLIENT_SECRET),
                new BasicNameValuePair("grant_type", "pin"),
                new BasicNameValuePair("pin", pin) }));
        String result = post(PIN_TO_TOKEN_URL, params);
        ObjectMapper mapper = new ObjectMapper();
        AccessToken token = null;
        try {
            token = mapper.readValue(result, AccessToken.class);
        } catch (IOException e) {
            throw new UploadException(e);
        }
        return token;
    }

    /*
     * post上传文件
     */
    private String postFile(String postUrl, File imgFileToUpload,
            String accessToken) throws UploadException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost hp = new HttpPost(postUrl);
        hp.addHeader("Authorization", "Bearer " + accessToken);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            MultipartEntityBuilder multiPartEntityBuilder = MultipartEntityBuilder
                    .create();
            multiPartEntityBuilder.addBinaryBody("image", imgFileToUpload);

            hp.setEntity(multiPartEntityBuilder.build());
            response = httpclient.execute(hp);
            result = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            throw new UploadException(e);
        }
        return result;
    }

    /**
     * token过期后刷新 access token
     * 
     * @param refreshToken 之前获取到的刷新用token
     * @return token对象
     * @throws UploadException
     */
    public AccessToken refreshToken(String refreshToken) throws UploadException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.addAll(Arrays.asList(new NameValuePair[] {
                new BasicNameValuePair("refresh_token", refreshToken),
                new BasicNameValuePair("client_id", CLIENT_ID),
                new BasicNameValuePair("client_secret", CLIENT_SECRET),
                new BasicNameValuePair("grant_type", "refresh_token") }));
        ObjectMapper mapper = new ObjectMapper();
        AccessToken token = null;
        try {
            token = mapper.readValue(post(REFRESH_TOKEN_URL, params),
                    AccessToken.class);
        } catch (IOException e) {
            throw new UploadException(e);
        }
        return token;

    }

    public ImgurAPI() {
    }

    public ImgurAPI(Config config) {
        this.config = config;
    }

    public static class AccessToken {
        @JsonProperty
        private String access_token;
        @JsonProperty
        private int expires_in;
        @JsonProperty
        private String token_type;
        @JsonProperty
        private String scope;
        @JsonProperty
        private String refresh_token;

        @JsonProperty
        private String account_username;

        public AccessToken() {
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getAccount_username() {
            return account_username;
        }

        public void setAccount_username(String account_username) {
            this.account_username = account_username;
        }

    }

    static class ImageInfo {
        private boolean success;
        private int status;
        private Data data;

        static class Data {
            private String id;
            private String title;
            private String description;
            private int datetime;
            private String type;
            private boolean animated;
            private int width;
            private int height;
            private int size;
            private int views;
            private int bandwidth;
            private boolean favorite;
            private String nsfw;
            private String section;
            private String deletehash;
            private String link;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public int getDatetime() {
                return datetime;
            }

            public void setDatetime(int datetime) {
                this.datetime = datetime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public boolean isAnimated() {
                return animated;
            }

            public void setAnimated(boolean animated) {
                this.animated = animated;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public int getViews() {
                return views;
            }

            public void setViews(int views) {
                this.views = views;
            }

            public int getBandwidth() {
                return bandwidth;
            }

            public void setBandwidth(int bandwidth) {
                this.bandwidth = bandwidth;
            }

            public boolean isFavorite() {
                return favorite;
            }

            public void setFavorite(boolean favorite) {
                this.favorite = favorite;
            }

            public String getNsfw() {
                return nsfw;
            }

            public void setNsfw(String nsfw) {
                this.nsfw = nsfw;
            }

            public String getSection() {
                return section;
            }

            public void setSection(String section) {
                this.section = section;
            }

            public String getDeletehash() {
                return deletehash;
            }

            public void setDeletehash(String deletehash) {
                this.deletehash = deletehash;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }
}
