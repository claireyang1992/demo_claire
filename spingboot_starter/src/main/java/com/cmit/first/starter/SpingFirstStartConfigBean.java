package com.cmit.first.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//有一种说法是如果在这里加上了这个注释，在配置类中可不需要@EnableConfigurationProperties({SpingFirstStartConfigBean.class})这个注释，但是在starter这里测试不成功，在spingbootAppplication测试成功了
@Component
@ConfigurationProperties(prefix = "spring.first.start")
public class SpingFirstStartConfigBean {
    private String param1;
    private String param2;

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

}
