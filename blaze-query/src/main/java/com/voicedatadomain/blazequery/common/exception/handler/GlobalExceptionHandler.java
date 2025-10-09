package com.voicedatadomain.blazequery.common.exception.handler;

import com.voicedatadomain.blazequery.common.exception.BaseException;
import com.voicedatadomain.blazequery.common.model.bean.Result;
import com.voicedatadomain.blazequery.common.model.enums.StatusCode;
import com.voicedatadomain.blazequery.common.model.enums.impl.SystemCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     * @param e 业务异常
     * @return 统一响应结果
     */
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleBaseException(BaseException e) {
        StatusCode statusCode = e.getStatusCode();
        String message = e.getMessage();
        Result<String> fail = Result.fail(statusCode);
        fail.logError(message);
        return fail;
    }

    /**
     * SQL异常处理
     * @param e SQL异常
     * @return 统一响应结果
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleSqlException(SQLException e) {
        StringBuilder errorMsg = new StringBuilder();
        Throwable cause = e;
        int depth = 0;

        // 循环获取SQL异常链信息
        while (cause != null && depth < 10) {
            errorMsg.append(cause.getClass().getSimpleName())
                    .append(": ")
                    .append(cause.getMessage())
                    .append("; ");
            cause = cause.getCause();
            depth++;
        }

        Result<String> fail = Result.fail(SystemCode.FAIL);
        fail.logError(errorMsg.toString());
        return fail;
    }

    /**
     * 参数校验异常处理
     * @param e 校验异常
     * @return 统一响应结果
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMsg = bindingResult.getFieldError() != null
                ? bindingResult.getFieldError().getDefaultMessage()
                : "参数校验失败";

        Result<String> fail = Result.fail(SystemCode.FAIL);
        fail.logError(errorMsg);

        return fail;
    }

    /**
     * 其他校验异常处理（如@RequestParam参数校验）
     * @param e 校验异常
     * @return 统一响应结果
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMsg = e.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("参数校验失败");

        Result<String> fail = Result.fail(SystemCode.FAIL);
        fail.logError(errorMsg);

        return fail;
    }

    /**
     * 框架异常处理
     * @param e 框架异常
     * @return 统一响应结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleFrameworkException(Exception e) {
        StringBuilder errorMsg = new StringBuilder();
        Throwable cause = e;
        int depth = 0;

        // 循环获取异常链信息
        while (cause != null && depth < 10) {
            errorMsg.append(cause.getClass().getName())
                    .append(": ")
                    .append(cause.getMessage())
                    .append("; ");
            cause = cause.getCause();
            depth++;
        }

        Result<String> fail = Result.fail(SystemCode.FAIL);
        fail.logError(errorMsg.toString());
        return fail;
    }
}