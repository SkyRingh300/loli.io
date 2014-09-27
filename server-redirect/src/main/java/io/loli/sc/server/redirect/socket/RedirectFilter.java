package io.loli.sc.server.redirect.socket;

import io.loli.sc.server.redirect.config.Config;
import io.loli.sc.server.redirect.dao.ImageDao;
import io.loli.sc.server.redirect.dao.LogDao;
import io.loli.sc.server.redirect.file.ByteCache;
import io.loli.sc.server.redirect.file.Cache;
import io.loli.sc.server.server.redirect.bean.Pair;
import io.loli.storage.redirect.RedirectHandler;
import io.loli.storage.redirect.RequestAuthFilter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Header;
import org.glassfish.grizzly.http.util.HttpStatus;

public class RedirectFilter implements RequestAuthFilter {
    private static ImageDao imageDao = new ImageDao();
    private static LogDao logDao = new LogDao();
    private static final Cache cache = new ByteCache(Config.maxCount);

    private static final Logger logger = LogManager.getLogger(RedirectHandler.class);

    /*
     * 当配置为使用缓存且(未配置exclude或者url不包含exclude字符串)时，使用缓存
     */
    public void filter(final Request request, final Response response) {
        try {
            String code = request.getRequestURI();
            logger.info("用户请求的url为:" + code);
            if (code.startsWith("/")) {
                code = code.substring(1);
            }
            Pair<String, String> result = imageDao.findUrlByCode(code);
            String url = result.getKey();
            if (null == url || "".equals(url.trim())) {
                logger.warn("url为空");
                response.setStatus(HttpStatus.NOT_FOUND_404);
            } else {

                logger.info("找到url为:" + url);
                String referer = request.getHeader(Header.Referer);
                String ip = request.getRemoteAddr();
                if (ip != null && ip.equals("127.0.0.1")) {
                    ip = request.getHeader("X-Real-IP");
                }
                String ua = request.getHeader(Header.UserAgent);
                logDao.save(url, ua, referer, ip, new Date());
                InputStream input = null;

                try (OutputStream output = response.getOutputStream();) {
                    long total = 0;
                    if (Config.useCache) {
                        byte[] bytes = cache.getBytes(url);
                        total = bytes.length;
                        input = new BufferedInputStream(new ByteArrayInputStream(bytes));
                    } else {
                        Pair<Long, InputStream> pair = cache.get(url);
                        input = pair.getValue();
                        total = pair.getKey();
                    }

                    if ((!"".equals(result.getValue())) && null != result.getValue()) {
                        response.setContentType(result.getValue());
                    }

                    response.setHeader("Cache-Control", "max-age=15552000");
                    if (total != 0) {
                        response.setContentLengthLong(total);
                    }
                    byte[] buffer = new byte[2048];
                    for (int length = 0; (length = input.read(buffer)) > 0;) {
                        output.write(buffer, 0, length);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e);
                    response.sendRedirect(url);
                } finally {
                    if (input != null) {
                        input.close();
                    }
                }
            }

        } catch (Exception e) {
            response.setStatus(HttpStatus.NOT_FOUND_404);
            logger.error("系统内部错误500:" + e);
        }

    }
}
