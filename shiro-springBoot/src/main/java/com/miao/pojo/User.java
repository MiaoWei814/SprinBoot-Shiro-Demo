package com.miao.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sprinBoot-Shiro-demo
 * @description:
 * @author: MiaoWei
 * @create: 2021-10-02 21:06
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String name;
    private String pwd;
    private String perms;
}
