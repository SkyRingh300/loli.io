package io.loli.sc.server.service;

import io.loli.sc.server.dao.BucketDao;
import io.loli.sc.server.entity.StorageBucket;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named
@Singleton
public class BucketService {
    private List<StorageBucket> bucketList;
    private StorageBucket[] bucketArray;

    private StorageBucket[] imageArray;
    private StorageBucket[] fileArray;

    @Inject
    public BucketService(BucketDao bucketDao) {
        if (bucketList == null) {
            bucketList = bucketDao.list();
        }
        if (bucketArray == null) {
            bucketArray = bucketList.toArray(new StorageBucket[bucketList.size()]);
        }
        if (imageArray == null) {
            List<StorageBucket> list = bucketList.stream()
                .filter(item -> item.getFileType().equals(StorageBucket.IMG_TYPE)).collect(Collectors.toList());
            imageArray = list.toArray(new StorageBucket[list.size()]);
        }
        if (fileArray == null) {
            List<StorageBucket> list = bucketList.stream()
                .filter(item -> item.getFileType().equals(StorageBucket.FILE_TYPE)).collect(Collectors.toList());
            fileArray = list.toArray(new StorageBucket[list.size()]);
        }

    }

    public void refresh() {

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

    public StorageBucket singleFileBucket() {
        return fileArray[0];
    }

    public StorageBucket randomBucket() {
        double d = Math.random();
        int i = (int) (d * bucketArray.length);
        return bucketArray[i];
    }

}
