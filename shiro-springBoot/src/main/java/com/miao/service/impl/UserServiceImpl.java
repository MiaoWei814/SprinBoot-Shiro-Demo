package com.miao.service.impl;

import com.miao.mapper.UserMapper;
import com.miao.pojo.User;
import com.miao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sprinBoot-Shiro-demo
 * @description:
 * @author: MiaoWei
 * @create: 2021-10-02 21:11
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByName(String name) {
        return userMapper.queryUserByName(name);
    }
}
