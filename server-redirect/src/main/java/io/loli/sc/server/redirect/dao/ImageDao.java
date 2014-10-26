package io.loli.sc.server.redirect.dao;

import io.loli.sc.server.redirect.bean.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.StringUtils;

public class ImageDao {
    private static final String SQL = "select s.endpoint, u.redirect_code,u.content_type from uploaded_image as u,storage_bucket as s where u.redirect_code=? and u.del_flag=false and u.bucket_id=s.id";
    private static final String SQL_LIKE = "select s.endpoint, u.redirect_code,u.content_type from uploaded_image as u,storage_bucket as s where u.redirect_code like ? and u.del_flag=false and u.bucket_id=s.id";
    private static final String SQUARE_SQL = "select s.endpoint, u.small_square_name,u.content_type from uploaded_image as u,storage_bucket as s where u.redirect_code=? and u.del_flag=false and u.bucket_id=s.id";

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

    public Pair<String, String> findUrlLikeCode(String code) {
        String url = "";
        String contentType = "";
        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQL_LIKE);) {
            pstmt.setString(1, code + "%");
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

    public String findSquarePathByCode(String origin) {
        String url = "";
        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(SQUARE_SQL);) {
            pstmt.setString(1, origin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString(2);
                if (StringUtils.isNullOrEmpty(name)) {
                    url = null;
                } else {
                    url = rs.getString(1) + "/" + rs.getString(2);
                }
            }
            ConnectionUtil.close(conn, pstmt, rs);
        } catch (SQLException e) {
            logger.error("SQL执行错误:" + e);
        }
        logger.info("找到的URL为:" + url);
        return url;
    }
}
