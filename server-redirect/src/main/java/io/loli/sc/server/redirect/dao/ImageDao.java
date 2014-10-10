package io.loli.sc.server.redirect.dao;

import io.loli.sc.server.redirect.bean.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageDao {
    private static final String SQL = "select s.endpoint, u.redirect_code,u.content_type from uploaded_image as u,storage_bucket as s where u.redirect_code=? and u.del_flag=false and u.bucket_id=s.id";
    private static final Logger logger = LogManager.getLogger(ImageDao.class);

    public Pair<String, String> findUrlByCode(String code) {
        String url = "";
        String contentType = "";
        try (Connection conn = ConnectionUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(SQL);) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                url = rs.getString(1) + "/" + rs.getString(2);
                contentType = rs.getString(3);
            }
            ConnectionUtil.close(conn, pstmt, rs);
        } catch (SQLException e) {
            logger.error("SQL执行错误:" + e);
        }
        logger.info("找到的URL为:" + url);
        return new Pair<>(url, contentType);
    }
}
