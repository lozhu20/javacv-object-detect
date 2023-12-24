package com.random.carryyou.config;

import com.random.carryyou.dto.Response;
import com.random.carryyou.utils.ResponseUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalHandlerExceptionResolver {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response<Void> allExceptionHandler(Exception e) {
        return ResponseUtil.fail(e.getMessage());
    }

}
