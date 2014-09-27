package io.loli.sc.server.storage;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import com.qiniu.api.auth.AuthException;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.PutPolicy;
import com.qiniu.api.rs.RSClient;

public class QnStorageUploader extends StorageUploader {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;

    public QnStorageUploader(String accessKeyId, String accessKeySecret, String endpoint, String bucketName) {
        super();
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }

    @Override
    public String upload(File file) {
        return this.upload(file, null);
    }

    @Override
    public String upload(File file, String contentType) {
        Mac mac = new Mac(accessKeyId, accessKeySecret);
        // 请确保该bucket已经存在
        PutPolicy putPolicy = new PutPolicy(bucketName);
        String uptoken = null;
        try {
            uptoken = putPolicy.token(mac);
        } catch (AuthException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        PutExtra extra = new PutExtra();
        if (StringUtils.isNotBlank(contentType)) {
            extra.mimeType = contentType;
        }

        String key = file.getName();
        PutRet ret = IoApi.putFile(uptoken, key, file.getPath(), extra);
        return endpoint + "/" + ret.getKey();
    }

    @Override
    public void delete(String file) {
        Mac mac = new Mac(accessKeyId, accessKeySecret);
        RSClient client = new RSClient(mac);
        client.delete(bucketName, file);
    }
}
