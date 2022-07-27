package com.r3.financing.service.impl;

import com.r3.financing.bean.ResetPassword;
import com.r3.financing.bean.User;
import com.r3.financing.dao.ResetPasswordMapper;
import com.r3.financing.dao.UserMapper;
import com.r3.financing.service.TokenService;
import com.r3.financing.service.UserService;
import com.r3.financing.util.RedisAPI;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisAPI redisAPI;

    @Resource
    private ResetPasswordMapper resetPasswordMapper;

    @Override
    public User getUserByUserCode(String userCode) {
        return userMapper.getUserByUserCode(userCode);
    }

    @Override
    public Integer insertUser(User user) {
        return userMapper.insertUser(user);
    }


    //用户验证通过
    public User userLandTrue(User user, String userName) throws Exception {
        //获取dcepUser类型以保存token
        User userNew = userMapper.getUserByUserCode(userName);
        //生成token
        String token = tokenService.generateToken(userNew);
        //移除旧token
        tokenService.removeToken(token);
        //baocuntoken
        tokenService.saveToken(token, userNew);
/*
        logger.debug("id：" + dcepUser.getUserId() + ";昵称：" + dcepUser.getUserName() + "的token为：" + token);
*/
        //获取时间及规定格式
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);

        //修改登录时间
//        try {
//            trackUserMapper.updateLoginTime(user.getId().toString(),sdf.parse(time));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        //保存redis
        redisAPI.set("userLand:" + userNew.getUserCode(), time);
//        logger.debug("id：" + dcepUser.getUserId() + ";账号为：" + dcepUser.getUserCode() + "的redis为：" + time);
        //清除返回数据的密码
        user.setPassword("");
        user.setToken(token);
        return user;
    }

    @Override
    public User login(String userCode, String password) throws Exception {
        //通过账号和登陆标识获取用户
        User user=userMapper.getUserByUserCode(userCode);
        if (user==null){
            throw new Exception("用户不存在");
        }
        //加密用户输入的密码
        //String password = DigestUtil.hmacSign(userPassword);
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        //和数据库查出的用户加密密码对比
        if (passwordEncoder.matches(password,user.getPassword())) {

            return userLandTrue(user, userCode);
        } else {
            throw new Exception("密码错误");
        }
    }

    @Override
    public boolean registerUser(User user) throws Exception {
        try{
            PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

            String encodedPassword=passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setStatus(0);
            if(userMapper.insertUser(user) > 0){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new Exception("注册用户失败");
    }

    @Override
    public User getUserByEmail(String email) {
        return userMapper.getUserByEmail(email);
    }

    @Override
    public boolean resetPassword(String userCode, String password) throws Exception {
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

        String encodedPassword=passwordEncoder.encode(password);
        if(userMapper.updateUserPassword(userCode, encodedPassword) < 1){
            throw new Exception("修改密码失败");
        }
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setUserCode(userCode);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resetPassword.setCreatedDate(sdf.parse(sdf.format(date)));

        if(resetPasswordMapper.add(resetPassword) < 1){
            throw new Exception("添加修改密码失败");
        }
        return true;
    }
}
