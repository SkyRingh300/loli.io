package io.loli.sc.server.redirect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImageDao {
    private static final String SQL = "select internal_path from uploaded_image where redirect_code=?";
    private static final Logger logger = LogManager.getLogger(ImageDao.class);

    public String findUrlByCode(String code) {
        String url = "";
        try (Connection conn = ConnectionUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SQL);) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                url = rs.getString(1);
            }
            ConnectionUtil.close(conn, pstmt, rs);
        } catch (SQLException e) {
            logger.error("SQL执行错误:" + e);
        }
        logger.info("找到的URL为:" + url);
        return url;
    }
}
