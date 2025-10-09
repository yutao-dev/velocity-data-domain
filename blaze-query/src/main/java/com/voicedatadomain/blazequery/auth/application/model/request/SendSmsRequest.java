package com.voicedatadomain.blazequery.auth.application.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Data
public class SendSmsRequest {

    @NotEmpty(message = "手机号不能为空")
    private String phone;
}
