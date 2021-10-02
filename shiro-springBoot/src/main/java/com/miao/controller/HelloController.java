package com.miao.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: sprinBoot-Shiro-demo
 * @description:
 * @author: MiaoWei
 * @create: 2021-10-02 16:31
 **/
@Controller
public class HelloController {

    @RequestMapping({"/","/index"})
    public String hello(Model model){
        model.addAttribute("msg", "hello,Shiro");
        return "index";
    }

    @RequestMapping("/user/add")
    public String add() {
        return "add";
    }

    @RequestMapping("/user/update")
    public String update() {
        return "update";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param model    模型
     * @return {@link String}
     */
    @RequestMapping("/login")
    public String login(String username, String password,Model model) {
        //使用shiro,编写认证操作

        //1.获取subject
        Subject subject = SecurityUtils.getSubject();
        //2.封装用户的数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //执行登录的方法,只要没异常就代表登录成功!
        try{
            subject.login(token);
            return "index";
        }catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在");
            return "login";
        }catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码不存在!");
            return "login";
        }

    }

    @ResponseBody
    @RequestMapping("/noAuth")
    public String unauthorized(){
        return "未经授权,无法访问此页面";
    }
}
