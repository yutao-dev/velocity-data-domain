package com.voicedatadomain.blazequery.auth.application.service.impl;

import com.voicedatadomain.blazequery.auth.application.service.AuthService;
import com.voicedatadomain.blazequery.auth.domain.model.UserAuth;
import com.voicedatadomain.blazequery.auth.domain.service.AuthDomainService;
import com.voicedatadomain.blazequery.common.model.bean.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 
 * 
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthDomainService authDomainService;

    /**
     * 发送短信验证码
     *
     * @param phone 手机号码
     * @return 验证码
     */
    @Override
    public String sendSms(String phone) {
        UserAuth userAuth = new UserAuth(phone);

        return authDomainService.sendSmsCode(userAuth);
    }
}
