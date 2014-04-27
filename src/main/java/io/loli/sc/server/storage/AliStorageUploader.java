package io.loli.sc.server.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;

public class AliStorageUploader extends StorageUploader {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;
    
    public AliStorageUploader(String accessKeyId, String accessKeySecret,
            String endpoint,String bucketName) {
        super();
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }


    @Override
    public String upload(File file) {
        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
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
        meta.setContentType("image/png");
        // 上传Object.
        String name = file.getName().substring(0, file.getName().indexOf("."))
                + ".png";
        client.putObject(bucketName, name, content, meta);
        return endpoint + "/" + name;
    }
}
