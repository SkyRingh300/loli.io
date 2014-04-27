package io.loli.sc.server.service;

import io.loli.sc.server.dao.BucketDao;
import io.loli.sc.server.entity.StorageBucket;
import io.loli.sc.server.storage.AliStorageUploader;
import io.loli.sc.server.storage.StorageUploader;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class BucketService {
    private static List<StorageBucket> bucketList;
    private static StorageBucket[] bucketArray;
    
    @Inject
    public BucketService(BucketDao bucketDao) {
        if (bucketList == null) {
            bucketList = bucketDao.list();
        }
        if (bucketArray == null) {
            bucketArray = bucketList.toArray(new StorageBucket[bucketList
                    .size()]);
        }
    }

    public List<StorageBucket> list() {
        return bucketList;
    }

    public StorageBucket[] listWithArray() {
        return bucketArray;
    }

    public StorageBucket randomBucket() {
        double d = Math.random();
        int i = (int) (d * bucketArray.length);
        return bucketArray[i];
    }

}
