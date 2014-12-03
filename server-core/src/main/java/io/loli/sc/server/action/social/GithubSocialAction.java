package io.loli.sc.server.action.social;

import io.loli.sc.server.entity.Social;
import io.loli.sc.server.social.parent.AuthInfo;
import io.loli.sc.server.social.weibo.GithubAuthManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Named
@RequestMapping(value = "social/github")
public class GithubSocialAction extends WeiboSocialAction {
    protected String type = Social.TYPE_GITHUB;

    @Override
    public void init() {
        InputStream in = this.getClass().getResourceAsStream("/social.properties");
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String id = p.getProperty("github_id");
        String secret = p.getProperty("github_key");
        String callBack = p.getProperty("github_call");
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(secret) && StringUtils.isNotBlank(callBack)) {
            manager = new GithubAuthManager(new AuthInfo(id, secret, callBack));
        } else {
            logger.error("Init failed: properties not complete");
        }
    }

    protected String getType() {
        return Social.TYPE_GITHUB;
    }

    @RequestMapping(value = "cancel")
    public String cancel(HttpSession session, RedirectAttributes redirectAttributes) {
        throw new UnsupportedOperationException("Github can not cancel");
    }
}
