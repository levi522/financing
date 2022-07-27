package com.r3.financing.dao;


import com.r3.financing.bean.RegisterCode;

public interface RegisterCodeMapper {

    RegisterCode queryRegisterCode();

    Integer insertRegisterCode(RegisterCode register);

    Integer updateStatus(Integer id);

    Integer queryRegisterCodeByCode(String code);

    Integer queryRegisterCodeByCodeActive(String code);
}
