package com.voicedatadomain.blazequery.auth.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Data
@TableName("login_logs")
public class LoginLogPo {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String ip;
    private LocalDateTime loginTime;
    private LocalDateTime logoutTime;
}
