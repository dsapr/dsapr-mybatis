package com.dsapr.provider;

/**
 * @author dsapr
 * @date 2022/4/5
 */
public interface Order {
    /**
     * 数字越大越早执行
     */
    default int getOrder() {
        return 0;
    }
}
