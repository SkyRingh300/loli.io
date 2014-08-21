package io.loli.sc.server.redirect;

import io.loli.sc.server.redirect.socket.Main;
import io.loli.storage.redirect.RedirectServer;

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
                    Main.start();
                } catch (Exception e) {
                }
            }
        }.start();
    }

    @Test
    public void testStartStop() throws IOException, InterruptedException {
        Thread.sleep(2000);
        Main.stop();
    }
}
