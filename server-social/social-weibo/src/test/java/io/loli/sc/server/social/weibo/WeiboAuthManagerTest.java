package io.loli.sc.server.social.weibo;

import io.loli.sc.server.social.parent.AuthInfo;
import io.loli.sc.server.social.parent.AuthManager;
import io.loli.sc.server.social.parent.UserInfo;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.junit.Test;

public class WeiboAuthManagerTest {

    @Test
    public void testGetAccessToken() throws URISyntaxException, IOException {
        AuthManager manager = new WeiboAuthManager(new AuthInfo("", "",
            "http://loli.io"));
        URI uri = new URI(manager.getAuthUrl());
        Desktop.getDesktop().browse(uri);
        String accessToken = manager.getAccessToken(new Scanner(System.in).nextLine()).getKey();
        UserInfo info = manager.getUserInfo(accessToken);
        System.out.println(info.getId());
        System.out.println(info.getUsername());
    }
}
