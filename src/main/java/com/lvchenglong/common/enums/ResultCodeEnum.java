package com.lvchenglong.common.enums;

public enum ResultCodeEnum {
    SUCCESS("code","请求成功"),
    TOKEN_INVALID_ERROR("401","请重新登录"),
    PARAM_ERROR("400","参数错误"),
    SYSTEM_ERROR("500","系统异常"),
    ;
    public String code;
    public String msg;

     ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
