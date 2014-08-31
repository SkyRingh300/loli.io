package io.loli.sc.server.service;

import io.loli.util.string.ShortUrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.inject.Named;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

@Named
public class FileFetchService {
    private static CloseableHttpClient httpclient = HttpClients.createDefault();

    public File fetch(String path) {
        HttpGet get = new HttpGet(path);
        CloseableHttpResponse response = null;
        File file = null;
        try {
            response = httpclient.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                file = new File(System.getProperty("java.io.tmpdir") + File.separator
                    + ShortUrl.shortText(new Date().getTime() + path, 8));
                FileOutputStream outputStream = new FileOutputStream(file);

                InputStream inputStream = response.getEntity().getContent();

                byte buff[] = new byte[4096];
                int counts = 0;
                while ((counts = inputStream.read(buff)) != -1) {
                    outputStream.write(buff, 0, counts);
                }
                outputStream.flush();
                outputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
