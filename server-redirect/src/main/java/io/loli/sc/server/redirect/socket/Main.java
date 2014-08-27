package io.loli.sc.server.redirect.socket;

import io.loli.sc.server.redirect.config.Config;
import io.loli.storage.redirect.RedirectServer;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static RedirectServer server;

    public static void start() throws IOException {
        
    }

    public static void main(String[] args) throws IOException {
        server = new RedirectServer().filter(new RedirectFilter()).port(Config.port);
        server.start();
    }

    public static void stop() {
        server.stop();
    }
}
