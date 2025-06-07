package com.linyelai.enums;

/**
 * 响应状态码枚举
 */
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(400, "参数校验失败"),
    UNAUTHORIZED(401, "未授权或token已过期"),
    FORBIDDEN(403, "没有相关权限"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    // 业务相关状态码
    SHOP_NOT_EXIST(10001, "店铺不存在"),
    SHOP_STATUS_ERROR(10002, "店铺状态异常"),
    SHOP_NAME_EXISTS(10003, "店铺名称已存在");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}