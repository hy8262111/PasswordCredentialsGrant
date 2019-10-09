package com.xinao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
/**
 * @Author: houyong
 * @Date: 2019/9/24 16:10
 * @describe: 该配置不用做任何事，就是为了用户调用时入参的参数不为null
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

}

