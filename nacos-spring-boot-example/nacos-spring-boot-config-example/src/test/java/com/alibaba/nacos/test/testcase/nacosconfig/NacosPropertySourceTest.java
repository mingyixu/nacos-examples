package com.alibaba.nacos.test.testcase.nacosconfig;

import javax.annotation.PostConstruct;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosProperty;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.test.testcase.BaseTestCase;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@NacosPropertySource(dataId = "com.alibaba.nacos.example.properties", autoRefreshed = true)
public class NacosPropertySourceTest extends BaseTestCase {

    @Value("${user.namespace}")
    private String name;

    @Value("${user.github}")
    private String github;

    @NacosInjected
    private ConfigService configService;

    @PostConstruct
    public void init() throws  NacosException {
        StringBuilder builder = new StringBuilder();
        builder.append("user.id = 1");
        builder.append(SystemUtils.LINE_SEPARATOR);
        builder.append("user.namespace = mingyi");
        builder.append(SystemUtils.LINE_SEPARATOR);
        builder.append("user.github = https://github.com/nacos");
        configService.publishConfig(DATA_ID, DEFAULT_GROUP, builder.toString());
    }

    @Before
    public void before() throws NacosException {
    }

    @Test
    public void testDoubleGetConfig() throws NacosException {
        Assert.assertEquals("mingyi", name);
    }

    @Test
    public void testDoubleGetConfig2() throws NacosException {
        Assert.assertEquals("https://github.com/nacos", github);
    }
}
