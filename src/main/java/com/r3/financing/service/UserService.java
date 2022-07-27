package com.r3.financing.service;

import com.r3.financing.bean.User;

public interface UserService {

    User getUserByUserCode(String userCode);

    Integer insertUser(User user);

    User login(String userCode,String password) throws Exception;

    boolean registerUser(User user) throws Exception;

    User getUserByEmail(String email);

    boolean resetPassword(String userCode,String password) throws Exception;

}
