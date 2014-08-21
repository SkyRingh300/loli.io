package io.loli.sc.server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

public class FileUtils {

    public static String md5Hash(File file) {
        FileInputStream fis = null;
        String md5 = "";
        try {
            fis = new FileInputStream(file);
            md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis));
        } catch (IOException e) {
            throw new RuntimeException();
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return md5;
    }
}
