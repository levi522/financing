package com.r3.financing.service;

import com.r3.financing.bean.ResetPassword;

public interface ResetPasswordService {

    Integer add(ResetPassword resetPassword);

    Integer queryResetPasswordCountByUserCode(String userCode);
}
