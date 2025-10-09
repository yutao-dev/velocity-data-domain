package com.voicedatadomain.blazequery.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字符串工具类，提供常用的字符串处理方法
 *
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
public class StringUtils {
    
    /**
     * 正则表达式类型枚举，定义了常用的正则表达式模式
     */
    @Getter
    @AllArgsConstructor
    public enum RegexType {
        /**
         * 邮箱正则表达式
         * 匹配格式：用户名@域名.顶级域名（如：example@test.com）
         */
        EMAIL("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", "邮箱格式错误！"),
        
        /**
         * 手机号正则表达式
         * 匹配中国大陆手机号格式：以1开头，第二位为3-9之间的数字，后面跟9位数字
         */
        PHONE("^1[3-9]\\d{9}$", "手机号格式错误！"),
        
        /**
         * 身份证号正则表达式
         * 支持15位或18位身份证号格式
         * 18位：17位数字加最后一位数字或X
         * 15位：15位数字
         */
        ID_CARD("^(\\d{17}(\\d|X))|(\\d{15})$", "身份证号格式错误！");
        
        /**
         * 正则表达式模式
         */
        private final String regex;
        
        /**
         * 错误提示信息
         */
        private final String message;
    }
    
    /**
     * 私有构造函数，防止实例化工具类
     */
    private StringUtils() {}
    
    /**
     * 判断字符串是否为空
     * 
     * @param str 待判断的字符串
     * @return true: 字符串为null或空字符串; false: 字符串不为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }
    
    /**
     * 判断字符串是否不为空
     * 
     * @param str 待判断的字符串
     * @return true: 字符串不为null且不为空字符串; false: 字符串为空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * 使用指定的正则表达式类型验证字符串格式
     * 
     * @param str 待验证的字符串
     * @param regex 正则表达式类型
     * @return true: 字符串匹配正则表达式; false: 字符串不匹配正则表达式
     */
    public static boolean regexMatch(String str, RegexType regex) {
        return str.matches(regex.getRegex());
    }
}
