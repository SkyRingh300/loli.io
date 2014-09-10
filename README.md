sc-server
=========

[![Build Status](https://drone.io/github.com/chocotan/sc-server/status.png)](https://drone.io/github.com/chocotan/sc-server/latest)

一个网盘/图床~还处于开发中
欢迎使用http://loli.io


### 运行环境
1. Java 1.8
2. Tomcat 7.0 或以上版本
3. MySQL 5.5 或以上版本

### 部署步骤

#### 编译打包

* 获取源代码
  ```
  git clone https://github.com/chocotan/sc-server
  ```

* 你需要先创建一个名为sc的数据库
数据库在```server-core/src/main/resources/db.properties```中设置

* 用户注册时会发送验证邮件，smtp在```server-core/src/main/resources/mail.properties```中设置

* 创建一个阿里云oss的bucket，并记录下相关信息: AccessKeyID, AccessKeyID, UploadUrl(上传地址, 可以绑定域名，也可以使用oss的外网域名，无效但必须写)，InternalUrl(上传地址)，EndPoint(同UploadUrl)，BucketName

* 在```server-core/src/main/resources/message.properties```中将redirectPath修改为UploadUrl，末尾要有"/"，比如我的UploadUrl是```http://test.com/```，那么应该修改为```redirectPath=http://test.com/```


* 打包 
  ```
  cd sc-server
  mvn clean package -Dmaven.test.skip
  ```

到```sc-server/server-core/target```中找到 ```server-core-x.y.z-XXXX.war```

#### 部署运行
  * 将上面的war丢到```TOMCAT_HOME/webapps/```中
  * 启动tomcat，会自动创建表
  * 关闭tomcat，将你的阿里云bucket信息插入storage_bucket中
  ```sql
  INSERT INTO `storage_bucket` VALUES 
  (1,'AccessKeyID','AccessKeyID','BucketName','ali','EndPoint','UploadUrl','1','image','InternalUrl'),;
  
  ```
  * 启动tomcat

  * 如果你的smtp配置不起作用，又想创建一个用户，可以使用如下SQL
  ```sql
  INSERT INTO `user` VALUES (1,'your@email.com','md5 of your passowrd','2014-04-29 04:20:56');
  ```
