package com.r3.financing.util;

// import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.r3.financing.bean.User;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by sam on 2018/4/23.
 */
@Component
public class ValidationUtil {

    @Resource
    private RedisAPI redisAPI;

    /**
     * 通过Token获取用户信息
     *
     * @param token
     * @return
     */
    public User getUser(String token) {
        try {
            //判断token是否存在
            if (!redisAPI.exists(token))
            return null;
            String itripUserJson = redisAPI.get(token);
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            User user = gson.fromJson(itripUserJson, User.class);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
