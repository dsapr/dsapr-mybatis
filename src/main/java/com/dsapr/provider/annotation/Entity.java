package com.dsapr.provider.annotation;

/**
 * 实体
 *
 * @author dsapr
 * @date 2022/4/5
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Entity {
    /**
     * 实体类
     */
    Class<?> value();

    /**
     * 属性配置
     */
    @interface Prop {
        /**
         * 属性名
         */
        String name();

        /**
         * 属性值
         */
        String value();

        /**
         * 属性值类型，支持 String, Integer, Long, Boolean, Double, Float 六种类型
         */
        Class type() default String.class;
    }
}
