package io.loli.sc.server.util;

import io.loli.aliyun.oss.StorageException;
import io.loli.aliyun.oss.StorageFile;
import io.loli.aliyun.oss.StorageFolder;
import io.loli.sc.server.entity.StorageBucket;
import io.loli.sc.server.service.BucketService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

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

    public StorageFolder getDayFolder() {
        LocalDateTime now = LocalDateTime.now();

        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        try {
            return this.getRootFolder().getSubFolder(String.valueOf(year)).getSubFolder(String.valueOf(month))
                .getSubFolder(String.valueOf(day));
        } catch (StorageException e) {
            return this.getRootFolder();
        }
    }

    public StorageFile uploadFile(Path path) {
        try {
            return this.getDayFolder().createSubFile(path);
        } catch (StorageException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public StorageFile uploadFile(File file) {
        try {
            return this.getDayFolder().createSubFile(file.toPath());
        } catch (StorageException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
