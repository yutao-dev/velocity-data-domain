package com.voicedatadomain.blazequery.common.model.bean;


import com.voicedatadomain.blazequery.common.model.enums.StatusCode;
import com.voicedatadomain.blazequery.common.model.enums.impl.SystemCode;
import com.voicedatadomain.blazequery.common.util.JsonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Data
@Slf4j
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 请求追踪ID
     */
    private String traceId;

    /**
     * 成功返回结果
     * @param data 返回数据
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        SystemCode success = SystemCode.SUCCESS;
        result.setCode(success.getCode());
        result.setMessage(success.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 成功返回结果
     * @return Result对象
     */
    public static Result<String> success() {
        return success(null);
    }

    /**
     * 失败返回结果
     * @param code 错误码
     * @param message 错误信息
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> fail(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败返回结果
     * @param <T> 数据类型
     * @return Result对象
     */
    public static <T> Result<T> fail(StatusCode statusCode) {
        Result<T> result = new Result<>();
        result.setCode(statusCode.getCode());
        result.setMessage(statusCode.getMessage());
        result.setTraceId(statusCode.getTraceId());
        return result;
    }

    /**
     * 错误返回结果
     * @return Result对象
     */
    public Result<T> logError() {
        if (!SystemCode.SUCCESS.getMessage().equals(this.message)) {
            logError(this.message);
        }
        return this;
    }

    /**
     * 错误返回结果
     */
    public void logError(String message) {
        if (!SystemCode.SUCCESS.getMessage().equals(this.message)) {
            log.error("请求失败！错误码: {}, 信息: {}, 追踪序号: {}", this.code, message, this.traceId);
        }
    }

    /**
     * 转换为JSON字符串
     * @return JSON字符串
     */
    public String toJsonStr() {
        return JsonUtils.toJson(this);
    }
}
