package io.loli.sc.server.action;

import io.loli.sc.server.entity.User;
import io.loli.sc.server.exception.DBException;
import io.loli.sc.server.service.UserService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Named
@RequestMapping(value = { "/user" })
public class UserLogin {
    @Inject
    private UserService userService;

    /**
     * 用户注册GET, 定向至注册页面
     * 
     * @param model
     */
    @RequestMapping(value = { "/regist" }, method = RequestMethod.GET)
    public String setUpReg(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return REGINPUT;
    }

    /**
     * 注册的INPUT地址
     */
    private static String REGINPUT = "/user/regist";

    /**
     * 用户注册POST提交
     * 
     * @param user User对象
     * @param model
     * @param re_password 用户重复输入的密码, 应为md5值
     * @param password_md5 客户端js自动生成的密码md5值, 用以验证非法提交
     */
    @RequestMapping(value = { "/regist" }, method = RequestMethod.POST)
    public String submitReg(@ModelAttribute User user,
            @RequestParam("token") String token, Model model,
            @RequestParam(required = true) String password_re,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Map<String, String> msgMap = new HashMap<String, String>();
        request.setAttribute("message", msgMap);
        Object tokenInSession = request.getSession().getAttribute("token");
        if (null != tokenInSession && null != token
                && !token.equals(tokenInSession)) {
            msgMap.put("token", "验证码不正确");
            return REGINPUT;
        } else {
            request.getSession().removeAttribute("token");
        }

        // 没有md5加密
        if (user.getPassword().length() != 32
                || !user.getPassword().equals(password_re)) {
            msgMap.put("email", "非法请求");
            return REGINPUT;
        }

        // 用户注册日期
        user.setRegDate(new Date());
        try {
            userService.save(user);
        } catch (DBException e) {
            // 已经存在此邮箱，抛出异常
            msgMap.put("email", e.getMessage());
            return REGINPUT;
        }
        redirectAttributes.addFlashAttribute("info", "您已成功注册");
        return "redirect:/user/login";
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String setUpLogin(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/user/login";
    }

    /**
     * 用户登陆POST提交
     * 
     * @param user
     * @param model
     * @param session 自动注入的Session对象
     */
    @RequestMapping(value = { "/login" }, method = RequestMethod.POST)
    public String submitLogin(@ModelAttribute("user") User user, Model model,
            HttpSession session, HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        // 保存页面显示信息的map
        Map<String, String> msgMap = new HashMap<String, String>();
        request.setAttribute("message", msgMap);

        // 是否验证通过
        boolean flag = true;
        if (user.getEmail() == null || user.getEmail().trim().length() == 0) {
            flag = false;
        }
        if (user.getPassword() == null
                || user.getPassword().trim().length() == 0
                || user.getPassword().length() != 32) {
            flag = false;
        }
        // 当验证失败时，跳转回登陆界面
        if (!flag) {
            // 非法请求
            msgMap.put("email", "非法请求");
            return "/user/login";
        }

        // 根据此email查询出用户
        User trueUser = userService.findByEmail(user.getEmail());
        // 当查询出来的user不为空时
        if (trueUser != null
                && user.getPassword().equals(trueUser.getPassword())) {
            session.setAttribute("user", trueUser);
            redirectAttributes.addFlashAttribute("info", "登陆成功");
            return "redirect:/";
        } else {
            // 邮箱或者密码错误
            msgMap.put("email", "用户名或者密码错误");
            request.setAttribute("email", user.getEmail());
            return "/user/login";
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logOut(HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            request.getSession().removeAttribute("user");
            redirectAttributes.addFlashAttribute("info", "您已成功退出");
        } else {
            // TODO 用户未登录时的操作
        }
        return "redirect:/user/login";
    }

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String welcome() {

        return "user/welcome";
    }
}
