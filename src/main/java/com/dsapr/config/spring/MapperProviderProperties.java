package com.dsapr.config.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Mapper 提供者配置
 *
 * @author dsapr
 * @date 2022/4/5
 */
@ConfigurationProperties(MapperProviderProperties.PREFIX)
public class MapperProviderProperties {
    public static final String PREFIX = "dsaprmybatis.framework.activerecord";

    /**
     * 是否启动自动注入 MapperProvider
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
