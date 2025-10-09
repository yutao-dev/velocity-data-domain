package com.voicedatadomain.blazequery.auth.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Data
public class LoginLog {
    private Long logId;
    private Long userId;
    private String ip;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
}
