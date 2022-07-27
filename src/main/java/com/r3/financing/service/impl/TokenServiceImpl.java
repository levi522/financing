package com.r3.financing.service.impl;

import com.r3.financing.bean.User;
import com.r3.financing.service.TokenService;
import com.r3.financing.util.DigestUtil;
import com.r3.financing.util.RedisAPI;
import com.r3.financing.util.ValidationUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sam on 2018/4/23.
 */
@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    @Resource
    private RedisAPI redisAPI;

    @Resource
    private ValidationUtil validationUtil;

    //token:客户端标识-USERCODE-USERID-CREATIONDATE-RONDEM[6位]
    @Override
    public String generateToken(User user) {
        StringBuffer sbToken = new StringBuffer("token:");
        sbToken.append("PC-");
        //userCode
        String md5UserCode = DigestUtil.hmacSign(user.getUserCode());
        sbToken.append(md5UserCode + "-");
        //userid
        sbToken.append(user.getId() + "-");
        //createTime
        sbToken.append(new SimpleDateFormat("yyyyMMddHHmmss")//
                .format(new Date()));
        //返回拼接结果
        return sbToken.toString();
    }

    @Override
    public void saveToken(String token, User user) {
        String itripUserJson = user.toString();
        if (token.indexOf("LI-") != -1)
            redisAPI.set(token, itripUserJson, 60 * 60 * 2);
    }

    @Override
    public Boolean validateToken(String token){
        //判断是否存在
        if (!redisAPI.exists(token))
            return false;
        return true;
    }

    @Override
    public void removeToken(String token) {
        if (redisAPI.exists(token))
            redisAPI.del(token);
    }



    @Override
    public String retoken(String token) throws Exception {
        //1、验证Token是否有效
        if (!validateToken(token))
            throw new Exception("未知的token或 token已过期");
        // 2、能不能够置换,是否处在保护期
        //时间 1小时
        //token  index3
        try {
            //构建时间
            long genTime = new SimpleDateFormat("yyyyMMddHHmmss")//
                    .parse(token.split("-")[3]).getTime();
            //当前时间
            long currTime = new Date().getTime();
            if (token.split("-")[0].equals("PC")) {
                //当前时间 - token构建时间 > 保护期   可以置换
                if (currTime - genTime < TOKEN_PROTECTION_PERIOD_PC) {
                    //禁止替换token
                    throw new Exception("token正处于保护期内，禁止替换");
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 3、进行置换（兼容移动端和PC端）
        Long ttl = redisAPI.ttl(token);
        if (ttl > 0 || ttl == -1) {
            //获取用户对象
            User user = validationUtil.getUser(token);
            //进行替换,生成新的token
            String newToken = generateToken(user);
            // 4、老的token延迟过期
            redisAPI.set(token, user.toString(), 60 * 3);
            // 5、新的Token保存到Redis中
            saveToken(newToken, user);
            return newToken;
        } else {
            throw new Exception("未知的token或 token已过期");
        }
    }


}
