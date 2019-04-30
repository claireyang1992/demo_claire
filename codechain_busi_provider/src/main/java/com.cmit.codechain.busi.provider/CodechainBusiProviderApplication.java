package com.cmit.codechain.busi.provider;

import com.cmit.codechain.busi.provider.config.ConfigBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
//@EnableConfigurationProperties({ConfigBean.class})
public class CodechainBusiProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CodechainBusiProviderApplication.class, args);
    }

}
