package com.gulimall.order.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author aqiang9  2020-09-01
 */
@ConfigurationProperties(prefix = "gulimall.thread")
@Getter
@Setter
public class ThreadPoolConfigProperties {
    private Integer coreSize  = 5;
    private Integer maxSize = 200;
    private Integer keepAliveTime = 15 ;

    public Integer getCoreSize() {
        return coreSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }
}
