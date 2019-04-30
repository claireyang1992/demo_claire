package com.cmit.codechain.busi.provider.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kapok.codegen")
public class ConfigBean {
    private String javaPath;

    public String getJavaPath() {
        return javaPath;
    }

    public void setJavaPath(String javaPath) {
        this.javaPath = javaPath;
    }

    public String getJavaRresources() {
        return javaRresources;
    }

    public void setJavaRresources(String javaRresources) {
        this.javaRresources = javaRresources;
    }

    private String javaRresources;


}
