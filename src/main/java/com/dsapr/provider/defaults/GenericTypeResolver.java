package com.dsapr.provider.defaults;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.cursor.Cursor;

import java.lang.reflect.*;
import java.util.*;

/**
 * 源码来自 https://github.com/mybatis/mybatis-3，在此基础上仅添加了一个 resolveMapperTypes 方法
 *
 * @author dsapr
 * @date 2022/4/5
 */
public class GenericTypeResolver {
    public GenericTypeResolver() {
        super();
    }

    public static Class<?> getReturnType(Method method, Class<?> srcType) {
        Class<?> returnType = method.getReturnType();
        Type resolvedReturnType = resolveReturnType(method, srcType);
        if (resolvedReturnType instanceof Class) {
            returnType = (Class<?>) resolvedReturnType;
            if (returnType.isArray()) {
                returnType = returnType.getComponentType();
            }
            // gcode issue #508
            if (void.class.equals(returnType)) {
                ResultType rt = method.getAnnotation(ResultType.class);
                if (rt != null) {
                    returnType = rt.value();
                }
            }
        } else if (resolvedReturnType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) resolvedReturnType;
            Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            if (Collection.class.isAssignableFrom(rawType) || Cursor.class.isAssignableFrom(rawType)) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    Type returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class<?>) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        // (gcode issue #443) actual type can be a also a parameterized type
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    } else if (returnTypeParameter instanceof GenericArrayType) {
                        Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter).getGenericComponentType();
                        // (gcode issue #525) support List<byte[]>
                        returnType = Array.newInstance(componentType, 0).getClass();
                    }
                }
            } else if (method.isAnnotationPresent(MapKey.class) && Map.class.isAssignableFrom(rawType)) {
                // (gcode issue 504) Do not look into Maps if there is not MapKey annotation
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 2) {
                    Type returnTypeParameter = actualTypeArguments[1];
                    if (returnTypeParameter instanceof Class<?>) {
                        returnType = (Class<?>) returnTypeParameter;
                    } else if (returnTypeParameter instanceof ParameterizedType) {
                        // (gcode issue 443) actual type can be a also a parameterized type
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    }
                }
            } else if (Optional.class.equals(rawType)) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                Type returnTypeParameter = actualTypeArguments[0];
                if (returnTypeParameter instanceof Class<?>) {
                    returnType = (Class<?>) returnTypeParameter;
                }
            }
        }

        return returnType;
    }

    public static Type resolveReturnType(Method method, Type srcType) {
        Type returnType = method.getGenericReturnType();
        Class<?> declaringClass = method.getDeclaringClass();
        return resolveType(returnType, srcType, declaringClass);
    }

    public static Type resolveType(Type type, Type srcType, Class<?> declaringClass) {
        if (type instanceof TypeVariable) {
            return type;
        } else if (type instanceof ParameterizedType) {
            return type;
        } else if (type instanceof GenericArrayType) {
            return type;
        } else {
            return type;
        }
    }

    /**
     * Resolve srcType types.
     *
     * @param srcType the src type
     * @return get the actual type of the generic parameter on the interface
     */
    public static Type[] resolveMapperTypes(Class<?> srcType) {
        Type[] types = srcType.getGenericInterfaces();
        List<Type> result = new ArrayList<>();
        for (Type type : types) {
            if (type instanceof Class) {
                result.addAll(Arrays.asList(resolveMapperTypes((Class<?>) type)));
            } else if (type instanceof ParameterizedType) {
                Collections.addAll(result, ((ParameterizedType) type).getActualTypeArguments());
            }
        }
        return result.toArray(new Type[]{});
    }

    /**
     * Resolve srcType types.
     *
     * @param method  the method
     * @param srcType the src type
     * @return get the actual type of the generic parameter on the interface
     */
    public static Type[] resolveMapperTypes(Method method, Type srcType) {
        Class<?> declaringClass = method.getDeclaringClass();
        TypeVariable<? extends Class<?>>[] typeParameters = declaringClass.getTypeParameters();
        Type[] result = new Type[typeParameters.length];
        for (int i = 0; i < typeParameters.length; i++) {
            result[i] = resolveType(typeParameters[i], srcType, declaringClass);
        }
        return result;
    }

    /**
     * Resolve param types.
     *
     * @param method  the method
     * @param srcType the src type
     * @return The parameter types of the method as an array of {@link Type}s. If they have type parameters in the
     * declaration,<br>
     * they will be resolved to the actual runtime {@link Type}s.
     */
    public static Type[] resolveParamTypes(Method method, Type srcType) {
        Type[] paramTypes = method.getGenericParameterTypes();
        Class<?> declaringClass = method.getDeclaringClass();
        Type[] result = new Type[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            result[i] = resolveType(paramTypes[i], srcType, declaringClass);
        }
        return result;
    }


    static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> rawType;

        private final Type ownerType;

        private final Type[] actualTypeArguments;

        public ParameterizedTypeImpl(Class<?> rawType, Type ownerType, Type[] actualTypeArguments) {
            super();
            this.rawType = rawType;
            this.ownerType = ownerType;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public String toString() {
            return "ParameterizedTypeImpl [rawType=" + rawType + ", ownerType=" + ownerType + ", actualTypeArguments=" + Arrays.toString(actualTypeArguments) + "]";
        }
    }

    static class WildcardTypeImpl implements WildcardType {
        private final Type[] lowerBounds;

        private final Type[] upperBounds;

        WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
            super();
            this.lowerBounds = lowerBounds;
            this.upperBounds = upperBounds;
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBounds;
        }

        @Override
        public Type[] getUpperBounds() {
            return upperBounds;
        }
    }

    static class GenericArrayTypeImpl implements GenericArrayType {
        private final Type genericComponentType;

        GenericArrayTypeImpl(Type genericComponentType) {
            super();
            this.genericComponentType = genericComponentType;
        }

        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }
    }
}
