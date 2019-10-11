package com.xinao.entity;

import lombok.Data;

/**
 * @Author: houyong
 * @Date: 2019/10/9 17:42
 * @describe
 */
@Data
public class LoginResult {
    private String clientId;
    private String clientSecret;
    private String userName;
    private String password;
}
