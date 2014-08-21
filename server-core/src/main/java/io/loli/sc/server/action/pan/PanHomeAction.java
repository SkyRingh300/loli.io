package io.loli.sc.server.action.pan;

import javax.inject.Named;

import org.springframework.web.bind.annotation.RequestMapping;

@Named
@RequestMapping("pan")
public class PanHomeAction {
    @RequestMapping(value = { "" })
    public String index() {
        return "/pan/index";
    }
}
