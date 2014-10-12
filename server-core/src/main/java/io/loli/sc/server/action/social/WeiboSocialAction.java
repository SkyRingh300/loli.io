package io.loli.sc.server.action.social;

import io.loli.sc.server.entity.Social;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.social.SocialService;
import io.loli.sc.server.social.parent.AuthInfo;
import io.loli.sc.server.social.parent.UserInfo;
import io.loli.sc.server.social.weibo.WeiboAuthManager;
import io.loli.util.bean.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Named
@RequestMapping(value = "social/weibo")
public class WeiboSocialAction extends SocialAction {
    @Inject
    private SocialService ss;

    protected String type = Social.TYPE_WEIBO;

    public WeiboSocialAction() {
        // init
        init();
    }

    @Override
    @RequestMapping(value = "redirect")
    public void redirect(HttpServletResponse response) {
        if (manager == null) {
            init();
        }
        String url = manager.getAuthUrl();
        if (StringUtils.isNotBlank(url)) {
            try {
                response.sendRedirect(url);
            } catch (IOException e) {
                logger.error("Failed to redirect:" + url + ", " + e);
            }
        }
    }

    @Override
    @RequestMapping(value = "acceptCode")
    public String acceptCode(@RequestParam(value = "code", required = false) String code, HttpSession session,
        Model model, RedirectAttributes redirectAttributes) {
        if (code == null) {
            model.addAttribute("info", "登录失败");
            return "/user/login";
        }

        if (session.getAttribute("user") == null) {
            Pair<String, Long> token = manager.getAccessToken(code);
            UserInfo info = manager.getUserInfo(token.getKey());

            ss.save(null, info.getId(), token.getKey(), info.getUsername(), type, token.getValue());
            Social social = ss.findByUserIdAndType(info.getId(), type);
            if (social != null) {
                session.setAttribute("user", social.getUser());
            }
            return "index";

        } else {
            Pair<String, Long> token = manager.getAccessToken(code);
            UserInfo info = manager.getUserInfo(token.getKey());

            ss.save((User) session.getAttribute("user"), info.getId(), token.getKey(), info.getUsername(), type,
                token.getValue());
            Social social = ss.findByUserIdAndType(info.getId(), type);
            if (social != null) {
                session.setAttribute("user", social.getUser());
            }
            redirectAttributes.addFlashAttribute("message", "绑定成功");
            return "redirect:/user/edit";
        }

    }

    @Override
    public void init() {
        InputStream in = this.getClass().getResourceAsStream("/social.properties");
        Properties p = new Properties();
        try {
            p.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String id = p.getProperty("weibo_id");
        String secret = p.getProperty("weibo_key");
        String callBack = p.getProperty("weibo_call");
        if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(secret) && StringUtils.isNotBlank(callBack)) {
            manager = new WeiboAuthManager(new AuthInfo(id, secret, callBack));
        } else {
            logger.error("Init failed: properties not complete");
        }
    }
}
