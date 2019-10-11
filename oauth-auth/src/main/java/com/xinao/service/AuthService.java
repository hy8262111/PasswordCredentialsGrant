package com.xinao.service;

import com.xinao.entity.AuthToken;
import com.xinao.entity.LoginResult;

/**
 * @Author: houyong
 * @Date: 2019/10/10 10:40
 * @describe
 */
public interface AuthService {
    AuthToken login(LoginResult loginResult);
}
