package com.bingo.test.component;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName ReflushProperties
 * @Description TODO
 * @Author h-bingo
 * @Date 2022-12-28 15:34
 * @Version 1.0
 */
@Component
// 配置中心动态刷新配置，配置中心有可不在本地项目配
// @RefreshScope
@ConfigurationProperties(prefix = "test")
public class ReflushProperties {

    // @Value("${test.name}")
    private String name;

    // @Value("${test.order}")
    private String order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
