package io.loli.sc.server.redirect.file;

import io.loli.storage.redirect.RedirectHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <p>
 * 字节数组实现的缓存类
 * <p>
 * 使用方法:
 * 
 * <pre>
 * 一个最多能缓存20个文件的类
 * Cache cache = new ByteCache(20);
 * 从缓存中获取该url所对应的文件，如果不存在会自动下载该文件
 * byte[] bytes = cache.getFile("http://1.loli.io/xxxx");
 * </pre>
 * 
 * @author choco (loli@linux.com)
 */
public class ByteCache implements Cache {
    private static final Logger logger = LogManager.getLogger(RedirectHandler.class);

    private Map<String, byte[]> dataMap = new TreeMap<>();
    /**
     * 最大文件数
     */
    private int maxNum = 10;

    /**
     * 非默认构造方法
     * 
     * @param maxNum 指定的最大文件数
     */
    public ByteCache(int maxNum) {
        this.maxNum = maxNum;
    }

    public ByteCache() {
    }

    {
        init();
    }

    /**
     * 初始化
     */
    private void init() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.loli.sc.server.redirect.file.Cache#saveFile(java.lang.String)
     */
    @Override
    public byte[] saveFile(String path) {
        byte[] bytes = null;
        try {
            InputStream is = get(path).getValue();
            bytes = inputStreamToByte(is);
            logger.info("将" + path + "写入缓存");
            dataMap.put(path, bytes);
            refreshCache();
        } catch (IOException e) {
            logger.error(e);
        }
        return bytes;
    }

    /*
     * (non-Javadoc)
     * 
     * @see io.loli.sc.server.redirect.file.Cache#getBytes(java.lang.String)
     */
    @Override
    public byte[] getBytes(String path) {
        // 当该文件不存在时，就调用saveFile方法下载
        if (!dataMap.containsKey(path)) {
            logger.info(path + "没有在缓存中找到");
            return this.saveFile(path);
        } else {
            logger.info("从缓存中找到" + path);
            return dataMap.get(path);
        }
    }

    /**
     * 刷新缓存,当缓存的文件大于10个时，将最旧的那个文件删除
     * 
     */
    private void refreshCache() {
        logger.info("当前缓存文件数:" + dataMap.size());
        // 当文件数大于10时，删除最早的文件
        if (dataMap.size() > maxNum) {
            String oldest = dataMap.keySet().iterator().next();
            dataMap.remove(oldest);
            logger.info("从缓存中删除" + oldest);
        }
    }

}
