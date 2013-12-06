package io.loli.sc.server.action;

import io.loli.sc.server.entity.User;
import io.loli.sc.server.service.UserService;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Named
@RequestMapping("/user/login")
public class UserLogin {
    @Inject
    private UserService userService;

    @RequestMapping(value = { "/regist" }, method = RequestMethod.GET)
    public String setUpReg(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/user/regist";
    }

    @RequestMapping(value = { "/regist" }, method = RequestMethod.POST)
    public String submitReg(@ModelAttribute User user, Model model,
            @RequestParam(required = true) String re_password) {
        if (!user.getPassword().equals(re_password)) {
            return null;
        }
        user.setRegDate(new Date());
        userService.save(user);
        return "redirect:/user/login";
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.GET)
    public String setUpLogin(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/user/login";
    }

    @RequestMapping(value = { "/login" }, method = RequestMethod.POST)
    public String submitLogin(@Valid @ModelAttribute("user") User user,
            Model model, HttpSession session) {
        User trueUser = userService.findByEmail(user.getEmail());
        if (user.getPassword().equals(trueUser.getPassword())) {
            session.setAttribute("user", trueUser);
            return "redirect:/user/home";
        } else {
            return "redirect:/user/login";
        }
    }

}
