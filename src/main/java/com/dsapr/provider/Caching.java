package com.dsapr.provider;

import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存 xml 形式对应的 SqlSource，避免重复解析
 *
 * @author dsapr
 * @date 2022/4/5
 */
public class Caching extends XMLLanguageDriver {
    private static final Map<String, SqlCache> CACHE_SQL = new ConcurrentHashMap<>(16);
}
