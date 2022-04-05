package com.dsapr.provider;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 实体类工厂
 *
 * @author dsapr
 * @date 2022/4/5
 */
public class EntityFactory {
    public static EntityTable create(Class<?> mapperType, Method mapperMethod) {
        Optional<Class<?>> optionalClass = EntityClassFinder.find(mapperType, mapperMethod);
        if (optionalClass.isPresent()) {
            return create(optionalClass.get());
        }
        throw new RuntimeException("Can't obtain " + (mapperMethod != null ?
                mapperMethod.getName() + "method" : mapperType.getSimpleName() + "interface") + " corresponding entity class");
    }

    /**
     * 获取类型对应的实体信息
     *
     * @param entityClass 实体类类型
     * @return 实体类信息
     */
    public static EntityTable create(Class<?> entityClass) {
//        EntityTableFactory.Chain entityTableFactoryChain = Instange
        return null;
    }

    /**
     * 实例
     */
    static class Instance {
        private static volatile EntityTableFactory.Chain entityTableFactoryChain;
        private static volatile EntityColumnFactory.Chain entityColumnFactoryChain;
    }
}
