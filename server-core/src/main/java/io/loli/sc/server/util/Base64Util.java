package io.loli.sc.server.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

    public static String encode(File file) {
        String str = "";
        try (FileInputStream is = new FileInputStream(file);) {
            str = Base64.encodeBase64String(inputStreamToByte(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将输入流转换成字节数组
     * 
     * @param is 需要转换的输入流
     * @return 转换后的字节数组
     * @throws IOException 当输入输出出现问题时抛出异常
     */
    public static byte[] inputStreamToByte(InputStream is) throws IOException {
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
