package io.loli.sc.server.action;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Named
@RequestMapping(value = { "" })
public class HomeAction {
    private static final Logger logger = Logger.getLogger(HomeAction.class);

    @RequestMapping(value = { "" })
    public String index(HttpServletRequest request, @RequestParam(value = "weibo", required = false) String weibo,
        HttpServletResponse response) {
        // HTTPS enabled
        // if ("http".equals(request.getScheme())) {
        // return "redirect:https://" + request.getServerName();
        // }
        return "index";
    }

    @RequestMapping(value = { "/comment" })
    public String comment() {
        return "comment";
    }

    @RequestMapping(value = { "download" })
    public String download(HttpServletRequest request) {
        try {
            String userAgent = request.getHeader("user-agent");
            List<String> list = new ArrayList<>();
            String current = getOs(userAgent);
            list.add("Windows");
            list.add("Mac");
            list.add("Linux");
            list.remove(current);
            request.setAttribute("list", list);
            request.setAttribute("current", current);
        } catch (Exception e) {
            logger.error(e);
        }
        return "download";
    }

    @RequestMapping(value = { "about" })
    public String about(HttpServletRequest request) {
        return "about";
    }

    @RequestMapping(value = { "terms" })
    public String term(HttpServletRequest request) {
        return "terms";
    }

    private String getOs(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Mac")) {
            return "Mac";
        } else {
            return "";
        }
    }

}
