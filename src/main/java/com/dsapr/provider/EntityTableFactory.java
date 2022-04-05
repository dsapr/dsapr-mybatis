package com.dsapr.provider;

/**
 * 实体类信息工厂，可以通过 SPI 加入处理链
 *
 * @author dsapr
 * @date 2022/4/5
 */
public interface EntityTableFactory extends Order{
    /**
     * 根据实体类创建 EntityTable
     *
     * @param entityClass 实体类类型
     * @param chain       调用下一个
     * @return
     */
    EntityTable createEntityTable(Class<?> entityClass, Chain chain);

    /**
     * 工厂链
     */
    interface Chain {
        /**
         * 根据实体类创建 EntityTable
         *
         * @param entityClass 实体类类型
         * @return 实体类信息
         */
        EntityTable createEntityTable(Class<?> entityClass);
    }
}
