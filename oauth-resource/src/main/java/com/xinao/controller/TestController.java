package com.xinao.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: houyong
 * @Date: 2019/9/24 11:26
 * @describe
 */
@RestController
public class TestController {

    /**
     * 该用户有此权限
     * @param
     * @return
     */
    @RequestMapping(value = "/success", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('company/get')")
    public String success() {
        return "success";
    }


    /**
     * 该用户没有此权限
     * @param
     * @return
     */
    @RequestMapping(value = "/fail", method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('aaaaaaa')")
    public String fail() {
        return "fail";
    }


    /**
     * 该接口放行,已经在resourceconfig中配置，不做限制
     * @param
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public String get() {
        return "success";
    }
}
