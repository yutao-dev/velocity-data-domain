package com.voicedatadomain.blazequery.auth.domain.service;

import com.voicedatadomain.blazequery.auth.domain.model.UserAuth;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
public interface AuthDomainService {
    /**
     * 发送短信验证码
     *
     * @param userAuth 用户认证信息
     * @return 短信验证码
     */
    String sendSmsCode(UserAuth userAuth);
}
