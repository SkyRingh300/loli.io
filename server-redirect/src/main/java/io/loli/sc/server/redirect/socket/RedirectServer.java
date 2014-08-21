package io.loli.sc.server.redirect.socket;

import io.loli.sc.server.redirect.config.Config;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;

public class RedirectServer {
    private static HttpServer httpServer;
    private static final Logger logger = LogManager.getLogger(RedirectServer.class);

    public static void start() throws IOException {
        httpServer = new HttpServer();
        NetworkListener networkListener = new NetworkListener("redirect-listener", "127.0.0.1",
                Config.port);
        ThreadPoolConfig threadPoolConfig = ThreadPoolConfig.defaultConfig().setCorePoolSize(10)
                .setMaxPoolSize(100);
        networkListener.getTransport().setWorkerThreadPoolConfig(threadPoolConfig);
        httpServer.addListener(networkListener);
        RedirectHandler httpHandler = new RedirectHandler();
        httpServer.getServerConfiguration().addHttpHandler(httpHandler, new String[] { "/" });
        logger.info("服务器准备开始运行");
        httpServer.start();
        System.out.println("Press any key to stop the server...");
        System.in.read();
    }

    public static void stop() {
        if (httpServer != null && httpServer.isStarted()) {
            httpServer.shutdownNow();
            logger.info("服务器停止运行");
        }
    }

    public static void main(String[] args) throws IOException {
        RedirectServer.start();
    }
}
