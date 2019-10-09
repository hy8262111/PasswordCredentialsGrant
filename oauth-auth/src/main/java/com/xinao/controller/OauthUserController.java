package com.xinao.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

/**
 * @Author: houyong
 * @Date: 2019/9/24 16:10
 * @describe: 获取用户接口
 */
@Controller
@RequestMapping
@Slf4j
public class OauthUserController {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;


    /**
     * 获取用户名,可以设置在请求头里key-value形式     Authorization           bearer 5c65b89e-a7e1-480d-89fe-1327fb45de07
     *           也可以使用form-data表单形式提交     access_token            5c65b89e-a7e1-480d-89fe-1327fb45de07
     *
     * @param principal
     * @return
     */
    @RequestMapping(value = "/username", method = RequestMethod.POST)
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }


    /**
     * 获取全面的用户信息，可以设置在请求头里key-value形式     Authorization           bearer 5c65b89e-a7e1-480d-89fe-1327fb45de07
     *                    也可以使用form-data表单形式提交     access_token            5c65b89e-a7e1-480d-89fe-1327fb45de07
     * @param auth
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody()
    public  User login(Authentication auth) {
        User user = (User) auth.getPrincipal();
        return user;
    }


    /**
     * 用户登出接口，销毁redis
     *
     * @param access_token 必传access_token
     * @return
     */
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    @ResponseBody()
    public JSONObject logout(String access_token) {
        boolean b = consumerTokenServices.revokeToken(access_token);
        return null;
    }

}
