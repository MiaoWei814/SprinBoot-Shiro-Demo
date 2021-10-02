package com.miao.service;

import com.miao.pojo.User;
import com.miao.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserServiceImpl userService;
    @Test
    void queryUserByName() {
        User miao = userService.queryUserByName("小威");
        System.out.println("miao = " + miao);
    }
}