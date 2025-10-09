package com.voicedatadomain.blazequery.auth.domain.model;

import com.voicedatadomain.blazequery.common.exception.core.AuthException;
import com.voicedatadomain.blazequery.common.model.enums.impl.AuthCode;
import com.voicedatadomain.blazequery.common.util.StringUtils;
import lombok.Data;

/**
 * 用户认证信息聚合根
 * 
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Data
public class UserAuth {
    /**
     * 用户唯一标识
     */
    private Long userId;
    
    /**
     * 用户名，用于登录认证
     * 注意：下述字段因为具有认证的业务意义，因此不进行非聚合根的提取
     */
    private String username;
    
    /**
     * 用户密码，用于登录认证
     */
    private String password;
    
    /**
     * 手机号码，用于手机登录和验证
     */
    private String phone;
    
    /**
     * 电子邮箱，用于邮箱登录和验证
     */
    private String email;
    
    /**
     * 用户登录日志信息
     * 聚合根要直接包含非聚合根
     */
    private LoginLog loginLog;
    
    /**
     * 用户基本资料信息
     */
    private UserProfile userProfile;
    
    /**
     * 默认构造函数，初始化登录日志和用户资料
     */
    public UserAuth() {
        this.loginLog = new LoginLog();
        this.userProfile = new UserProfile();
    }
    
    /**
     * 带手机号的构造函数
     * 
     * @param phone 手机号码
     * @throws AuthException 当手机号格式不正确时抛出异常
     */
    public UserAuth(String phone) {
        if (!StringUtils.regexMatch(phone, StringUtils.RegexType.PHONE)) {
            throw new AuthException(AuthCode.PHONE_ERROR);
        }
        
        this.loginLog = new LoginLog();
        this.userProfile = new UserProfile();
    }
}
