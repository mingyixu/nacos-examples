package com.alibaba.nacos.test.testcase.nacosconfig;

import javax.annotation.PostConstruct;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.test.testcase.BaseTestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NacosConfigListenerTest extends BaseTestCase {

    @NacosInjected
    private ConfigService configService;

    private String content = "";

    @NacosConfigListener(dataId = "com.alibaba.nacos.example.properties", timeout = 5000)
    public void onMessage(String config) {
        System.out.print("======recevice=====");
        content = config;
    }

    @Before
    public void before() {

    }

    @Test
    public void testConfigListener() throws NacosException {
        boolean result = configService.publishConfig("test4", "DEFAULT_GROUP", "66666");
        Assert.assertTrue(result);
        BaseTestCase.waitForSeconds(6);

        Assert.assertEquals("4444444", content);
    }
}
