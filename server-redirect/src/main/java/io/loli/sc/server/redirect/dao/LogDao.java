package io.loli.sc.server.redirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogDao {
    private static final String SQL = "insert into redirect_log(url,ua,referer,ip,created_date) values(?,?,?,?,?)";
    private static final Logger logger = LogManager.getLogger(LogDao.class);

    public int save(String url, String ua, String referer, String ip, Date createdDate) {
        int result = 0;
        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SQL);) {
            pstmt.setString(1, url);
            pstmt.setString(2, ua);
            pstmt.setString(3, referer);
            pstmt.setString(4, ip);
            pstmt.setTimestamp(5, new Timestamp(createdDate.getTime()));
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("SQL执行出错:" + e);
        }
        return result;
    }

}
