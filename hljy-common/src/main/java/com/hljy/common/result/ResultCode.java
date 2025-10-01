package com.hljy.common.result;

/**
 * 枚举了一些常用API操作码
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    
    // 用户相关错误码
    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    USER_PASSWORD_ERROR(1003, "密码错误"),
    USER_ACCOUNT_DISABLED(1004, "账号已被禁用"),
    
    // 验证码相关错误码
    CAPTCHA_ERROR(2001, "验证码错误"),
    CAPTCHA_EXPIRED(2002, "验证码已过期"),
    
    // 文件上传相关错误码
    FILE_UPLOAD_FAILED(3001, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORTED(3002, "文件类型不支持"),
    FILE_SIZE_EXCEEDED(3003, "文件大小超出限制"),
    
    // 匹配相关错误码
    MATCH_NOT_FOUND(4001, "匹配记录不存在"),
    
    // 聊天相关错误码
    CHAT_SESSION_NOT_FOUND(5001, "聊天会话不存在"),
    MESSAGE_SEND_FAILED(5002, "消息发送失败"),
    
    // 动态相关错误码
    MOMENT_NOT_FOUND(6001, "动态不存在"),
    MOMENT_ALREADY_LIKED(6002, "已经点赞过了"),
    
    // 通知相关错误码
    NOTIFICATION_SEND_FAILED(7001, "通知发送失败"),
    
    // 支付相关错误码
    PAYMENT_FAILED(8001, "支付失败"),
    ORDER_NOT_FOUND(8002, "订单不存在"),
    ORDER_ALREADY_PAID(8003, "订单已支付"),
    
    // 系统相关错误码
    SYSTEM_ERROR(9001, "系统异常"),
    SERVICE_UNAVAILABLE(9002, "服务暂不可用"),
    REQUEST_TOO_FREQUENT(9003, "请求过于频繁");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

