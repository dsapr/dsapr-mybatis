package com.dsapr.mapper;

import com.dsapr.example.ExampleProvider;
import com.dsapr.provider.Caching;
import org.apache.ibatis.annotations.Lang;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.cursor.Cursor;

/**
 * 游标查询方法
 *
 * @param <T> 实体类
 * @param <E> 符合Example数据结构的对象
 * @author dsapr
 * @date 2022/4/5
 */
public interface CursorMapper<T, E>{

    /**
     * 根据实体字段条件查询
     *
     * @param entity 实体类
     * @return 实体列表
     */
    @Lang(Caching.class)
    @SelectProvider(type = EntityProvider.class, method = "select")
    Cursor<T> selectCursor(T entity);

    /**
     * 根据 Example 条件查询
     *
     * @param example 条件
     * @return 实体列表
     */
    @Lang(Caching.class)
    @SelectProvider(type = ExampleProvider.class, method = "selectByExample")
    Cursor<T> selectCursorByExample(E example);
}
