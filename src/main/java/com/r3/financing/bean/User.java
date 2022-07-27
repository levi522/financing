package com.r3.financing.bean;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class User {

    private Integer id;

    private String userCode;

    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdDate;

    private String token;

    private String email;

    private Integer status;

    private Integer role;
}
