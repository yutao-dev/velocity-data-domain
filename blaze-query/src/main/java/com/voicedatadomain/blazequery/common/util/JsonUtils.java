package com.voicedatadomain.blazequery.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

/**
 * JSON工具类，提供JSON序列化和反序列化功能
 *
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Slf4j
public class JsonUtils {
    
    /**
     * 全局共享的ObjectMapper实例，已配置好通用设置
     */
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    
    /**
     * 私有构造函数，防止实例化工具类
     */
    private JsonUtils() {}

    /*
      静态初始化块，配置ObjectMapper的通用属性
      1. 自动发现并注册所有可用模块
      2. 反序列化时忽略未知属性
      3. 将空字符串视为null对象
      4. 序列化时空对象不抛出异常
     */
    static {
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
    
    /**
     * 将对象序列化为JSON字符串
     *
     * @param obj 待序列化的对象
     * @return 序列化后的JSON字符串，失败时返回空字符串
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json序列化错误", e);
        }
        return "";
    }
    
    /**
     * 将JSON字符串反序列化为指定类型的对象
     *
     * @param json  待反序列化的JSON字符串
     * @param clazz 目标对象的Class类型
     * @param <T>   泛型参数，表示目标对象类型
     * @return 反序列化后的对象，失败时返回null
     */
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json反序列化错误", e);
        }
        return null;
    }
    
    /**
     * 将字节数组反序列化为指定类型的对象（支持复杂泛型类型）
     *
     * @param bytes 待反序列化的字节数组
     * @param clazz 目标对象的TypeReference类型引用
     * @param <T>   泛型参数，表示目标对象类型
     * @return 反序列化后的对象，失败时返回null
     */
    public static <T> T parse(byte[] bytes, TypeReference<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (Exception e) {
            log.error("json反序列化错误", e);
        }
        return null;
    }
}
   
