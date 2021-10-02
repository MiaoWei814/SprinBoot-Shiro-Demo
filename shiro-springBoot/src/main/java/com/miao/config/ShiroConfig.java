package com.miao.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;

/**
 * @program: sprinBoot-Shiro-demo
 * @description:
 * @author: MiaoWei
 * @create: 2021-10-02 17:10
 **/
@Configuration  //声明为配置类
public class ShiroConfig {
    //我们之前说Shiro有三大核心:Subject、SecurityManager、Realm对象
    //创建ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager shiroFilterFactoryBean){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //关联SecurityManager对象,设置安全管理器
        bean.setSecurityManager(shiroFilterFactoryBean);
        //添加shiro的内置过滤器
        /*
            常用过滤器有如下几个:
            anon:无需认证就可以访问
            authc:必须认证了才可以访问
            user:如果使用了记住我功能就可以直接访问
            perms:拥有某个资源权限才可以访问
            role:拥有某个角色权限才可以访问
         */
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        //必须授权我们才能进行访问,正常情况下,没有授权就会跳转到未授权的页面
        map.put("/user/add", "perms[user:add]"); //进这个页面的用户必须用于字符串user:add这个才会表示有权限
        map.put("/user/update", "perms[user:update]"); //进这个页面的用户必须用于字符串user:update这个才会表示有权限

        //对以下路径进行认证才可以访问,进行拦截!
        map.put("/user/*", "authc");
        bean.setFilterChainDefinitionMap(map);

        //拦截跳转指定登录页
        bean.setLoginUrl("/toLogin");
        //设置未授权的页面
        bean.setUnauthorizedUrl("/noAuth");

        return bean;
    }





    //创建DefaultwebSecurityManager---对应了SecurityManager
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        //关联Realm对象
//        manager.setRealm(userRealm()); 踩坑:不能直接填下面的自定义Realm对象,因为下面对象是已经被spring接管了,所以我们需要以spring的方式进行获取
        manager.setRealm(userRealm);
        

        return manager;
    }
    //创建Realm对象  第一步   之所以说先创建Realm是因为SecurityManager需要它
    @Bean //默认为方法名
    public UserRealm userRealm(){
        return new UserRealm();
    }

    @Bean
    //配置ShiroDialect：方言，用于 thymeleaf 和 shiro 标签配合使用
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
