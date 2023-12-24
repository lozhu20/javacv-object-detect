package com.random.carryyou.utils;

import com.random.carryyou.dto.Response;
import com.random.carryyou.dto.ResultEnum;

public class ResponseUtil {

    public static <T> Response<T> success(T data) {
        return new Response<>(ResultEnum.SUCCESS, data);
    }

    public static <T> Response<T> fail(String message) {
        return new Response<>(ResultEnum.FAIL.getCode(), message, null);
    }
}
