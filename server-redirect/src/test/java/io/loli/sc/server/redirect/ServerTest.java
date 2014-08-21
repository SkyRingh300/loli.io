package io.loli.sc.server.redirect;

import io.loli.sc.server.redirect.socket.RedirectServer;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class ServerTest {

    @BeforeClass
    public static void beforeClass() throws IOException {
        new Thread() {
            @Override
            public void run() {
                try {
                    RedirectServer.start();
                } catch (Exception e) {
                }
            }
        }.start();
    }

    @Test
    public void testStartStop() throws IOException, InterruptedException {
        Thread.sleep(2000);
        RedirectServer.stop();
    }
}
