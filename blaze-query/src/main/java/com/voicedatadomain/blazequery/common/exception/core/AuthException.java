package com.voicedatadomain.blazequery.common.exception.core;

import com.voicedatadomain.blazequery.common.exception.BaseException;
import com.voicedatadomain.blazequery.common.model.enums.StatusCode;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
public class AuthException extends BaseException {

    public AuthException(StatusCode statusCode) {
        super(statusCode);
    }
}
