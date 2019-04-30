package com.cmit.codechain.busi.provider.controller;

import com.cmit.codechain.busi.provider.config.ConfigBean;
import com.cmit.codechain.busi.provider.config.TestConfigBean;
import com.cmit.first.starter.FirstStartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    ConfigBean configBean;

    @Autowired
    TestConfigBean testConfigBean;

    @Autowired
    FirstStartService firstStartService;

    @GetMapping("/value")
    public String getConfigValue(){
        return configBean.getJavaPath()+testConfigBean.getName();

    }


    @GetMapping("/fiststarter")
    public String fiststarter(){
        return firstStartService.getParam();

    }
}
