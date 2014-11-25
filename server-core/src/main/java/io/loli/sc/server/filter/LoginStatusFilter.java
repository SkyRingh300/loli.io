package io.loli.sc.server.filter;

import io.loli.sc.server.entity.LoginStatus;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.LoginStatusService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

@Named(value = "loginStatus")
public class LoginStatusFilter implements Filter {

    @Inject
    private LoginStatusService loginStatusService;

    private Logger logger = Logger.getLogger(LoginStatusFilter.class);

    public LoginStatusFilter() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (req.getServletPath().contains("api")) {
            chain.doFilter(request, response);
            return;
        }
        try {
            HttpSession session = req.getSession();
            Object obj = session.getAttribute("user");
            // 如果已经登录, 则无视
            if (obj != null) {
                User user = (User) obj;

                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    long count = Arrays.stream(req.getCookies()).filter(c -> c.getName().equals("token")).count();
                    if (count == 0) {
                        LoginStatus ls = loginStatusService.findByUId(user.getId());
                        if (ls == null) {
                            ls = loginStatusService.getLoginStatusByUId(user.getId());

                            ls.setLastLogin(new Date());
                            loginStatusService.update(ls);
                        }
                        Cookie cookie = new Cookie("token", ls.getToken());
                        cookie.setMaxAge(3600 * 24 * 365);
                        cookie.setPath("/");
                        res.addCookie(cookie);
                    }
                }

            } else {
                // 如果没有登录, 寻找此token是否对应某个用户，如果是，则将此用户添加到session中
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    Arrays.stream(req.getCookies()).filter(c -> c.getName().equals("token")).forEach(c -> {
                        User user = loginStatusService.findByToken(c.getValue());
                        if (user != null) {
                            session.setAttribute("user", user);
                            // 将该用户的登录时间保存进数据库中去
                        loginStatusService.updateDate(user);
                    }
                }   );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("登录状态过滤器发生异常:" + e);
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig fConfig) throws ServletException {
    }

}
