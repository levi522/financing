package com.r3.financing.service.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.r3.financing.bean.User;
import com.r3.financing.dao.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user=userMapper.getUserByUserCode(username);
            if (user!=null) {
                Collection list = new ArrayList<>();
                return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
