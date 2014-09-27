package io.loli.sc.server.social.weibo;

import io.loli.sc.server.social.parent.AuthInfo;
import io.loli.sc.server.social.parent.AuthManager;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.junit.Test;

public class WeiboAuthManagerTest {

    @Test
    public void testGetAccessToken() throws URISyntaxException, IOException {
        AuthManager manager = new WeiboAuthManager(new AuthInfo("3616446582", "165f979663b6c178033a52ef0305a817",
            "http://loli.io"));
        URI uri = new URI(manager.getAuthUrl());
        Desktop.getDesktop().browse(uri);
        System.out.println(manager.getAccessToken(new Scanner(System.in).nextLine()));

    }
}
