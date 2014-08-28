package io.loli.sc.api;

import io.loli.sc.config.Config;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;

public class DropboxAPI extends APITools implements API {
    private static final String CLIENT_ID = "6r6ejqhp9b5lrbq";
    private static final String CLIENT_SECRET = "42fjwiakd5zpu0i";
    private static final String AUTH = "https://www.dropbox.com/1/oauth2/authorize?response_type=code&client_id="
            + CLIENT_ID;
    private static final String PIN_TO_TOKEN_URL = "https://api.dropbox.com/1/oauth2/token";

    private Config config;

    public void auth() throws UploadException {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(AUTH));
        } catch (IOException | URISyntaxException e) {
            throw new UploadException(e);
        }
    }

    public String upload(File fileToUpload) throws UploadException {
        DbxRequestConfig dconfig = new DbxRequestConfig("sc-java", Locale
                .getDefault().toString());
        DbxClient client = new DbxClient(dconfig, config.getDropboxConfig()
                .getAccessToken());

        DbxEntry.File uploadedFile = null;
        String shareLink = null;
        try (InputStream inputStream = new FileInputStream(fileToUpload);) {
            uploadedFile = client.uploadFile("/" + fileToUpload.getName(),
                    DbxWriteMode.add(), fileToUpload.length(), inputStream);
            shareLink = client.createShareableUrl(uploadedFile.path);

        } catch (DbxException | IOException e) {
            throw new UploadException(e);
        }
        return shareLink;
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
                new BasicNameValuePair("grant_type", "authorization_code"),
                new BasicNameValuePair("code", pin) }));
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

    public static class AccessToken {
        private String access_token;
        private String token_type;
        private String uid;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    public DropboxAPI() {
    }

    public DropboxAPI(Config config) {
        this.config = config;
    }

}