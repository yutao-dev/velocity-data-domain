package com.voicedatadomain.blazequery.common.exception.core;

import com.voicedatadomain.blazequery.common.exception.BaseException;
import com.voicedatadomain.blazequery.common.model.enums.StatusCode;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
public class SystemException extends BaseException {
    /**
     * 构造函数，接受一个状态码对象作为参数，用于初始化基础异常类。
     *
     * @param statusCode 状态码对象，包含错误码和对应的描述信息
     */
    public SystemException(StatusCode statusCode) {
        super(statusCode);
    }

    /**
     * 获取当前异常关联的状态码对象。
     * 此方法重写了父类BaseException中的getStatusCode方法，
     * 提供了更具体的返回值类型（StatusCode接口实现）。
     *
     * @return 返回绑定到此异常的状态码对象
     */
    @Override
    public StatusCode getStatusCode() {
        return super.getStatusCode();
    }
}
