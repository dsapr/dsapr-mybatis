package com.dsapr.provider;

/**
 * 实体中字段和列对应关系接口
 *
 * @author dsapr
 * @date 2022/4/5
 */
public class EntityColumn extends EntityProps<EntityColumn>{
    protected final EntityField field;

    /**
     * 所属实体类
     */
    protected EntityTable entityTable;

    /**
     * 列名
     */
    protected String column;

    /**
     * 是否为主键
     */
    protected boolean id;

    protected EntityColumn(EntityField field) {
        this.field = field;
    }
}
