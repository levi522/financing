package com.r3.financing.controller;

import com.r3.financing.bean.JwtRequest;
import com.r3.financing.bean.User;
import com.r3.financing.bean.UserRegister;
import com.r3.financing.config.JwtTokenUtil;
import com.r3.financing.service.RegisterCodeService;
import com.r3.financing.service.UserService;
import com.r3.financing.util.Dto;
import com.r3.financing.util.DtoUtil;
import com.r3.financing.util.MailUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "user api")
@RequestMapping("/user")
@RestController
@CrossOrigin
public class UserController {

    @Resource
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private MailUtils mailUtils;

    @Resource
    private RegisterCodeService registerCodeService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {
        User userByUserCode = userService.getUserByUserCode(authenticationRequest.getUserCode());
        if(userByUserCode==null){
            return ResponseEntity.ok(DtoUtil.returnFail("用户名没有找到","100001"));
        }else {
            if(userByUserCode.getStatus() == 0){
                return ResponseEntity.ok(DtoUtil.returnFail("用户未激活","100002"));
            }
            PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
            //String password= DigestUtil.hmacSign(authenticationRequest.getPassword());
            if(!passwordEncoder.matches(authenticationRequest.getPassword(),userByUserCode.getPassword())){
                return ResponseEntity.ok(DtoUtil.returnFail("密码不匹配","100002"));
            }
        }
        User authenticateUser=authenticate(authenticationRequest.getUserCode(), authenticationRequest.getPassword());
        //JwtUserDetailsService.
        //userDetailsService.setPassword(authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUserCode());
        final String token = jwtTokenUtil.generateToken(userDetails);
        authenticateUser.setToken(token);
        return ResponseEntity.ok(DtoUtil.returnDataSuccess(authenticateUser,"登录成功"));
    }

    private User authenticate(String username, String password) throws Exception {
        User user;
        try {
            user = userService.login(username, password);
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            e.printStackTrace();
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        return user;
    }

    @RequestMapping(value = "register", method = RequestMethod.POST,
            produces = "application/json")
    @ResponseBody
    public Dto register(@RequestBody UserRegister userRegister) throws Exception {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        User user = new User();
        user.setUserCode(userRegister.getUserCode());
        user.setPassword(userRegister.getPassword());
        user.setEmail(userRegister.getEmail());
        user.setRole(userRegister.getRole());
        user.setCreatedDate(sdf.parse(sdf.format(date)));
        String password = user.getPassword();
        try {
            if(userService.getUserByEmail(user.getEmail()) != null){
                return DtoUtil.returnFail("邮箱已存在", "");
            }
            if(userService.getUserByUserCode(user.getUserCode())!=null){
                return DtoUtil.returnFail("用户名已存在", "");
            }else{
                if(user==null){
                    return DtoUtil.returnFail("user is null", "");
                }
                if (userService.registerUser(user)) {
//                    System.out.println("新增用户信息成功");
//                    //createWallet.addPeerOrgUser(COMPOSE_PROJECT_NAME,caIndex,trackUser.getPassword(),orgIndex,trackUser.getUserName());
//                    //ChainCode chainCode=new ChainCode();
//                    //chainCode.setBalance(COMPOSE_PROJECT_NAME,trackUser.getUserName(),1000,channelName,chaincodeName,"setBalance");
//                    //将密码暂时设为原密码 用于免登录
//                    User loginUser = userService.login(user.getUserCode(), password);
//                    System.out.println("自注册用户免登录");
//                    System.out.println("登录成功");
//                    final UserDetails userDetails = userDetailsService
//                            .loadUserByUsername(loginUser.getUserCode());
//
//                    final String token = jwtTokenUtil.generateToken(userDetails);
//                    loginUser.setToken(token);

                    return DtoUtil.returnDataSuccess("","注册成功");
                }
            }
        } catch (Exception e) {
            throw new Exception();
        }
        System.out.println("运行出错");
        return DtoUtil.returnFail("未知错误", "");
    }

    @RequestMapping(value = "/sendResetPasswordMail", method = RequestMethod.GET)
    public Dto sendResetPasswordMail(@RequestParam String email) {
        try{
            User user = userService.getUserByEmail(email);
            if(user == null){
                return DtoUtil.returnFail("邮箱不存在","");
            }
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(user.getUserCode());
            final String token = jwtTokenUtil.generateToken(userDetails);
            String subject="修改密码";
            String from = "levi522@163.com";
            String to = email;
            String[] cc = {};
            String[] bcc = {};
            Map<String, Object> data = new HashMap<>();
            data.put("date","111111");
            data.put("yearTips","222222");
            data.put("lunarCalendar","333333");
            data.put("typeDes","4444444");

            Map<String, Object> data1 = new HashMap<>();
            data1.put("dsResult", "dsResult");
            String templatePath = "mail.html";
            mailUtils.sendThymeleafMail(subject,from,to,cc,bcc,data1,templatePath);
            user.setToken(token);
            user.setPassword("");
            return DtoUtil.returnSuccess("",user);
        }catch (Exception e){
            return DtoUtil.returnFail("异常","");
        }

    }

    @RequestMapping(value = "/verifyRegisterCode", method = RequestMethod.GET)
    public Dto verifyRegisterCode(@RequestParam String registerCode) throws Exception {
        Integer count = registerCodeService.queryRegisterCodeByCodeActive(registerCode);
        if(count > 0){
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername("root");
            final String token = jwtTokenUtil.generateToken(userDetails);
            return DtoUtil.returnSuccess("",token);
        }else{
            return DtoUtil.returnFail("错误的验证码或已失效","");

        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public Dto resetPassword(@RequestBody User user){
        try{
            User userNew = userService.getUserByUserCode(user.getUserCode());
            if(userNew.getStatus() == 0){
                return DtoUtil.returnSuccess("用户未激活","");
            }
            userNew.setPassword("");
            if(userNew != null){
                if(userService.resetPassword(user.getUserCode(),user.getPassword())){
                    final UserDetails userDetails = userDetailsService
                            .loadUserByUsername(userNew.getUserCode());
                    final String token = jwtTokenUtil.generateToken(userDetails);
                    userNew.setToken(token);
                    return DtoUtil.returnSuccess("",userNew);
                }else{
                    return DtoUtil.returnFail("修改密码失败","");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return DtoUtil.returnFail("修改密码异常","");
    }

}
