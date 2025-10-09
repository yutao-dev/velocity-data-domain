package com.voicedatadomain.blazequery.auth.application.controller;

import com.voicedatadomain.blazequery.auth.application.model.request.SendSmsRequest;
import com.voicedatadomain.blazequery.auth.application.service.AuthService;
import com.voicedatadomain.blazequery.common.model.bean.Result;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * 
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 发送短信验证码
     * @param request 发送短信请求参数
     * @return 发送结果
     */
    @PostMapping("/sms")
    public Result<String> sendSms(@Validated @RequestBody SendSmsRequest request) {
        log.info("发送短信请求参数：{}", request);

        String smsCode = authService.sendSms(request.getPhone());

        return Result.success(smsCode);
    }
}
