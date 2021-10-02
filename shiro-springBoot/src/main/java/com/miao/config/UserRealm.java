package com.miao.config;

import com.miao.pojo.User;
import com.miao.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: sprinBoot-Shiro-demo
 * @description:
 * @author: MiaoWei
 * @create: 2021-10-02 17:17
 **/
//自定义的UserRealm 需要继承AuthorizingRealm这个类
public class UserRealm  extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了==>授权doGetAuthorizationInfo");
        //授予权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject(); //拿到当前对象
        User user = (User) subject.getPrincipal();//拿到User对象
        //添加权限
        info.addStringPermission(user.getPerms());
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了==>认证doGetAuthorizationInfo");

        //1.判断用户名是否正确
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        //查询数据库
        User user = userService.queryUserByName(token.getUsername());

        if (user == null) {
            //用户名不存在
            //这里我们只需抛出null,shiro底层就会抛出UnknownAccountException
            return null;
        }
        //放置session,注意这里session不是我们之前HttpSession,这里的session是一个接口!
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute("loginUser", user);
        //2.验证密码
        //这里我们不需要去验证密码,因为Shiro底层就会帮我们自动验证,因为密码明文传输容易遭到密码泄漏
        //这里我们只需要将密码扔给Shiro就可以了!
        return new SimpleAuthenticationInfo(user, user.getPwd(), "");
    }
}
