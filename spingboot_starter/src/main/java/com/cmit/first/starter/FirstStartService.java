package com.cmit.first.starter;

import org.springframework.beans.factory.annotation.Autowired;

public class FirstStartService {
    @Autowired
    SpingFirstStartConfigBean spingFirstStartConfig;

    public String getParam1(){
        return spingFirstStartConfig.getParam1();
    }

    public String getParam(){
        return spingFirstStartConfig.getParam1()+spingFirstStartConfig.getParam2();
    }
}
