package com.alibaba.nacos.test.testcase.nacosconfig;

import javax.annotation.PostConstruct;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySources;
import com.alibaba.nacos.test.testcase.BaseTestCase;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME;

@NacosPropertySource(dataId = "com.alibaba.nacos.example.properties", autoRefreshed = true)
@NacosPropertySource(dataId = "com.alibaba.nacos.test.properties", autoRefreshed = true, before = SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME)
public class NacosPropertySource2Test extends BaseTestCase {

    @NacosValue("${user.namespace}")
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

        builder = new StringBuilder();
        builder.append("admin.id = 2");
        builder.append(SystemUtils.LINE_SEPARATOR);
        builder.append("admin.namespace = hexu.hx");
        builder.append(SystemUtils.LINE_SEPARATOR);
        builder.append("admin.github = https://github.com/nacos");
        configService.publishConfig(DATA_ID, DEFAULT_GROUP, builder.toString());
        configService.publishConfig("com.alibaba.nacos.test.properties", DEFAULT_GROUP, builder.toString());
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
