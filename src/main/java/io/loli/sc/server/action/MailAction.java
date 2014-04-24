package io.loli.sc.server.action;

import io.loli.sc.server.service.MailService;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Named
@RequestMapping(value = "mail")
public class MailAction {

    @Inject
    private MailService mailService;

    @RequestMapping(value = "send", method = RequestMethod.POST)
    @ResponseBody
    public String sendEmail(@RequestParam("email") String email) {
        mailService.save(email);
        return "success";
    }

    @RequestMapping(value = "verify", method = RequestMethod.POST)
    @ResponseBody
    public String verify(@RequestParam("token") String token) {
        return String.valueOf(mailService.verify(token));
    }

}
