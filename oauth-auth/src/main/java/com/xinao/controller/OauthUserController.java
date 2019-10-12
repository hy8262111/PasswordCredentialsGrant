package com.xinao.controller;

import com.alibaba.fastjson.JSONObject;
import com.xinao.entity.LoginResult;
import com.xinao.entity.UserSession;
import com.xinao.service.AuthService;
import com.xinao.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @Author: houyong
 * @Date: 2019/10/10 16:10
 * @describe: 获取用户接口
 */
@RestController
@RequestMapping("/auth")
public class OauthUserController extends BaseController {


    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private AuthService authService;


    /**
     * 获取用户名,可以设置在请求头里key-value形式     Authorization           bearer 5c65b89e-a7e1-480d-89fe-1327fb45de07
     * 也可以使用form-data表单形式提交     access_token            5c65b89e-a7e1-480d-89fe-1327fb45de07
     *
     * @param principal
     * @return
     */
    @RequestMapping(value = "/getUserName", method = RequestMethod.POST)
    public String currentUserName(Principal principal) {
        return principal.getName();
    }


    /**
     * 获取全面的用户信息，可以设置在请求头里key-value形式     Authorization           bearer 5c65b89e-a7e1-480d-89fe-1327fb45de07
     * 也可以使用form-data表单形式提交     access_token            5c65b89e-a7e1-480d-89fe-1327fb45de07
     *
     * @param auth
     * @return
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public UserSession login(Authentication auth) {
        UserSession user = (UserSession) auth.getPrincipal();
        return user;
    }


    /**
     * 用户登出接口，销毁redis，清空cookie
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public JSONObject logout(String token) {
        //清空redis
        consumerTokenServices.revokeToken(token);
        //清空cookie
        clearToken();
        return null;
    }


    @PostMapping("/login")
    public String login(LoginResult loginResult) {
        if (loginResult == null || StringUtils.isEmpty(loginResult.getUserName())) {
            //账号没有输入
        }
        String token =  authService.login(loginResult);

        //存储令牌到cookkie中存储令牌到cookie
        saveTokenToCookie(token);

        return token;
    }


    //保存令牌到cookie
    private void saveTokenToCookie(String token) {
        CookieUtil.addCookie(response, "/", "uid", token, 3600, true);
    }

    //清空cookie中得令牌
    private void clearToken() {
        CookieUtil.addCookie(response, "/", "uid", "", 0, true);
    }

}
