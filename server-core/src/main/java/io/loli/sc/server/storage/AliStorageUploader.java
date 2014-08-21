package io.loli.sc.server.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;

public class AliStorageUploader extends StorageUploader {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;
    private String uploadUrl;

    public AliStorageUploader(String accessKeyId, String accessKeySecret,
            String endpoint, String uploadUrl, String bucketName) {
        super();
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
        this.uploadUrl = uploadUrl;
    }

    @Override
    public String upload(File file) {
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(uploadUrl.trim(), accessKeyId.trim(),
                accessKeySecret.trim());
        // 获取指定文件的输入流
        InputStream content = null;
        try {
            content = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();

        // 必须设置ContentLength
        meta.setContentLength(file.length());
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(file);
            
        } catch (IOException e) {
        }
        if (bi !=null) {
            meta.setContentType("image/png");
        }
        // 上传Object.
        String name = file.getName();
        client.putObject(bucketName, name, content, meta);
        return endpoint + "/" + name;
    }

    @Override
    public void delete(String file) {
        OSSClient client = new OSSClient(uploadUrl, accessKeyId,
                accessKeySecret);
        client.deleteObject(bucketName, file);
    }
}
