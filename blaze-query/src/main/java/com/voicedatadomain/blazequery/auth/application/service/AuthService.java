package com.voicedatadomain.blazequery.auth.application.service;

import com.voicedatadomain.blazequery.common.model.bean.Result;
import jakarta.validation.constraints.NotEmpty;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
public interface AuthService {

    /**
     * 发送短信验证码
     * @param phone 手机号码
     * @return 验证码
     */
    String sendSms(String phone);
}
