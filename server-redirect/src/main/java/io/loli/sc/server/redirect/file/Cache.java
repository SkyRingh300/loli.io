package io.loli.sc.server.redirect.file;

import io.loli.sc.server.redirect.bean.Pair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public interface Cache {

    /**
     * 将某个path的文件保存到文件缓存中
     * 
     * @param path 需要保存的文件path
     * @return 字节数组，如果捕获到异常，则返回空
     */
    public abstract byte[] saveFile(String path);

    /**
     *
     * <p>
     * 从缓存中获取指定path的文件 并将其保存至缓存中 <br>
     * 仅当文件缓存中不存在这个文件时 才从远端获取
     * 
     * <p>
     * 文件名是根据path自动取得的<br>
     * 如下<br>
     * http://1.loli.io/xxx.png to xxx<br>
     * http://1.loli.io/xxxx to xxxx
     * 
     * @param path 需要获取的path
     * @return 如果存在 则返回该path的字节，如果不存在，从远端获取
     */
    public abstract byte[] getBytes(String path);

    /**
     * 获取指定的url的输入流
     * 
     * @param urlString url
     * @return 此url的输入流
     * @throws IOException 当IO出现问题时抛出异常
     */
    default public Pair<Long, InputStream> get(String urlString) throws IOException {
        if (urlString.equalsIgnoreCase("")) {
            return null;
        } else if (urlString.toLowerCase().startsWith("http://")) {
        } else {
            return null;
        }
        HttpURLConnection httpConnection;
        URL url = new URL(urlString);
        httpConnection = (HttpURLConnection) url.openConnection();
        httpConnection.setRequestMethod("GET");
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);
        int code = httpConnection.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            return new Pair<Long, InputStream>(httpConnection.getContentLengthLong(), httpConnection.getInputStream());
        } else {
            return null;
        }
    }

    /**
     * 将输入流转换成字节数组
     * 
     * @param is 需要转换的输入流
     * @return 转换后的字节数组
     * @throws IOException 当输入输出出现问题时抛出异常
     */
    default public byte[] inputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte data[] = bytestream.toByteArray();
        bytestream.close();
        return data;
    }

}