package com.lvchenglong.exception;

import com.lvchenglong.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.lvchenglong.controller")
@Slf4j
public class GlobalExceptionHandler {
    //统一异常处理@ExceptionHandler,主要用于Exception
    @ExceptionHandler(Exception.class)
    //返回json串
    @ResponseBody
    public Result error(HttpServletRequest request,Exception e){
        log.error("异常消息：",e);
        return Result.error();
    }

    //统一异常处理@ExceptionHandler,主要用于Exception
    @ExceptionHandler(CustomException.class)
    //返回json串
    @ResponseBody
    public Result CustomError(HttpServletRequest request,CustomException e){
        log.error("业务异常：",e);
        return Result.error(e.getCode(),e.getMsg());
    }
}
