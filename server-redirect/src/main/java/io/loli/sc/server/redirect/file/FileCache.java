package io.loli.sc.server.redirect.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author choco (loli@linux.com)
 */
public class FileCache implements Cache {
    private Path root;
    private static final Logger logger = LogManager.getLogger(FileCache.class);

    public FileCache() {
    }

    {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        logger.info("初始化缓存");
        root = new File(System.getProperty("java.io.tmpdir")).toPath();
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
        try (InputStream is = get(path).getValue();) {
            bytes = inputStreamToByte(is);
            Files.write(filePath, bytes);
            logger.info("将" + path + "写入文件系统");
            // refreshCache();
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

    @Override
    public byte[] getBytes(String path) {
        // 生成文件名
        String fileName = path.substring(path.lastIndexOf("/") + 1);
       
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

}
