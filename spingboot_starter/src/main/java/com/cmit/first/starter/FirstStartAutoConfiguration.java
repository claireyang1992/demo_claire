package com.cmit.first.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({SpingFirstStartConfigBean.class})
//当配置文件中spring.first.start.enable=true时进行自动配置，如果没有设置此值就默认使用matchIfMissing对应的值
@ConditionalOnProperty(prefix = "spring.first.start", value = "enable", matchIfMissing = true)//name = "enabled.firstStartAutoConfiguration",
//表示当classPath下存在FirstStartService.class文件时改配置文件类才有效
@ConditionalOnClass(FirstStartService.class)
public class FirstStartAutoConfiguration {

    @Bean
    // 当容器中没有指定Bean的情况下，自动配置FirstStartService类
    @ConditionalOnMissingBean(FirstStartService.class)
    public FirstStartService firstStartService(){
        return new FirstStartService();
    }
}
