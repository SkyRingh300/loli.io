package io.loli.sc.server.service;

import io.loli.sc.server.dao.BucketDao;
import io.loli.sc.server.entity.StorageBucket;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class BucketService {
    private static List<StorageBucket> bucketList;
    private static StorageBucket[] bucketArray;

    private static StorageBucket[] imageArray;
    private static StorageBucket[] fileArray;

    @Inject
    public BucketService(BucketDao bucketDao) {
        if (bucketList == null) {
            bucketList = bucketDao.list();
        }
        if (bucketArray == null) {
            bucketArray = bucketList.toArray(new StorageBucket[bucketList
                    .size()]);
        }
        if (imageArray == null) {
            Stream<StorageBucket> str = bucketList.stream().filter(
                    item -> item.getFileType().equals(StorageBucket.IMG_TYPE));
            fileArray = str.collect(Collectors.toList()).toArray(
                    new StorageBucket[(int) str.count()]);
        }
        if (fileArray == null) {
            Stream<StorageBucket> str = bucketList.stream().filter(
                    item -> item.getFileType().equals(StorageBucket.FILE_TYPE));
            fileArray = str.collect(Collectors.toList()).toArray(
                    new StorageBucket[(int) str.count()]);
        }

    }

    public List<StorageBucket> list() {
        return bucketList;
    }

    public StorageBucket[] listWithArray() {
        return bucketArray;
    }

    public StorageBucket randomImageBucket() {
        double d = Math.random();
        int i = (int) (d * imageArray.length);
        return imageArray[i];
    }

    public StorageBucket randomFileBucket() {
        double d = Math.random();
        int i = (int) (d * fileArray.length);
        return fileArray[i];
    }

    public StorageBucket randomBucket() {
        double d = Math.random();
        int i = (int) (d * bucketArray.length);
        return bucketArray[i];
    }

}
