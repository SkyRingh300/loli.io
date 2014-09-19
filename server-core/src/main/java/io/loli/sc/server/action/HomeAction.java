package io.loli.sc.server.action;

import io.loli.sc.server.service.BucketService;
import io.loli.sc.server.storage.WeiboStorageUploader;
import io.loli.util.bean.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Named
@RequestMapping(value = { "" })
public class HomeAction {
    private static final Logger logger = Logger.getLogger(HomeAction.class);

    @RequestMapping(value = { "" })
    public String index(HttpServletRequest request, @RequestParam(value = "weibo", required = false) String weibo) {
        if (StringUtils.isNotBlank(weibo)) {
            Pair<Integer, Integer> res = new Pair<>(0, 0);
            try {
                BucketService.weiboList.stream().forEach(
                    obj -> {
                        Pair<Integer, Integer> result = ((WeiboStorageUploader) WeiboStorageUploader.newInstance(obj))
                            .getLimit();
                        res.setKey(res.getKey() + result.getKey());
                        res.setValue(res.getValue() + result.getValue());
                    });
            } catch (Exception e) {
                logger.error(e);
            }
            request.setAttribute("limit", res);
        }

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

    @RequestMapping(value = { "file" })
    public String fileUpload(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        List<String> list = new ArrayList<>();
        String current = getOs(userAgent);
        list.add("Windows");
        list.add("Mac");
        list.add("Linux");
        list.remove(current);
        request.setAttribute("list", list);
        request.setAttribute("current", current);
        return "image/fileUpload";
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
