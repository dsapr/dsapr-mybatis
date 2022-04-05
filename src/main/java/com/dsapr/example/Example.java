package com.dsapr.example;

/**
 * 通用查询对象
 *
 * @author dsapr
 * @date 2022/4/3
 */
public class Example<T> {
    /**
     * 排序字段
     */
    protected String order;

    /**
     * 是否使用distinct
     */
    protected Boolean distinct;

    /**
     * 指定查询列
     */
    protected String selectColumns;

    protected String startSql;

    protected String endSql;
}
