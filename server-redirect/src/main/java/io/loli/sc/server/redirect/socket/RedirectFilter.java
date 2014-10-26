package io.loli.sc.server.redirect.socket;

import io.loli.sc.server.redirect.bean.Pair;
import io.loli.sc.server.redirect.config.Config;
import io.loli.sc.server.redirect.dao.ImageDao;
import io.loli.sc.server.redirect.dao.LogDao;
import io.loli.sc.server.redirect.file.Cache;
import io.loli.sc.server.redirect.file.FileCache;
import io.loli.storage.redirect.RedirectHandler;
import io.loli.storage.redirect.RequestAuthFilter;
import io.loli.util.concurrent.TaskExecutor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.util.Header;
import org.glassfish.grizzly.http.util.HttpStatus;

import com.mysql.jdbc.StringUtils;

public class RedirectFilter implements RequestAuthFilter {
    private static ImageDao imageDao = new ImageDao();
    private static LogDao logDao = new LogDao();
    private static final Cache cache = new FileCache();

    private static final Logger logger = LogManager.getLogger(RedirectHandler.class);

    private static final TaskExecutor executor = new TaskExecutor(30);

    private static Set<String> blackList = new LinkedHashSet<String>();

    {
        try (InputStream is = this.getClass().getResourceAsStream("/blacklist.txt");) {
            @SuppressWarnings("resource")
            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                if (str != null && !"".equals(str.trim())) {
                    blackList.add(str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
            e.printStackTrace();
        }
    }

    private void send404(Response response) {
        response.setStatus(HttpStatus.NOT_FOUND_404);

        response.setContentType("image/png");

        try (InputStream is = this.getClass().getResourceAsStream("/404.png");
            OutputStream os = response.getOutputStream();) {
            byte[] b = new byte[1024];
            while (is.read(b) >= 0) {
                os.write(b);
            }

        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void sendOutput(String url, String contentType, Request req, Response resp) {
        InputStream input = null;

        try {
            OutputStream output = resp.getOutputStream();
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

            if ((!"".equals(contentType)) && null != contentType) {
                resp.setContentType(contentType);
            }

            resp.setHeader("Cache-Control", "max-age=15552000");
            if (total != 0) {
                resp.setContentLengthLong(total);
            }
            byte[] buffer = new byte[2048];
            for (int length = 0; (length = input.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            send404(resp);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * 当配置为使用缓存且(未配置exclude或者url不包含exclude字符串)时，使用缓存
     */
    public void filter(final Request request, final Response response) {

        String referer = request.getHeader(Header.Referer);

        // Do with black list
        if (referer != null && !referer.trim().equals("") && !blackList.isEmpty()) {

            for (String site : blackList) {
                if (referer.startsWith(site)) {
                    logger.warn("该网址被禁止访问, referer是" + referer);
                    send403(response);
                    return;
                }
            }
        }

        try {
            String code = request.getRequestURI();
            logger.info("用户请求的url为:" + code);
            if (code.startsWith("/")) {
                code = code.substring(1);
            }

            // 缩略图
            if (code.contains(".")) {
                String name = code.substring(0, code.indexOf("."));
                if (name.length() == 7) {
                    String format = code.substring(code.indexOf(".") + 1);
                    if (name.endsWith("q")) {
                        String origin = name.substring(0, 6) + "." + format;
                        Pair<String, String> result = imageDao.findUrlByCode(origin);
                        try {
                            String squarePath = imageDao.findSquarePathByCode(origin);
                            System.out.println(squarePath);
                            if (StringUtils.isNullOrEmpty(squarePath)) {
                                sendOutput(result.getKey(), result.getValue(), request, response);
                                return;
                            }
                            String contentType = result.getValue();
                            sendOutput(squarePath, contentType, request, response);
                            return;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

            Pair<String, String> result = imageDao.findUrlByCode(code);
            if (StringUtils.isNullOrEmpty(result.getKey()) && code.length() == 6) {
                result = imageDao.findUrlLikeCode(code);
            }
            String url = result.getKey();
            if (null == url || "".equals(url.trim())) {
                logger.warn("url为空");
                send404(response);
            } else {

                logger.info("找到url为:" + url);

                String ip = request.getRemoteAddr();
                if (ip != null && ip.equals("127.0.0.1")) {
                    ip = request.getHeader("X-Real-IP");
                }
                final String fip = ip;
                String ua = request.getHeader(Header.UserAgent);
                executor.execute(() -> logDao.save(url, ua, referer, fip, new Date()));
                sendOutput(url, result.getValue(), request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            send404(response);
        }

    }

    private void send403(Response response) {
        response.setStatus(HttpStatus.FORBIDDEN_403);

        response.setContentType("image/png");

        try (InputStream is = this.getClass().getResourceAsStream("/403.png");
            OutputStream os = response.getOutputStream();) {
            byte[] b = new byte[1024];
            while (is.read(b) >= 0) {
                os.write(b);
            }

        } catch (IOException e) {
            logger.error(e);
        }
    }
}
