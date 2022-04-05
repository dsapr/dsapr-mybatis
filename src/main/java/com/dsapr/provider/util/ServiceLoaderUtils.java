package com.dsapr.provider.util;

import com.dsapr.provider.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SPI 工具类
 *
 * @author dsapr
 * @date 2022/4/5
 */
public class ServiceLoaderUtils {
    public static <T> List<T> getInstances(Class<T> clazz) {
        // SPI 获取所有实现类
        ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);
        List<T> instances = new ArrayList<>();
        for (T factory : serviceLoader) {
            instances.add(factory);
        }
        // 判断是否为 Order 子类
        if (instances.size() > 1 && Order.class.isAssignableFrom(clazz)) {
            instances.sort(Comparator.comparing(f -> ((Order) f).getOrder()).reversed());
        }
        return instances;
    }
}
