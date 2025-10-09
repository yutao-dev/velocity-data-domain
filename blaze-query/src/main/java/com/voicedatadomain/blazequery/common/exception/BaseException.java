package com.voicedatadomain.blazequery.common.exception;

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
public class BaseException extends RuntimeException {
    /**
     * 异常对应的错误状态码对象，提供详细的错误信息和数值标识
     */
    private final StatusCode statusCode;
}
