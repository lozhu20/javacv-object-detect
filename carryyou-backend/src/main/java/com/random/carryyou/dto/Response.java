package com.random.carryyou.dto;

import lombok.Data;

@Data
public class Response<T> {

    private final Integer code;

    private final String message;

    private final T data;


    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Response(ResultEnum resultEnum, T data) {
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
        this.data = data;
    }
}
