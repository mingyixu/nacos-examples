package com.alibaba.nacos.test.testcase;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.example.spring.boot.NacosConfigApplication;

import org.apache.commons.lang3.SystemUtils;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={NacosConfigApplication.class})   // 指定启动类
public class BaseTestCase {
    static final public int WAIT_TIME = 3;
    static final public String DEFAULT_GROUP = "DEFAULT_GROUP";
    public String DATA_ID = "com.alibaba.nacos.example.properties";

    @NacosInjected
    private ConfigService configService;

    public void BaseTestCase() throws NacosException {
    }

    static public void waitForSeconds(long second) {
        try {
            Thread.sleep(second * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
