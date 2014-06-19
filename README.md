sc-server
=========

一个图床
支持使用阿里云、七牛云存储


使用maven命令打包
```
mvn clean package
```
* 数据库在```src/main/resources/db.properties```中配置
* 在运行后会自动创建表storage_bucket, 需要将云存储的信息插入表中, ```endpoint```是所绑定的域名，```access_key_id```和```access_key_secret```不用多说, ```upload_url```是文件上传的url, ```file_type```是文件类型, 用以区别图片文件和其他文件, ```enabled```是否开启, 1为开启, 0为关闭
