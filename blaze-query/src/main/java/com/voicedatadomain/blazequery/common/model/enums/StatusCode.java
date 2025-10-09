package com.voicedatadomain.blazequery.common.model.enums;

import java.util.UUID;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
public interface StatusCode {
    /**
     * 获取状态码的数字标识
     * @return 状态码数值
     */
    Integer getCode();

    /**
     * 获取状态码对应的描述信息
     * @return 状态描述文本
     */
    String getMessage();

    /**
     * 默认方法，生成唯一跟踪ID用于链路追踪
     * @return UUID字符串
     */
    default String getTraceId() {
        return UUID.randomUUID().toString();
    }
}