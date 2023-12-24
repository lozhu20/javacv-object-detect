package com.random.carryyou.dto;

import lombok.Getter;

@Getter
public enum ResultEnum {

    SUCCESS(0, "请求成功"),

    FAIL(-1, "处理失败"),

    TOKEN_EXPIRED(-2, "登陆信息过期");

    private final Integer code;
    private final String message;

    ResultEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
