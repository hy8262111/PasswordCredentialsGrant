package com.xinao.service.impl;

import com.xinao.entity.AuthToken;
import com.xinao.entity.LoginResult;
import com.xinao.service.AuthService;
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
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Override
    public AuthToken login(LoginResult loginResult) {
        String userName = loginResult.getUserName();
        String password = loginResult.getPassword();
        String clientId = loginResult.getClientId();
        String clientSecret = loginResult.getClientSecret();
        AuthToken authToken = applyToken(userName, password, clientId, clientSecret);
        if(null == authToken){
            //todo 申请令牌失败
        }
        return authToken;
    }


    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {

        //采用客户端负载均衡，从注册中心获取认证服务的ip 和端口
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-auth");
        URI uri = serviceInstance.getUri();
        String authUrl =uri+ "/oauth/token";

        //请求的内容分两部分
        //1、header信息，包括了http basic认证信息
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        String httpbasic = httpbasic(clientId, clientSecret);
        //"Basic WGNXZWJBcHA6WGNXZWJBcHA="
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
        String refreshToken = (String) tokenInfo.get("refresh_token");
        String tokenType = (String) tokenInfo.get("token_type");
        Integer expiresIn = (Integer) tokenInfo.get("expires_in");
        String scope = (String) tokenInfo.get("scope");
        if (StringUtils.isEmpty(accessToken) || StringUtils.isEmpty(refreshToken)) {
            String code = (String )tokenInfo.get("code");
            if("401".equals(code)){
                //todo 抛出异常
            }else{
                //todo
            }
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(accessToken);
        authToken.setRefreshToken(refreshToken);
        authToken.setScope(scope);
        authToken.setExpiresIn(expiresIn);
        authToken.setTokenType(tokenType);
        return authToken;
    }


    //拼接http basic认证串
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}