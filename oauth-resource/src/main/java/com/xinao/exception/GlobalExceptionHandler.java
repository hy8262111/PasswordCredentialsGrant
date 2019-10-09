package com.xinao.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 处理springsecurity没有权限异常,注意导包不要弄错
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Response handleAccessDeniedException(AccessDeniedException e) {
        logger.warn("", e);
        return new Response("1111", "没有权限");
    }

    /**
     * 处理所有不可知的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response handleException(Exception e) {
        logger.error(e.getMessage(), e);
        Response response = new Response();
        response.setCode(RetCode.EXCEPTION.getCode());
        response.setMsg(RetCode.EXCEPTION.getMsg());
        response.setDesc(e.getMessage());
        return response;
    }


}
