package io.loli.sc.server.redirect.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class ConnectionUtil {
    private static final Logger logger = LogManager.getLogger(ImageDao.class);
    private static DataSource datasource = new DataSource();
    private static String url;
    private static String driver;
    private static String username;
    private static String password;
    // load db properties;
    static {
        logger.info("准备加载数据库配置文件");
        Properties dbProps = null;
        dbProps = new Properties();
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(
                "db.properties");) {
            dbProps.load(is);
        } catch (IOException e) {
            logger.error("加载出现异常:" + e);
        }
        url = dbProps.getProperty("url");
        driver = dbProps.getProperty("driver");
        username = dbProps.getProperty("username");
        password = dbProps.getProperty("password");
        logger.info("加载完毕:" + url + ", " + driver + ", " + username + ", " + password);
    }

    static {
        logger.info("初始化数据库连接池");
        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName(driver);
        p.setUsername(username);
        p.setPassword(password);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(40);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        datasource.setPoolProperties(p);
        logger.info("数据库连接池加载完毕");
    }

    public static Connection getConnection() throws SQLException {
        Connection con = null;
        con = datasource.getConnection();
        return con;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        logger.error(e);
                    } finally {
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (SQLException e) {
                                logger.error(e);
                            }
                        }
                    }
                }
            }
        }
    }
}
