package io.loli.sc.server.storage.ali;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.Bucket;
import com.aliyun.openservices.oss.model.OSSObjectSummary;
import com.aliyun.openservices.oss.model.ObjectListing;

public class Test {
    public static void main(String[] args) {
        String accessKeyId = "ehs1zdft7UUIV4jS";
        String accessKeySecret = "NhBQXwpRV1idI0c2hcsEWKlyMyklXi";

        // 初始化一个OSSClient
        OSSClient client = new OSSClient(accessKeyId, accessKeySecret);
        for(Bucket b:client.listBuckets()){
            System.out.println(b.getName());
        }
        
        // 获取指定bucket下的所有Object信息
        ObjectListing listing = client.listObjects("screen1");
        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            client.deleteObject("screen1", objectSummary.getKey());
        }
    }
}
