package com.voicedatadomain.blazequery.common.model.enums.impl;

import com.voicedatadomain.blazequery.common.model.enums.StatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Getter
@RequiredArgsConstructor
public enum AuthCode implements StatusCode {

    /**
     * 登录成功
     */
    LOGIN_SUCCESS(200, "登录成功！"),

    /**
     * 手机号格式错误或者不存在
     */
    PHONE_ERROR(1001, "手机号格式错误或者不存在！"),

    /**
     * 操作频繁
     */
    FREQUENT_OPERATIONS(1002, "操作频繁，请稍后再试！");

    private final Integer code;
    private final String message;
}
