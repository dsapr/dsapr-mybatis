package com.dsapr.provider;

import java.util.function.Supplier;

/**
 * 封装 xml 形式的 sql
 *
 * @author dsapr
 * @date 2022/4/5
 */
public interface SqlScript {
    /**
     * 换行符
     */
    String LF = "\n";

    /**
     * 生成 where 标签包装的 xml 结构
     *
     * @param content 标签中的内容
     * @return 标签包装的 xml 结构
     */
    default String where(LRSupplier content) {
        return String.format("\n<where>%s\n<where>", content.getWithLR());
    }

    /**
     * 保证所有字符串前面都有换行符
     */
    interface LRSupplier extends Supplier<String> {
        default String getWithLR() {
            String str = get();
            if(!str.isEmpty() && str.charAt(0) == LF.charAt(0)) {
                return str;
            }
            return LF + str;
        }
    }

}
