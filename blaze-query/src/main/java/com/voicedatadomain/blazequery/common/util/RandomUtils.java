package com.voicedatadomain.blazequery.common.util;

import java.util.Random;

/**
 * 随机字符串生成工具类
 *
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
public class RandomUtils {
    
    /**
     * 字符集，包含大小写字母和数字
     */
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    /**
     * 随机数生成器实例
     */
    private static final Random RANDOM = new Random();
    
    /**
     * 私有构造函数，防止实例化
     */
    private RandomUtils() {}
    
    /**
     * 生成指定长度的随机字符串
     *
     * @param length 字符串长度
     * @return 指定长度的随机字符串
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char randomChar = generateRandomCharacter();
            sb.append(randomChar);
        }
        return sb.toString();
    }
    
    /**
     * 从字符集中随机选择一个字符
     *
     * @return 随机字符
     */
    private static char generateRandomCharacter() {
        int randomIndex = RANDOM.nextInt(ALPHABET.length());
        return ALPHABET.charAt(randomIndex);
    }
}
