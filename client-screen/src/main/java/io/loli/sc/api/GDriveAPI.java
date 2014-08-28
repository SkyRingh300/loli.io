package io.loli.sc.api;

import io.loli.sc.config.Config;

import java.awt.Desktop;
import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

public class GDriveAPI extends APITools implements API {

    private final static String CLIENT_ID = "843116795212.apps.googleusercontent.com";

    private final static String CLIENT_SECRET = "7dtggnvbXOVsV0GV0N3FieXp";

    private final static String REDIRECT_URL = "urn:ietf:wg:oauth:2.0:oob";
    private final static String AUTH = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id="
            + CLIENT_ID
            + "&redirect_uri="
            + REDIRECT_URL
            + "&scope=https://www.googleapis.com/auth/drive.file%20https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile&approval_prompt=auto";

    private Config config;

    @Override
    public String upload(File fileToUpload) throws UploadException {
        com.google.api.services.drive.model.File f = null;
        try {
            Credential cre = GDriveUpload.tokenToCre(config.getGdriveConfig()
                    .getAccessToken(), config.getGdriveConfig()
                    .getRefreshToken());
            cre.refreshToken();
            Drive drive = GDriveUpload.getDrive(cre);
            String parent_id = GDriveUpload.getFolderId(drive);
            if (parent_id == null) {
                parent_id = GDriveUpload.createFolder("sc-java", cre);
            }
            f = GDriveUpload.uploadFile(fileToUpload, cre, parent_id);
            GDriveUpload.insertPermission(drive, f.getId(), "", "anyone",
                    "reader");
        } catch (HeadlessException | IOException e) {
            throw new UploadException(e);
        }
        try {
            f.setWebViewLink(f.getWebContentLink().substring(0,
                    f.getWebContentLink().indexOf("&")));
        } catch (Exception e) {
            throw new UploadException(e);
        }
        return f.getWebViewLink();
    }

    public GDriveAPI() {
    }

    public GDriveAPI(Config config) {
        this.config = config;
    }

    private String code;

    @Override
    public void auth() throws UploadException {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(AUTH));
        } catch (IOException | URISyntaxException e) {
            throw new UploadException(e);
        }
    }

    public AccessToken pinToToken(String pin) throws UploadException {
        Credential c = null;
        try {
            c = GDriveAuth.exchangeCode(pin);
        } catch (io.loli.sc.api.GDriveAPI.GDriveAuth.CodeExchangeException e) {
            throw new UploadException(e);
        }
        AccessToken token = new AccessToken();
        token.setAccess_token(c.getAccessToken());
        token.setExpires_in(c.getExpiresInSeconds());
        token.setRefresh_token(c.getRefreshToken());
        return token;
    }

    public String getCode() {
        return code;
    }

    public static class AccessToken {
        private String access_token;
        private long expires_in;
        private String refresh_token;

        public String getAccess_token() {
            return access_token;
        }

        public void setExpires_in(long expires_in) {
            this.expires_in = expires_in;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public long getExpires_in() {
            return expires_in;
        }
    }

    public static class NewAccessToken {
        private String Access_token;
        private int Expires_in;
        private String Token_type;

        public String getAccess_token() {
            return Access_token;
        }

        public void setAccess_token(String access_token) {
            Access_token = access_token;
        }

        public int getExpires_in() {
            return Expires_in;
        }

        public void setExpires_in(int expires_in) {
            Expires_in = expires_in;
        }

        public String getToken_type() {
            return Token_type;
        }

        public void setToken_type(String token_type) {
            Token_type = token_type;
        }

    }

    static class GDriveUpload {

        private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

        private static final JsonFactory JSON_FACTORY = new JacksonFactory();
        private static Drive drive = null;

        public static Drive getDrive(Credential credential) {
            drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName("Google-DriveSample/1.0").build();
            return drive;
        }

        /**
         * Insert a new permission.
         * 
         * @param service Drive API service instance.
         * @param fileId ID of the file to insert permission for.
         * @param value User or group e-mail address, domain name or
         *            {@code null} "default" type.
         * @param type The value "user", "group", "domain" or "default".
         * @param role The value "owner", "writer" or "reader".
         * @return The inserted permission if successful, {@code null}
         *         otherwise.
         * @throws UploadException 
         */
        public static Permission insertPermission(Drive service, String fileId,
                String value, String type, String role) throws UploadException {
            Permission newPermission = new Permission();

            newPermission.setValue(value);
            newPermission.setType(type);
            newPermission.setRole(role);
            try {
                return service.permissions().insert(fileId, newPermission)
                        .execute();
            } catch (IOException e) {
                throw new UploadException(e);
            }
        }

        public static com.google.api.services.drive.model.File uploadFile(
                java.io.File file, Credential credential, String parent)
                throws IOException {
            com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
            fileMetadata.setParents(Arrays.asList(new ParentReference()
                    .setId(parent)));
            fileMetadata.setTitle(file.getName());
            InputStreamContent mediaContent = new InputStreamContent(
                    "image/png", new BufferedInputStream(new FileInputStream(
                            file)));
            mediaContent.setLength(file.length());

            Drive.Files.Insert insert = drive.files().insert(fileMetadata,
                    mediaContent);
            MediaHttpUploader uploader = insert.getMediaHttpUploader();
            uploader.setDirectUploadEnabled(true);
            return insert.execute();
        }

        public static Credential tokenToCre(String accessToken,
                String refreshToken) {
            GoogleCredential credential = new GoogleCredential.Builder()
                    .setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT)
                    .setClientSecrets(CLIENT_ID, CLIENT_SECRET).build()
                    .setAccessToken(accessToken).setRefreshToken(refreshToken);
            return credential;
        }

        public static String createFolder(String name, Credential credential) throws UploadException {
            com.google.api.services.drive.model.File body = new com.google.api.services.drive.model.File();
            body.setTitle(name);
            body.setMimeType("application/vnd.google-apps.folder");
            Drive drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    credential).setApplicationName("Google-DriveSample/1.0")
                    .build();

            com.google.api.services.drive.model.File result = null;
            try {
                result = drive.files().insert(body).execute();
            } catch (IOException e) {
                throw new UploadException(e);
            }
            return result.getId();
        }

        public static String getFolderId(Drive service) throws IOException {
            List<com.google.api.services.drive.model.File> result = new ArrayList<com.google.api.services.drive.model.File>();
            Files.List request = service.files().list();
            String parent_id = null;
            do {
                try {
                    FileList files = request.execute();

                    result.addAll(files.getItems());
                    request.setPageToken(files.getNextPageToken());
                } catch (IOException e) {
                    System.out.println("An error occurred: " + e);
                    request.setPageToken(null);
                }
            } while (request.getPageToken() != null
                    && request.getPageToken().length() > 0);
            for (com.google.api.services.drive.model.File file : result) {
                if (file.getTitle().equals("sc-java")
                        && !file.getLabels().getTrashed()) {
                    parent_id = file.getId();
                    break;
                }
            }
            return parent_id;
        }

        public static Credential refreshToken(Credential cre)
                throws UploadException {
            try {
                cre.refreshToken();
            } catch (IOException e) {
                throw new UploadException(e);
            }

            return cre;
        }
    }

    static class GDriveAuth {

        private static String s = "{\"web\": {\"client_id\": \"843116795212.apps.googleusercontent.com\",\"client_secret\": \"7dtggnvbXOVsV0GV0N3FieXp\",\"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\"token_uri\": \"https://accounts.google.com/o/oauth2/token\"}}";

        private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
        private static final List<String> SCOPES = Arrays.asList(
                "https://www.googleapis.com/auth/drive.file",
                "https://www.googleapis.com/auth/userinfo.email",
                "https://www.googleapis.com/auth/userinfo.profile");

        private static GoogleAuthorizationCodeFlow flow = null;

        /**
         * Exception thrown when an error occurred while retrieving credentials.
         */
        public static class GetCredentialsException extends Exception {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;
            protected String authorizationUrl;

            /**
             * Construct a GetCredentialsException.
             * 
             * @param authorizationUrl The authorization URL to redirect the
             *            user to.
             */
            public GetCredentialsException(String authorizationUrl) {
                this.authorizationUrl = authorizationUrl;
            }

            /**
             * Set the authorization URL.
             */
            public void setAuthorizationUrl(String authorizationUrl) {
                this.authorizationUrl = authorizationUrl;
            }

            /**
             * @return the authorizationUrl
             */
            public String getAuthorizationUrl() {
                return authorizationUrl;
            }
        }

        /**
         * Exception thrown when a code exchange has failed.
         */
        public static class CodeExchangeException extends
                GetCredentialsException {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Construct a CodeExchangeException.
             * 
             * @param authorizationUrl The authorization URL to redirect the
             *            user to.
             */
            public CodeExchangeException(String authorizationUrl) {
                super(authorizationUrl);
            }

        }

        /**
         * Exception thrown when no refresh token has been found.
         */
        public static class NoRefreshTokenException extends
                GetCredentialsException {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            /**
             * Construct a NoRefreshTokenException.
             * 
             * @param authorizationUrl The authorization URL to redirect the
             *            user to.
             */
            public NoRefreshTokenException(String authorizationUrl) {
                super(authorizationUrl);
            }

        }

        /**
         * Exception thrown when no user ID could be retrieved.
         */
        private static class NoUserIdException extends Exception {

            /**
             * 
             */
            private static final long serialVersionUID = 1L;
        }

        /**
         * Retrieved stored credentials for the provided user ID.
         * 
         * @param userId User's ID.
         * @return Stored Credential if found, {@code null} otherwise.
         */
        static Credential getStoredCredentials(String userId) {
            // TODO: Implement this method to work with your database.
            // Instantiate a
            // new
            // Credential instance with stored accessToken and refreshToken.
            throw new UnsupportedOperationException();
        }

        /**
         * Store OAuth 2.0 credentials in the application's database.
         * 
         * @param userId User's ID.
         * @param credentials The OAuth 2.0 credentials to store.
         */
        static void storeCredentials(String userId, Credential credentials) {
            // TODO: Implement this method to work with your database.
            // Store the credentials.getAccessToken() and
            // credentials.getRefreshToken()
            // string values in your database.
            throw new UnsupportedOperationException();
        }

        /**
         * Build an authorization flow and store it as a static class attribute.
         * 
         * @return GoogleAuthorizationCodeFlow instance.
         * @throws IOException Unable to load client_secrets.json.
         */
        static GoogleAuthorizationCodeFlow getFlow() throws IOException {
            if (flow == null) {
                HttpTransport httpTransport = new NetHttpTransport();
                JacksonFactory jsonFactory = new JacksonFactory();
                GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                        jsonFactory, new BufferedReader(new StringReader(s)));
                flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
                        jsonFactory, clientSecrets, SCOPES)
                        .setAccessType("offline").setApprovalPrompt("force")
                        .build();
            }
            return flow;
        }

        /**
         * Exchange an authorization code for OAuth 2.0 credentials.
         * 
         * @param authorizationCode Authorization code to exchange for OAuth 2.0
         *            credentials.
         * @return OAuth 2.0 credentials.
         * @throws CodeExchangeException An error occurred.
         */
        static Credential exchangeCode(String authorizationCode)
                throws CodeExchangeException {
            try {
                GoogleAuthorizationCodeFlow flow = getFlow();
                GoogleTokenResponse response = flow
                        .newTokenRequest(authorizationCode)
                        .setRedirectUri(REDIRECT_URI).execute();
                return flow.createAndStoreCredential(response, null);
            } catch (IOException e) {
                System.err.println("An error occurred: " + e);
                throw new CodeExchangeException(null);
            }
        }

        /**
         * Send a request to the UserInfo API to retrieve the user's
         * information.
         * 
         * @param credentials OAuth 2.0 credentials to authorize the request.
         * @return User's information.
         * @throws NoUserIdException An error occurred.
         */
        static Userinfo getUserInfo(Credential credentials)
                throws NoUserIdException {
            Oauth2 userInfoService = new Oauth2.Builder(new NetHttpTransport(),
                    new JacksonFactory(), credentials).build();
            Userinfo userInfo = null;
            try {
                userInfo = userInfoService.userinfo().get().execute();
            } catch (IOException e) {
                System.err.println("An error occurred: " + e);
            }
            if (userInfo != null && userInfo.getId() != null) {
                return userInfo;
            } else {
                throw new NoUserIdException();
            }
        }

        /**
         * Retrieve the authorization URL.
         * 
         * @param emailAddress User's e-mail address.
         * @param state State for the authorization URL.
         * @return Authorization URL to redirect the user to.
         * @throws IOException Unable to load client_secrets.json.
         */
        public static String getAuthorizationUrl(String emailAddress,
                String state) throws IOException {
            GoogleAuthorizationCodeRequestUrl urlBuilder = getFlow()
                    .newAuthorizationUrl().setRedirectUri(REDIRECT_URI)
                    .setState(state);
            urlBuilder.set("user_id", emailAddress);
            return urlBuilder.build();
        }

        /**
         * Retrieve credentials using the provided authorization code.
         * 
         * This function exchanges the authorization code for an access token
         * and queries the UserInfo API to retrieve the user's e-mail address.
         * If a refresh token has been retrieved along with an access token, it
         * is stored in the application database using the user's e-mail address
         * as key. If no refresh token has been retrieved, the function checks
         * in the application database for one and returns it if found or throws
         * a NoRefreshTokenException with the authorization URL to redirect the
         * user to.
         * 
         * @param authorizationCode Authorization code to use to retrieve an
         *            access token.
         * @param state State to set to the authorization URL in case of error.
         * @return OAuth 2.0 credentials instance containing an access and
         *         refresh token.
         * @throws NoRefreshTokenException No refresh token could be retrieved
         *             from the available sources.
         * @throws IOException Unable to load client_secrets.json.
         */
        public static Credential getCredentials(String authorizationCode,
                String state) throws CodeExchangeException,
                NoRefreshTokenException, IOException {
            String emailAddress = "";
            try {
                Credential credentials = exchangeCode(authorizationCode);
                Userinfo userInfo = getUserInfo(credentials);
                String userId = userInfo.getId();
                emailAddress = userInfo.getEmail();
                if (credentials.getRefreshToken() != null) {
                    storeCredentials(userId, credentials);
                    return credentials;
                } else {
                    credentials = getStoredCredentials(userId);
                    if (credentials != null
                            && credentials.getRefreshToken() != null) {
                        return credentials;
                    }
                }
            } catch (CodeExchangeException e) {
                e.printStackTrace();
                // Drive apps should try to retrieve the user and credentials
                // for
                // the current
                // session.
                // If none is available, redirect the user to the authorization
                // URL.
                e.setAuthorizationUrl(getAuthorizationUrl(emailAddress, state));
                throw e;
            } catch (NoUserIdException e) {
                e.printStackTrace();
            }
            // No refresh token has been retrieved.
            String authorizationUrl = getAuthorizationUrl(emailAddress, state);
            throw new NoRefreshTokenException(authorizationUrl);
        }

    }
}