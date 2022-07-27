package com.r3.financing.service.impl;

import com.r3.financing.bean.RegisterCode;
import com.r3.financing.dao.RegisterCodeMapper;
import com.r3.financing.service.RegisterCodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class RegisterCodeServiceImpl implements RegisterCodeService {

    @Resource
    private RegisterCodeMapper registerCodeMapper;


    @Override
    public RegisterCode queryRegisterCode() {
        return registerCodeMapper.queryRegisterCode();
    }



    @Override
    public Integer updateStatus(Integer id) {
        return registerCodeMapper.updateStatus(id);
    }

    @Override
    public Integer queryRegisterCodeByCode(String code) {
        return registerCodeMapper.queryRegisterCodeByCode(code);
    }

    @Override
    public Integer queryRegisterCodeByCodeActive(String code) {
        return registerCodeMapper.queryRegisterCodeByCodeActive(code);
    }
}
