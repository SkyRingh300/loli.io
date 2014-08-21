package io.loli.sc.server.redirect.file;

import io.loli.sc.server.redirect.socket.RedirectHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

/**
 * <p>
 * 使用内存文件系统实现的缓存类
 * <p>
 * 使用方法:
 * 
 * <pre>
 * 一个最多能缓存20个文件的类
 * Cache cache = new SimpleCache(20);
 * 从缓存中获取该url所对应的文件，如果不存在会自动下载该文件
 * Path path = cache.getFile("http://1.loli.io/xxxx");
 * 往缓存中以指定的文件名存储一个url文件
 * byte[] bytes = cache.saveFile("xxxxxx","http://1.loli.io/xxxx");
 * </pre>
 * 
 * @author choco (loli@linux.com)
 */
public class SimpleCache implements Cache {
    private FileSystem fs;
    private Path root;
    private static final Logger logger = LogManager.getLogger(RedirectHandler.class);
    /**
     * 最大文件数
     */
    private int maxNum = 10;

    /**
     * 非默认构造方法
     * 
     * @param maxNum 指定的最大文件数
     */
    public SimpleCache(int maxNum) {
        this.maxNum = maxNum;
    }

    public SimpleCache() {
    }

    {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        logger.info("初始化缓存");
        fs = Jimfs.newFileSystem(Configuration.unix());
        root = fs.getPath("/");
        try {
            if (!Files.exists(root))
                Files.createDirectory(root);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将某个path的文件保存到文件缓存中
     * 
     * @param path 需要保存的文件path
     * @return 文件数组
     */
    @Override
    public byte[] saveFile(String path) {
        // 生成文件名
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        Path filePath = root.resolve(fileName);
        byte[] bytes = null;
        try (InputStream is = get(path);) {
            bytes = inputStreamToByte(is);
            Files.write(filePath, bytes);
            logger.info("将" + path + "写入缓存");
            refreshCache();
        } catch (IOException e) {
            logger.error(e);
        }
        return bytes;
    }

    /**
     *
     * <p>
     * 从缓存中获取指定path的文件 并将其保存至缓存中 <br>
     * 仅当文件缓存中不存在这个文件时 才从远端获取
     * 
     * <p>
     * 文件名是根据path自动取得的<br>
     * 如下<br>
     * http://1.loli.io/xxx.png to xxx<br>
     * http://1.loli.io/xxxx to xxxx
     * 
     * @param path 需要获取的path
     * @return 如果存在 则返回该path 如果不存在 必须返回null
     */
    public Path getFile(String path) {
        // 生成文件名
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.indexOf("."));
        }
        Path p = root.resolve(fileName);

        // 当该文件不存在时，就调用saveFile方法下载
        if (!Files.exists(p)) {
            logger.info(path + "没有在缓存中找到");
            this.saveFile(path);
        } else {
            logger.info("从缓存中找到" + path);
        }
        return p;
    }

    /**
     *
     * <p>
     * 从缓存中获取指定path的文件 并将其保存至缓存中 <br>
     * 仅当文件缓存中不存在这个文件时 才从远端获取
     * 
     * <p>
     * 文件名是根据path自动取得的<br>
     * 如下<br>
     * http://1.loli.io/xxx.png to xxx<br>
     * http://1.loli.io/xxxx to xxxx
     * 
     * @param path 需要获取的path
     * @return 如果存在 则返回byte数组
     */
    @Override
    public byte[] getBytes(String path) {
        // 生成文件名
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        if (fileName.contains(".")) {
            fileName = fileName.substring(0, fileName.indexOf("."));
        }
        Path p = root.resolve(fileName);

        byte[] bytes = null;
        // 当该文件不存在时，就调用saveFile方法下载
        if (!Files.exists(p)) {
            logger.info(path + "没有在缓存中找到");
            bytes = this.saveFile(path);
        } else {
            logger.info("从缓存中找到" + path);
        }
        // 再从文件系统中读取一次，不直接使用上面的数组
        try (InputStream is = Files.newInputStream(p);) {
            bytes = inputStreamToByte(is);
        } catch (IOException e) {
            logger.error(e);
        }
        return bytes;
    }

    /**
     * 刷新缓存,当缓存的文件大于10个时，将最旧的那个文件删除
     * 
     * @throws IOException 当IO出现问题时抛出异常
     */
    private void refreshCache() throws IOException {
        // 获取根目录下的所有文件
        DirectoryStream<Path> stream = Files.newDirectoryStream(root,
                entry -> !Files.isDirectory(entry));
        List<Path> pathList = Lists.newArrayList(stream);
        // 对这些文件根据最后修改时间排序
        Collections.sort(
                pathList,
                (path1, path2) -> {
                    int result = 0;
                    try {
                        result = Files.getLastModifiedTime(path1).compareTo(
                                Files.getLastModifiedTime(path2));
                    } catch (Exception e) {
                        logger.error(e);
                        result = 0;
                    }
                    return result;
                });
        logger.info("当前缓存文件数:" + pathList.size());
        // 当文件数大于10时，删除最早的文件
        if (pathList.size() > maxNum) {
            Path pathToDelete = pathList.get(0);
            Files.delete(pathToDelete);
            logger.info("从缓存中删除" + pathToDelete.getName(0));
        }
    }

}
