package io.loli.sc.server.redirect.config;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 配置类, 加载此类时自动读取配置，无需实例化即可调用属性
 * 
 * @author choco (loli@linux.com)
 */
public class Config {
    private static Logger logger = LogManager.getLogger(Config.class);
    // 是否使用缓存
    public static boolean useCache = true;
    // 服务器端口
    public static int port = 8888;
    // 最大缓存文件数
    public static int maxCount = 100;
    // 不缓存的文件
    public static String exclude = "screen2";
    static {
        Properties prop = null;
        prop = new Properties();
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("config.properties");) {
            prop.load(is);
        } catch (Exception e) {
            logger.error(e);
        }
        if (prop != null) {
            try {
                useCache = "1".equals(prop.getProperty("useCache")) ? true : false;
                port = Integer.parseInt(prop.getProperty("port"));
                maxCount = Integer.parseInt(prop.getProperty("maxCount"));
                if (!useCache) {
                    maxCount = 0;
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.info("配置加载完毕:useCache=" + useCache + ", port=" + port + " ,maxCount=" + maxCount);
    }
}
