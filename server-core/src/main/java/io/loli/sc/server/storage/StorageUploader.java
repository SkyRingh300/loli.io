package io.loli.sc.server.storage;

import io.loli.sc.server.entity.StorageBucket;

import java.io.File;

public abstract class StorageUploader {
    public abstract String upload(File file);

    public abstract void delete(String file);

    public static StorageUploader newInstance(StorageBucket bucket) {
        StorageUploader uploader = null;
        switch (bucket.getType()) {
        case StorageBucket.ALI_TYPE:
            uploader = new AliStorageUploader(bucket.getAccessKeyId(), bucket.getAccessKeySecret(),
                bucket.getEndPoint(), bucket.getUploadUrl(), bucket.getName());
            break;

        case StorageBucket.QN_TYPE:
            uploader = new QnStorageUploader(bucket.getAccessKeyId(), bucket.getAccessKeySecret(),
                bucket.getEndPoint(), bucket.getName());
            break;

        case StorageBucket.WEIBO_TYPE:
            uploader = new WeiboStorageUploader(bucket.getAccessKeyId());
            break;
        case StorageBucket.WEIBO_MOBILE_TYPE:
            uploader = WeiboMobileStorageUploader.getInstance(bucket.getAccessKeyId());
            break;
        }
        return uploader;
    }
}
