package io.loli.sc.server.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.upyun.sdk.UpYunClient;
import com.upyun.sdk.exception.UpYunExcetion;
import com.upyun.sdk.vo.FileVo;

public class UpStorageUploader extends StorageUploader {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;

    public UpStorageUploader(String accessKeyId, String accessKeySecret, String endpoint, String bucketName) {
        super();
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }

    @Override
    public String upload(File file) {
        UpYunClient client = UpYunClient.newClient(bucketName, accessKeyId, accessKeySecret);
        try {
            client.uploadFile(file.getName(), new FileInputStream(file), Long.valueOf(file.length()).intValue());

            FileVo fileVo = client.listFileInfo(file.getName());
            return endpoint + "/" + fileVo.getName();
        } catch (UpYunExcetion | FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String upload(File file, String contentType) {
        return this.upload(file);
    }

    @Override
    public void delete(String file) {
        UpYunClient client = UpYunClient.newClient(bucketName, accessKeyId, accessKeySecret);
        try {
            client.deleteFile(file);
        } catch (UpYunExcetion e) {
            e.printStackTrace();
        }
    }

}
