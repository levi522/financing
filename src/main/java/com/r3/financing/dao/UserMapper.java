package com.r3.financing.dao;

import com.r3.financing.bean.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    User getUserByUserCode(String userCode);

    Integer insertUser(User user);

    Integer updateUserPassword(@Param("userCode") String userCode,@Param("password") String password);

    User getUserByEmail(String email);
}
