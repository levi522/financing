package com.r3.financing.bean;

import lombok.Data;

@Data
public class UserRegister {

    private String userCode;

    private String password;

    private String email;

    private Integer role;
}
