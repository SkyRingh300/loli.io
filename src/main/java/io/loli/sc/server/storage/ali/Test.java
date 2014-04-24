package io.loli.sc.server.storage.ali;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;

public class Test {
    public static void main(String[] args) {
        String accessKeyId = "ehs1zdft7UUIV4jS";
        String accessKeySecret = "NhBQXwpRV1idI0c2hcsEWKlyMyklXi";
        String endpoint = "http://screen1.oss.aliyuncs.com";

        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        // 获取指定文件的输入流
        File file = new File("/home/choco/.SC-JAVA/2014-04-14 01:19的截图.png");
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

        // 上传Object.
        PutObjectResult result = client.putObject("screen1",
                String.valueOf(new Date().getTime()), content, meta);

        // 打印ETag
        System.out.println(result.getETag());
    }
}
