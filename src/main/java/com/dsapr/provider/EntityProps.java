package com.dsapr.provider;

import com.dsapr.provider.annotation.Entity;
import org.springframework.jdbc.core.metadata.CallMetaDataProviderFactory;

import java.util.Properties;

/**
 * @author dsapr
 * @date 2022/4/5
 */
public class EntityProps <T extends EntityProps>{

    /**
     * 附加属性用于拓展
     */
    protected Properties props;

    public Properties props() {
        return props;
    }

    /**
     * 获取属性值
     *
     * @param prop 属性名
     * @return 属性值
     */
    public <V> V getProp(String prop) {
        return prop != null ? (V) props.get(prop) : null;
    }

    /**
     * 获取属性值
     *
     * @param prop 属性名
     * @param def  默认值
     * @return 属性值
     */
    public <V> V getProp(String prop, V def) {
        V val = getProp(prop);
        return val != null ? val : def;
    }

    /**
     * 设置属性值
     *
     * @param prop  属性名
     * @param value 属性值
     */
    public T setProp(String prop, Object value) {
        if (props == null) {
            props = new Properties();
        }
        props.put(prop, value);
        return (T) this;
    }

    /**
     * 设置属性值
     *
     * @param prop 注解信息
     */
    public T setProp(Entity.Prop prop) {
        if (props == null) {
            props = new Properties();
        }
        props.put(prop.name(), getEntityPropValue(prop));
        return (T) this;
    }

    /**
     * 获取属性值
     */
    public static Object getEntityPropValue(Entity.Prop prop) {
        Class type = prop.type();
        if (type == Boolean.class) {
            return Boolean.parseBoolean(prop.value());
        } else if (type == Integer.class) {
            return Integer.parseInt(prop.value());
        } else if (type == Long.class) {
            return Long.parseLong(prop.value());
        }else if (type == Double.class) {
            return Double.parseDouble(prop.value());
        }else if (type == Float.class) {
            return Float.parseFloat(prop.value());
        }
        return prop.value();
    }
}
