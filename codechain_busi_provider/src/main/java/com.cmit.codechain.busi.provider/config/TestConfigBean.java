package com.cmit.codechain.busi.provider.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kapok.test")
@PropertySource("classpath:test.properties")//如果不是在application文件中，需要改配置指定路径
public class TestConfigBean {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
