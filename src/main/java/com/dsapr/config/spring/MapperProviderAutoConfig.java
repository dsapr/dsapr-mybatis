package com.dsapr.config.spring;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置 mapperProvider
 *
 * @author dsapr
 * @date 2022/4/4
 */
@Configuration
@ConditionalOnProperty(prefix = MapperProviderProperties.PREFIX, name = "enabled", matchIfMissing = true)
@EnableConfigurationProperties(MapperProviderProperties.class)
public class MapperProviderAutoConfig {
    /**
     * 当前没有自定义的 Mapper 提供者时，提供一个默认的
     *
     * 当使用多数据源时，需要通过 {@link org.springframework.context.annotation.Primary} 注解指定主要的 {@link SqlSessionTemplate}
     */
    @Bean
    @ConditionalOnMissingBean
    public MapperProvider springMapperRegistry(SqlSessionTemplate sqlSessionTemplate) {
        return new MapperProvider(sqlSessionTemplate);
    }

    /**
     * 自动注册为默认的 Mapper 提供者
     *
     * 当使用多数据源时，需要通过 {@link org.springframework.context.annotation.Primary} 注解指定主要的默认的 {@link MapperProvider}
     */
    @Configuration
    public static class AutoRegisterConfiguration implements InitializingBean {
        private final MapperProvider mapperProvider;

        public AutoRegisterConfiguration(MapperProvider mapperProvider) {
            this.mapperProvider = mapperProvider;
        }

        @Override
        public void afterPropertiesSet() {
            mapperProvider.registerAsDefault();
        }
    }
}
