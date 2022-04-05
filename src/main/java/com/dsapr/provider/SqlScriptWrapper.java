package com.dsapr.provider;

import com.dsapr.mapper.EntityProvider;
import com.dsapr.provider.util.ServiceLoaderUtils;
import org.apache.ibatis.builder.annotation.ProviderContext;

import java.util.List;

/**
 * SPI 接口：对最终的 SQL 进行处理
 *
 * @author dsapr
 * @date 2022/4/5
 */
public interface SqlScriptWrapper {
    /**
     * 对 script 包装中的 SQL 进行加工处理
     *
     * @param context   接口和方法信息
     * @param entity    实体类
     * @param sqlScript sql脚本
     * @return
     */
    SqlScript wrap(ProviderContext context, EntityTable entity, SqlScript sqlScript);

    static SqlScript wrapSqlScript(ProviderContext context, EntityTable entity, SqlScript sqlScript) {
        for (SqlScriptWrapper wrapper : Instance.getEntityTableFactoryChain()) {
            sqlScript = wrapper.wrap(context, entity, sqlScript);
        }
        return sqlScript;
    }

    /**
     * 实例
     */
    class Instance {
        private static volatile List<SqlScriptWrapper> sqlScriptWrappers;

        public static List<SqlScriptWrapper> getEntityTableFactoryChain() {
            if (sqlScriptWrappers == null) {
                synchronized (EntityFactory.class) {
                    if (sqlScriptWrappers == null) {
                        sqlScriptWrappers = ServiceLoaderUtils.getInstances(SqlScriptWrapper.class);
                    }
                }
            }
            return sqlScriptWrappers;
        }
    }
}
