package com.voicedatadomain.blazequery.common.model.enums.impl;

import com.voicedatadomain.blazequery.common.model.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Getter
@AllArgsConstructor
public enum SystemCode implements StatusCode {

    /**
     * 表示请求成功处理
     */
    SUCCESS(200, "请求成功！"),

    /**
     * 表示参数错误
     */
    PARAM_ERROR(400, "参数错误！"),

    /**
     * 缺少访问权限
     */
    UNACCESSED(401, "无访问权限！"),

    /**
     * 找不到资源
     */
    NOT_FOUND(404, "未找到该资源！"),

    /**
     * 表示服务器内部异常
     */
    FAIL(500, "服务器内部异常，请联系管理员！");


    private final Integer code;
    private final String message;
}
