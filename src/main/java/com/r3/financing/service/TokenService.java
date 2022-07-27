package com.r3.financing.service;


import com.r3.financing.bean.User;

/**
 * Created by sam on 2018/5/16.
 */
public interface TokenService {

    public static final long TOKEN_PROTECTION_PERIOD_PC = 60 * 60 * 1000;
    public static final long TOKEN_PROTECTION_PERIOD_MOBILE = 60 * 60 * 1000;

    //客户端标识-USERCODE-USERID-CREATIONDATE-RONDEM[6位]
    //构建Token
    public String generateToken(User user);

    //保存Token
    public void saveToken(String token, User user);

    //验证Token是否有效
    public Boolean validateToken(String token);

    //删除Token
    public void removeToken(String token);

    //重置token
    public String retoken(String token) throws Exception;

}
