package com.dsapr.provider;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.logging.log4j.util.Supplier;

/**
 * sql 缓存
 *
 * @author dsapr
 * @date 2022/4/5
 */
public class SqlCache {
    private final ProviderContext providerContext;

    private final EntityTable entity;

    private final Supplier<String> sqlScriptSupplier;

    public SqlCache(ProviderContext providerContext, Supplier<String> sqlScriptSupplier) {
        this.providerContext = providerContext;
        this.sqlScriptSupplier = sqlScriptSupplier;
    }
}
