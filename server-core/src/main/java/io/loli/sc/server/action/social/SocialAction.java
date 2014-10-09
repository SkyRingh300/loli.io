package io.loli.sc.server.action.social;

import io.loli.sc.server.social.parent.AuthManager;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;

public abstract class SocialAction {
    protected AuthManager manager;
    protected Logger logger = Logger.getLogger(getClass());

    public abstract void init();

    public abstract void redirect(HttpServletResponse resp);

    public abstract String acceptCode(String code, HttpSession session, Model model);
}
