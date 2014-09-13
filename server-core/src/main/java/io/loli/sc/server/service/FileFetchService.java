package io.loli.sc.server.service;

import io.loli.util.string.MD5Util;
import io.loli.util.string.ShortUrl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

@Named
public class FileFetchService {
    private static final Logger logger = Logger.getLogger(FileFetchService.class);
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    @Inject
    private UploadedImageService uic;

    public File fetch(String path) {
        HttpGet get = new HttpGet(path);
        CloseableHttpResponse response = null;
        File file = null;
        try {
            response = httpclient.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                file = null;
                // 获取图片扩展名，jpg,png
                String fileName = System.getProperty("java.io.tmpdir") + File.separator
                    + getFileName(ShortUrl.shortText(new Date().getTime() + path + System.nanoTime()))
                    + getSuffix(path);
                file = new File(fileName);
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
            logger.error(e);
        }
        return file;
    }

    private String getSuffix(String path) {
        if (path.endsWith(".png") || path.endsWith(".PNG")) {
            return ".png";
        }
        if (path.endsWith(".jpeg") || path.endsWith(".JPEG")) {
            return ".jpg";
        }
        if (path.endsWith(".jpg") || path.endsWith(".jpg")) {
            return ".jpg";
        }
        if (path.endsWith(".bmp") || path.endsWith(".BMP")) {
            return ".bmp";
        }
        if (path.endsWith(".gif") || path.endsWith(".GIF")) {
            return ".gif";
        }
        return "";

    }

    private String getFileName(String[] urls) {
        String str = null;

        try {
            for (String url : urls) {
                if (!uic.checkExists(url)) {
                    return url;
                }
            }

            str = MD5Util.hash(String.valueOf(System.nanoTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

}
