package com.r3.financing.dao;

import com.r3.financing.bean.ResetPassword;

public interface ResetPasswordMapper {

    Integer add(ResetPassword resetPassword);

    Integer queryResetPasswordCountByUserCode(String userCode);
}
