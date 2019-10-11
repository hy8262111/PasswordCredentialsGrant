package com.xinao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by houyong on 2019/10/10
 * 用户信息拓展类，返回给调用方
 */
@Data
@ToString
public class AuthToken {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Integer expiresIn;
    private String scope;
}
