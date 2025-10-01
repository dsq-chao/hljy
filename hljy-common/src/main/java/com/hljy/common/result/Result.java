package com.hljy.common.result;

import lombok.Data;

/**
 * 统一返回结果
 */
@Data
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
     * 时间戳
     */
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message) {
        this();
        this.code = code;
        this.message = message;
    }
    
    public Result(Integer code, String message, T data) {
        this(code, message);
        this.data = data;
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), message, data);
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return new Result<T>(ResultCode.FAILED.getCode(), ResultCode.FAILED.getMessage());
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(String message) {
        return new Result<T>(ResultCode.FAILED.getCode(), message);
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(ResultCode resultCode) {
        return new Result<T>(resultCode.getCode(), resultCode.getMessage());
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed(Integer code, String message) {
        return new Result<T>(code, message);
    }
    
    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }
    
    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result<T>(ResultCode.VALIDATE_FAILED.getCode(), message);
    }
    
    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized() {
        return failed(ResultCode.UNAUTHORIZED);
    }
    
    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden() {
        return failed(ResultCode.FORBIDDEN);
    }
}

