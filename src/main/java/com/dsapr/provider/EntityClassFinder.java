package com.dsapr.provider;

import com.dsapr.provider.util.ServiceLoaderUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 获取实体类
 *
 * @author dsapr
 * @date 2022/4/5
 */
public interface EntityClassFinder extends Order{
    static Optional<Class<?>> find(Class<?> mapperType, Method mapperMethod) {
        Objects.requireNonNull(mapperType);
        for (EntityClassFinder instance : EntityClassFinderInstance.getInstances()) {
            Optional<Class<?>> optionalClass = instance.findEntityClass(mapperType, mapperMethod);
            if (optionalClass.isPresent()) {
                return optionalClass;
            }
        }
        return Optional.empty();
    }

    /**
     * 查找方法对应的 实体类
     *
     * @param mapperType   Mapper 接口，不能为空
     * @param mapperMethod Mapper 接口方法，可以为空
     * @return
     */
    Optional<Class<?>> findEntityClass(Class<?> mapperType, Method mapperMethod);

    /**
     * 是否为定义的实体类型
     */
    boolean isEntityClass(Class<?> clazz);

    /**
     * 实例
     */
    class EntityClassFinderInstance {
        private static volatile List<EntityClassFinder> INSTANCES;

        public static List<EntityClassFinder> getInstances() {
            if (INSTANCES == null) {
                synchronized (EntityClassFinder.class) {
                    if (INSTANCES == null) {
                        //  SPI 获取实现
                        INSTANCES = ServiceLoaderUtils.getInstances(EntityClassFinder.class);
                    }
                }
            }
            return INSTANCES;
        }
    }
}
