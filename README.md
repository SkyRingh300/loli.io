萝莉图床
=========

[![Build Status](https://drone.io/github.com/chocotan/sc-server/status.png)](https://drone.io/github.com/chocotan/sc-server/latest)

一个使用阿里云OSS/又拍云/七牛云作为存储的图床~还处于开发中
欢迎使用https://loli.io

> 如果在部署过程中遇到任何疑问，欢迎提交issue

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
* 在```server-core/src/main/resources/message.properties```中将redirectPath修改为UploadUrl，末尾要有"/"，比如我的UploadUrl是```http://1.loli.io/```，那么应该修改为```redirectPath=http://1.loli.io/```
* 如果你的流量吃紧，可以使用`server-redirect`模块，主要代码见`server-redirect`模块的`RedirectFilter.java`，它会将所有的图片缓存到本地，下次访问同样url的图片时就会从缓存中读取。`ImageRedirectServer.groovy`是程序主入口，如果不给端口参数，则会默认使用localhost的8888端口，所以如果部署在本机，上面的`redirectPath`就是`http://localhost:8888/`了。如果不需要缓存，那么无需运行这个。


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
  (1,'AccessKeyID','AccessKeyID','BucketName','ali','EndPoint','UploadUrl','1','image','InternalUrl');
  ```
  * 启动tomcat
  * 如果你的smtp配置不起作用，又想创建一个用户，可以使用如下SQL

  ```sql
  INSERT INTO `user` VALUES (1,'your@email.com','md5 of your password','2014-04-29 04:20:56');
  ```
  * 用户的密码是使用的MD5值，lab分支中是使用的`md5(密码+注册时间)`，本该有更安全的方法，可以自己实现，并将代码中登陆以及注册的地方的md5函数替换掉
  * 用户注册的验证码有效期是30分钟(session超时时间)，这里的注册流程和很多网站不一样；别人是`注册(成功)->发送激活邮件->点击激活链接`，而我这里是`注册(未成功)->发送激活邮件->输入激活的code->注册成功`，前者比较科学，在未来的版本中会考虑改掉
  
### 开发
* 本项目使用`Maven`管理依赖，直接导入装有maven插件的eclipse即可
* 如果导入后程序报错，请参考错误信息里的提示
