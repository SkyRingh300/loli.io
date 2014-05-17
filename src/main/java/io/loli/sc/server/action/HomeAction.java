package io.loli.sc.server.action;

import javax.inject.Named;

import org.springframework.web.bind.annotation.RequestMapping;

@Named
@RequestMapping(value={"/"})
public class HomeAction {
    
    @RequestMapping(value={"/"})
    public String index(){
        return "index";
    }
}
