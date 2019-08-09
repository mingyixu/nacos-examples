package com.alibaba.nacos.test.testcase.nacosconfig;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.test.testcase.BaseTestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;

@NacosPropertySource(dataId = "mingyi.test", autoRefreshed = true)
public class NacosInjectedTest extends BaseTestCase {
    @NacosInjected
    private ConfigService configService;

    private String dataId;
    private String content;

    @Before
    public void before() {
        dataId = "mingyi.test";
        content = "test.yanlin";

    }

    @Test
    @Rollback(false)  //标明使用完此方法后事务不回滚,true时为回滚
    public void testPublishConfig() throws NacosException {
        boolean result = configService.publishConfig(dataId, DEFAULT_GROUP, content);
        Assert.assertTrue(result);

        BaseTestCase.waitForSeconds(WAIT_TIME);

        String ret = configService.getConfig(dataId, DEFAULT_GROUP, WAIT_TIME);
        Assert.assertEquals(ret, content);
    }

    @Test(timeout = WAIT_TIME*5000)
    public void testListenerConfig() throws NacosException {
        final AtomicInteger count = new AtomicInteger(0);
        final HashMap<String, String> configinfos = new HashMap<String, String>();
        final String key = "asy_rev";
        configService.addListener(dataId, DEFAULT_GROUP, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String config) {
                Assert.assertEquals(content, config); // asserts true
                System.out.println("=======");
                count.incrementAndGet();
                configinfos.put(key, config);
            }
        });

        BaseTestCase.waitForSeconds(WAIT_TIME);
        boolean result = configService.publishConfig(dataId, DEFAULT_GROUP, content);
        Assert.assertTrue(result);

        while (count.get() == 0) {
            BaseTestCase.waitForSeconds(1);
        }
        Assert.assertEquals(1, count.get());
        Assert.assertEquals(content, configinfos.get(key));
    }
}
