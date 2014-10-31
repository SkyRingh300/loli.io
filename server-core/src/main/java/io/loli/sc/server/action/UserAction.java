package io.loli.sc.server.action;

import io.loli.sc.server.entity.Social;
import io.loli.sc.server.entity.User;
import io.loli.sc.server.exception.DBException;
import io.loli.sc.server.service.UserService;
import io.loli.sc.server.service.social.SocialService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Named
@RequestMapping(value = { "/user" })
public class UserAction {
    @Inject
    private UserService userService;

    @Inject
    private SocialService socialService;

    /**
     * 注册的INPUT地址
     */
    private static final String REG_INPUT = "/user/regist";

    private static final String LOGIN_INPUT = "/user/login";

    private static final String MSG_NAME = "message";

    private static final String TOKEN_NAME = "token";

    private static final String EMAIL_NAME = "email";

    private static final Logger logger = Logger.getLogger(UserAction.class);

    /**
     * 用户注册GET, 定向至注册页面
     * 
     * @param model
     */
    @RequestMapping(value = { "/regist" }, method = RequestMethod.GET)
    public String setUpReg(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return REG_INPUT;
    }

    /**
     * 用户注册POST提交
     * 
     * @param user User对象
     * @param model
     * @param re_password 用户重复输入的密码, 应为md5值
     * @param password_md5 客户端js自动生成的密码md5值, 用以验证非法提交
     */
    @RequestMapping(value = { "/regist" }, method = RequestMethod.POST)
    public String submitReg(@ModelAttribute User user, @RequestParam(TOKEN_NAME) String token, Model model,
        @RequestParam(required = true, value = "password_re") String passwordRe, HttpServletRequest request,
        RedirectAttributes redirectAttributes) {
        Map<String, String> msgMap = new HashMap<String, String>();
        request.setAttribute(MSG_NAME, msgMap);
        Object tokenInSession = request.getSession().getAttribute(TOKEN_NAME);
        if (null != tokenInSession && null != token && !token.equals(tokenInSession)) {
            msgMap.put(TOKEN_NAME, "验证码不正确");
            return REG_INPUT;
        } else {
            request.getSession().removeAttribute(TOKEN_NAME);
        }

        // 没有md5加密
        if (user.getPassword().length() != 32 || !user.getPassword().equals(passwordRe)) {
            msgMap.put(EMAIL_NAME, "非法请求");
            return REG_INPUT;
        }

        // 用户注册日期
        user.setRegDate(new Date());
        try {
            userService.save(user);
        } catch (DBException e) {
            logger.info("已经存在此邮箱" + e);
            // 已经存在此邮箱，抛出异常
            msgMap.put(EMAIL_NAME, e.getMessage());
            return REG_INPUT;
        }
        redirectAttributes.addFlashAttribute("info", "您已成功注册");
        return "redirect:" + LOGIN_INPUT;
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String setUpLogin(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return LOGIN_INPUT;
    }

    /**
     * 用户登录POST提交
     * 
     * @param user
     * @param model
     * @param session 自动注入的Session对象
     */
    @RequestMapping(value = { "/login" }, method = RequestMethod.POST)
    public String submitLogin(@ModelAttribute("user") User user, Model model, HttpSession session,
        HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        // 保存页面显示信息的map
        Map<String, String> msgMap = new HashMap<String, String>();
        request.setAttribute(MSG_NAME, msgMap);

        // 是否验证通过
        boolean flag = true;
        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            flag = false;
        }
        if (user.getPassword() == null || user.getPassword().trim().length() == 0 || user.getPassword().length() != 32) {
            flag = false;
        }
        // 当验证失败时，跳转回登录界面
        if (!flag) {
            // 非法请求
            msgMap.put(EMAIL_NAME, "非法请求");
            return LOGIN_INPUT;
        }

        // 根据此email查询出用户
        User trueUser = userService.findByEmail(user.getEmail());
        // 当查询出来的user不为空时
        if (trueUser != null && user.getPassword().equals(trueUser.getPassword())) {
            session.setAttribute("user", trueUser);
            redirectAttributes.addFlashAttribute("info", "登录成功");
            return "redirect:/";
        } else {
            // 邮箱或者密码错误
            msgMap.put(EMAIL_NAME, "用户名或者密码错误");
            request.setAttribute(EMAIL_NAME, user.getEmail());
            return LOGIN_INPUT;
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.getSession().removeAttribute("user");
            redirectAttributes.addFlashAttribute("info", "您已成功退出");
            Cookie cookie = new Cookie(TOKEN_NAME, "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            // TODO 用户未登录时的操作
        }
        return "redirect:" + LOGIN_INPUT;
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome() {

        return "user/welcome";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String changePwdForm(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("info", "非法请求");
            return "redirect:" + LOGIN_INPUT;
        }
        model.addAttribute("user", user);
        User u = (User) user;
        List<Social> socials = socialService.listByUserId(u.getId());
        socials.forEach(social -> {
            for (String type : Social.TYPES) {
                if (social.getType().equals(type)) {
                    model.addAttribute(type, social);
                }
            }
        });
        return "user/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String changePwdSubmit(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes,
        @RequestParam(required = true, value = "password_re") String passwordRe,
        @RequestParam(required = true, value = "password_old") String passwordOld) {
        Object user = request.getSession().getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("info", "非法请求");
            return "redirect:" + LOGIN_INPUT;
        }

        if (((User) user).getPassword().equals(passwordOld)) {
            ((User) user).setPassword(passwordRe);
            userService.update((User) user);
            redirectAttributes.addFlashAttribute(MSG_NAME, "更新密码成功");
            return "redirect:edit";
        } else {
            redirectAttributes.addFlashAttribute(MSG_NAME, "原密码错误");
            return "redirect:edit";
        }
    }

    @RequestMapping(value = "/updateNickname", method = RequestMethod.POST)
    @ResponseBody
    public String updateNickname(HttpSession session, @RequestParam(value = "nickName") String nickName) {
        User user = (User) session.getAttribute("user");
        userService.updateNickname(user, nickName);

        return "success";
    }
}
