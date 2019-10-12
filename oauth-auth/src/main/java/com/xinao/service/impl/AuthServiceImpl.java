package com.xinao.service.impl;

import com.alibaba.fastjson.JSON;
import com.xinao.constance.ServiceNames;
import com.xinao.entity.LoginResult;
import com.xinao.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

/**
 * @Author: houyong
 * @Date: 2019/10/10 10:41
 * @describe
 */
@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    /**
     * 获取令牌
     * @param loginResult
     * @return
     */
    @Override
    public String login(LoginResult loginResult) {
        String userName = loginResult.getUserName();
        String password = loginResult.getPassword();
        String clientId = loginResult.getClientId();
        String clientSecret = loginResult.getClientSecret();
        String token = applyToken(userName, password, clientId, clientSecret);
        return token;
    }


    private String applyToken(String username, String password, String clientId, String clientSecret) {

        //采用客户端负载均衡，从注册中心获取认证服务的ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose(ServiceNames.AUTH_SERVICE_NAME);
        URI uri = serviceInstance.getUri();

        String authUrl = uri + "/oauth/token";
        logger.info("调用认证中心的客户端负载均衡地址，{}", authUrl);

        //请求的内容分两部分
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();

        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
        String httpbasic = httpbasic(clientId, clientSecret);
        logger.info("http basic认证信息，{}", httpbasic);
        headers.add("Authorization", httpbasic);

        //2、包括：grant_type、username、passowrd
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);

        HttpEntity<MultiValueMap<String, String>> multiValueMapHttpEntity = new HttpEntity<>(body, headers);

        //指定 restTemplate当遇到400或401响应时候也不要抛出异常，也要正常返回值
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                //当响应的值为400或401时候也要正常响应，不要抛出异常
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        //远程调用申请令牌
        ResponseEntity<Map> exchange = null;
        try {
            exchange = restTemplate.exchange(authUrl, HttpMethod.POST, multiValueMapHttpEntity, Map.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }

        // 返回的令牌信息
        Map tokenInfo = exchange.getBody();
        String accessToken = (String) tokenInfo.get("access_token");
        Integer expiresIn = (Integer) tokenInfo.get("expires_in");
        logger.info("令牌过期时间设置，{}", expiresIn);

        if (StringUtils.isEmpty(accessToken)) {
            //当用户不存在要响应“用户不存在”
            String errorDescription = (String) tokenInfo.get("error_description");
            if (null == errorDescription) {
                //todo 抛出异常 说明用户不存在
                System.out.println("账号不存在");
            } else if ("Bad credentials".equalsIgnoreCase(errorDescription)) {
                //todo 当密码错误也要解析密码错误的信息，响应到客户端
                System.out.println("密码错误");
            } else if (errorDescription.indexOf("Full authentication is required") >= 0) {
                //todo appid错误
                System.out.println("appId 或 appserect 错误");
            } else {
                //todo
            }
        }
        return accessToken;
    }


    /**
     * 拼接http basic认证串
     */
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
