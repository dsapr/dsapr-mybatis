package com.dsapr.provider.defaults;

import com.dsapr.provider.annotation.Entity;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 默认实现
 *
 * @author dsapr
 * @date 2022/4/5
 */
public class DefaultEntityClassFinder extends GenericEntityClassFinder{
    @Override
    public Optional<Class<?>> findEntityClass(Class<?> mapperType, Method mapperMethod) {
        if (mapperMethod != null) {
            if (mapperMethod.isAnnotationPresent(Entity.class)) {
                Entity entity = mapperMethod.getAnnotation(Entity.class);
                return Optional.of(entity.value());
            }
        }

        if (mapperType.isAnnotationPresent(Entity.class)) {
            Entity entity = mapperType.getAnnotation(Entity.class);
            return Optional.of(entity.value());
        }

        // 未明确指明
        return super.findEntityClass(mapperType, mapperMethod);
    }

    @Override
    public boolean isEntityClass(Class<?> clazz) {
        return clazz.isAssignableFrom(null);
    }
}
