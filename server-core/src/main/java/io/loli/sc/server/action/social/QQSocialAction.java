package io.loli.sc.server.action.social;

import io.loli.sc.server.social.parent.AuthInfo;
import io.loli.sc.server.social.weibo.QQAuthManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Named
@RequestMapping(value = "social/qq")
public class QQSocialAction extends WeiboSocialAction {

    @Override
    public void init() {
        InputStream in = this.getClass().getResourceAsStream("/social.properties");
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String id = p.getProperty("qq_id");
        String secret = p.getProperty("qq_key");
        String callBack = p.getProperty("qq_call");
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(secret) && StringUtils.isNotBlank(callBack)) {
            manager = new QQAuthManager(new AuthInfo(id, secret, callBack));
        } else {
            logger.error("Init failed: properties not complete");
        }
    }
}
