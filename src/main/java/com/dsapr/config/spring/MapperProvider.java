package com.dsapr.config.spring;

import com.dsapr.mapper.BaseMapper;
import com.dsapr.provider.EntityClassFinder;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mapper 接口实例
 *
 * @param <T> 实体类类型
 * @param <I> 主键类型
 * @param <M> 实体对应的Mapper
 * @author dsapr
 * @date 2022/4/5
 */
public class MapperProvider<T, I, M extends BaseMapper<T, I>> implements ApplicationListener<ContextRefreshedEvent> {
    protected static ApplicationContext applicationContext;

    /**
     * 缓存默认注入的实例
     */
    protected Map<Class<?>, BaseMapper<T, I>> modelMapper = new ConcurrentHashMap<>();

    protected SqlSessionTemplate sqlSessionTemplate;

    public MapperProvider(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    /**
     * 获取默认实例
     */
    public static <T, I, M extends BaseMapper<T, I>> MapperProvider<T, I, M> getDefaultInstance() {
        return MapperProviderInstance.getINSTANCE();
    }

    /**
     * 获取指定实例
     */
    public static <T, I, M extends BaseMapper<T, I>> MapperProvider<T, I, M> getInstance(String instanceName) {
        return (MapperProvider<T, I, M>) applicationContext.getBean(instanceName);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        MapperProvider.applicationContext = event.getApplicationContext();
        this.sqlSessionTemplate.getConfiguration().getMapperRegistry().getMappers().forEach(mapper -> {
            addMapper(mapper, this.sqlSessionTemplate.getMapper(mapper));
        });
    }

    /**
     * 添加接口和实例
     * @param type   Mapper 接口类
     * @param mapper Mapper 实例
     */
    public void addMapper(Class<?> type, Object mapper) {
        if (type != null && BaseMapper.class.isAssignableFrom(type)) {
            EntityClassFinder.find(type, null).ifPresent(clazz -> {
                if (mapper != null) {
                    modelMapper.put(clazz, (BaseMapper<T, I>) mapper);
                }
            });
        }
    }

    /**
     * 获取实体类对应的 Mapper 接口
     *
     * @param modelClass 实体类
     * @return Mapper 接口
     */
    public M baseMapper(Class<T> modelClass) {
        if (modelMapper.containsKey(modelClass)) {
            return (M) modelMapper.get(modelClass);
        }
        throw new RuntimeException(modelClass.getName() + " Mapper interface not found");
    }

    /**
     * 将当前实例设置为默认实例
     */
    public void registerAsDefault() {
        MapperProviderInstance.setINSTANCE(this);
    }

    private static class MapperProviderInstance {
        public static MapperProvider INSTANCE;

        private static <T, I, M extends BaseMapper<T, I>> MapperProvider<T, I, M> getINSTANCE() {
            if (INSTANCE == null) {
                throw new NullPointerException("MapperProvider default instance not found");
            }
            return INSTANCE;
        }

        private static <T, I, M extends BaseMapper<T, I>> void setINSTANCE(MapperProvider<T, I, M> INSTANCE) {
            MapperProviderInstance.INSTANCE = INSTANCE;
        }
    }
}
