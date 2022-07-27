package com.r3.financing.service.impl;

import com.r3.financing.bean.ResetPassword;
import com.r3.financing.dao.ResetPasswordMapper;
import com.r3.financing.service.ResetPasswordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ResetPasswordServiceImpl implements ResetPasswordService {

    @Resource
    private ResetPasswordMapper resetPasswordMapper;

    @Override
    public Integer add(ResetPassword resetPassword) {
        return resetPasswordMapper.add(resetPassword);
    }

    @Override
    public Integer queryResetPasswordCountByUserCode(String userCode) {
        return resetPasswordMapper.queryResetPasswordCountByUserCode(userCode);
    }
}
