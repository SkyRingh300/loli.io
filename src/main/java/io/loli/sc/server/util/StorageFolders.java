package io.loli.sc.server.util;

import io.loli.aliyun.oss.StorageFolder;
import io.loli.sc.server.entity.StorageBucket;
import io.loli.sc.server.service.BucketService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import com.aliyun.openservices.oss.OSSClient;

@Named
@Singleton
public class StorageFolders {
    private static StorageFolder root = null;

    @Inject
    public BucketService bucketService;

    public StorageFolder getRootFolder() {
        if (root == null) {
            StorageBucket bucket = bucketService.singleFileBucket();
            OSSClient client = new OSSClient(bucket.getUploadUrl(), bucket.getAccessKeyId(),
                    bucket.getAccessKeySecret());
            root = StorageFolder.getRootFolder(client, bucket.getName());
        }
        return root;
    }
}
