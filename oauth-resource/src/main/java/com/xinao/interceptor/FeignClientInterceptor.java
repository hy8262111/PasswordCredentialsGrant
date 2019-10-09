/*
package com.xinao.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

*/
/**
 * @Author: houyong
 * @Date: 2019/9/24 11:26
 * @describe:微服务之前调用，相互传递token,该类放在common包中，不要在common中使用注入，谁用谁就注入
 /
public class FeignClientInterceptor implements RequestInterceptor {

    //当进行Feign 远程调用时此方法会执行
    @Override
    public void apply(RequestTemplate requestTemplate) {


        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取request
        HttpServletRequest request = servletRequestAttributes.getRequest();
        //获取头信息的所有key
        Enumeration<String> headerNames = request.getHeaderNames();
        if(headerNames!=null){
            while(headerNames.hasMoreElements()){
                //获取头信息的 key
                String header_key = headerNames.nextElement();

                if(header_key.equals("authorization")){
                    String token = request.getHeader("authorization");
                    //将Authorization头信息传到下级目标服务
                    requestTemplate.header("authorization",token);
                }
            }
        }


    /**
     * 微服务之前调用，传递access_token
    *//*

    */
/*@Bean
    public FeignClientInterceptor feignClientInterceptor(){
        return new FeignClientInterceptor();
    }*//*

}
*/
