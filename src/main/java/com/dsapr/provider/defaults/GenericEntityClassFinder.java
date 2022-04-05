package com.dsapr.provider.defaults;

import com.dsapr.provider.EntityClassFinder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * 根据泛型，从返回值、参数、接口泛型参数来判断对应的实体类类型
 *
 * @author dsapr
 * @date 2022/4/5
 */
public abstract class GenericEntityClassFinder implements EntityClassFinder {
    @Override
    public Optional<Class<?>> findEntityClass(Class<?> mapperType, Method mapperMethod) {
        Optional<Class<?>> optionalClass;
        if (mapperMethod != null) {
            optionalClass = getEntityClassByMapperMethodReturnType(mapperType, mapperMethod);
            if (optionalClass.isPresent()) {
                return optionalClass;
            }
            optionalClass = getEntityClassByMapperMethodReturnType(mapperType, mapperMethod);
            if (optionalClass.isPresent()) {
                return optionalClass;
            }
        }
        optionalClass = getEntityClassByMapperMethodAndMapperType(mapperType, mapperMethod);
        if (optionalClass.isPresent()) {
            return optionalClass;
        }
        return getEntityClassByMapperType(mapperType);
    }

    /**
     * 根据方法返回值获取
     *
     * @param mapperType   接口
     * @param mapperMethod 方法
     * @return 实体类类型
     */
    protected Optional<Class<?>> getEntityClassByMapperMethodReturnType(Class<?> mapperType, Method mapperMethod) {
        Class<?> returnType = GenericTypeResolver.getReturnType(mapperMethod, mapperType);
        return isEntityClass(returnType) ? Optional.of(returnType) : Optional.empty();
    }

    /**
     * 根据方法获取参数
     *
     * @param mapperType   接口
     * @param mapperMethod 方法
     * @return 实体类类型
     */
    protected Optional<Class<?>> getEntityClassByMapperMethodParamTypes(Class<?> mapperType, Method mapperMethod) {
        return getEntityClassByTypes(GenericTypeResolver.resolveParamTypes(mapperMethod, mapperType));
    }

    /**
     * 根据方法所在接口泛型获取，只有定义在泛型接口中的方法才能通过泛型获取，定义的最终使用类中的无法通过泛型获取
     *
     * @param mapperType   接口
     * @param mapperMethod 方法
     * @return 实体类类型
     */
    protected Optional<Class<?>> getEntityClassByMapperMethodAndMapperType(Class<?> mapperType, Method mapperMethod) {
        return getEntityClassByTypes(GenericTypeResolver.resolveMapperTypes(mapperMethod, mapperType));
    }

    /**
     * 根据接口泛型获取，当前方法只根据接口泛型获取实体类，和当前执行的方法无关，是优先级最低的一种情况
     *
     * @param mapperType 接口
     * @return 实体类类型
     */
    protected Optional<Class<?>> getEntityClassByMapperType(Class<?> mapperType) {
        return getEntityClassByTypes(GenericTypeResolver.resolveMapperTypes(mapperType));
    }

    /**
     * 根据 type 获取可能的实例类型
     */
    protected Optional<Class<?>> getEntityClassByType(Type type) {
        if (type instanceof Class) {
            if (isEntityClass((Class<?>) type)) {
                return Optional.of((Class<?>) type);
            }
        } else if (type instanceof GenericTypeResolver.ParameterizedTypeImpl) {
            return getEntityClassByTypes(((GenericTypeResolver.ParameterizedTypeImpl) type).getActualTypeArguments());
        } else if (type instanceof GenericTypeResolver.WildcardTypeImpl) {
            Optional<Class<?>> optionalClass = getEntityClassByTypes(((GenericTypeResolver.WildcardTypeImpl) type).getLowerBounds());
            if (optionalClass.isPresent()) {
                return optionalClass;
            }
            return getEntityClassByTypes(((GenericTypeResolver.WildcardTypeImpl) type).getUpperBounds());
        } else if (type instanceof GenericTypeResolver.GenericArrayTypeImpl) {
            return getEntityClassByType(((GenericTypeResolver.GenericArrayTypeImpl) type).getGenericComponentType());
        }
        return Optional.empty();
    }

    /**
     * 根据 types 获取可能的实例类型
     */
    protected Optional<Class<?>> getEntityClassByTypes(Type[] types) {
        for (Type type : types) {
            Optional<Class<?>> optionalClass = getEntityClassByType(type);
            if (optionalClass.isPresent()) {
                return optionalClass;
            }
        }
        return Optional.empty();
    }
}
