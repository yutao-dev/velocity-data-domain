package com.voicedatadomain.blazequery.auth.domain.service.impl;

import com.voicedatadomain.blazequery.auth.domain.model.UserAuth;
import com.voicedatadomain.blazequery.auth.domain.service.AuthDomainService;
import com.voicedatadomain.blazequery.common.cache.StringCacheProvider;
import com.voicedatadomain.blazequery.common.model.enums.CacheKey;
import com.voicedatadomain.blazequery.common.util.RandomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 认证领域服务实现类
 * 提供用户认证相关的核心业务逻辑实现
 *
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthDomainServiceImpl implements AuthDomainService {
    private final StringCacheProvider stringCacheProvider;

    /**
     * 发送短信验证码
     * 生成6位随机短信验证码并存储到缓存中，用于后续验证
     *
     * @param userAuth 用户认证信息，包含手机号等信息
     * @return 生成的6位短信验证码
     */
    @Override
    public String sendSmsCode(UserAuth userAuth) {
        // 获取用户手机号
        String phone = userAuth.getPhone();

        // 生成6位随机字符串作为短信验证码
        String smsCode = RandomUtils.generateRandomNumber(6);

        // 构建手机号对应的缓存key
        String phoneKey = CacheKey.PHONE_CODE.keyAssembled(phone);
        
        // 将验证码存储到缓存中，key为手机号，value为验证码
        stringCacheProvider.setString(phoneKey, smsCode);
        
        // 返回生成的短信验证码
        return smsCode;
    }
}
