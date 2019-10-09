package com.xinao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
/**
 * @Author: houyong
 * @Date: 2019/9/26 10:25
 * @describe
 */
@Component("customWebResponseExceptionTranslator")
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        if (e instanceof OAuth2Exception || e instanceof AuthenticationServiceException) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new CustomOauthException("用户名或者密码错误"));
        }
        return null;
    }
}

