package com.r3.financing.service;


import com.r3.financing.bean.RegisterCode;

public interface RegisterCodeService {

    RegisterCode queryRegisterCode();

    Integer updateStatus(Integer id);

    Integer queryRegisterCodeByCode(String code);

    Integer queryRegisterCodeByCodeActive(String code);
}
