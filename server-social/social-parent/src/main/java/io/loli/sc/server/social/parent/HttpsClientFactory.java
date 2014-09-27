package io.loli.sc.server.social.parent;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 获取一个https客户端实例
 * 
 * @author choco
 */
public class HttpsClientFactory {
    public static org.apache.http.client.HttpClient getInstance() {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("https", ssf).build();
            HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
            return HttpClients.custom().setConnectionManager(cm).build();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static org.apache.http.client.HttpClient getSimpleInstance() {
        CloseableHttpClient client = HttpClients.createDefault();
        return client;
    }
}