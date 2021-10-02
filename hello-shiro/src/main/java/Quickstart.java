import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单的快速使用告诉你如何使用Shiro的API
 * Simple Quickstart application showing how to use Shiro's API.
 */
public class Quickstart {
    //使用日志门面
    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);

    public static void main(String[] args) {

        // The easiest way to create a Shiro SecurityManager with configured
        // realms, users, roles and permissions is to use the simple INI config.
        // We'll do that by using a factory that can ingest a .ini  file and
        // return a SecurityManager instance:
    /*
        翻译为:一个简单的案例告诉你如何去创建Shiro的安全管理,通过配置一些realms,用户,角色和一些权限在简单的ini配置文件中,
        我们可以通过使用这种工厂去创建SecurityManager的实例
    */

        // Use the shiro.ini file at the root of the classpath
        // (file: and url: prefixes load from files and urls  respectively):
    /*
        翻译:使用这Shiro.ini在我们根目录下读取
     */
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();

        // for this simple example quickstart, make the SecurityManager
        // accessible as a JVM singleton. Most applications wouldn't  do this
        // and instead rely on their container configuration or  web.xml for
        // webapps. That is outside the scope of this simple quickstart, so
        // we'll just do the bare minimum so you can continue to get a feel
        // for things.
    /*
        翻译:对于当前一个简单的快速开始,设置SecurityManager为JVM单例,只有一个全局唯一的对象;大多数的应用我们不需要这么做,大多数的时候
        我们会将其配置在web.xml中或者webapps中
     */
        SecurityUtils.setSecurityManager(securityManager);
        //------------------------------------------------下面是重点------------------------------------------------------------------

        //获取当前的用户对象subject
        Subject currentUser = SecurityUtils.getSubject();
        //通过当前页用户拿到session,这里并不是http的session而是shiro的session
        Session session = currentUser.getSession();
        //往session设置值,key为somekey,value为aValue
        session.setAttribute("someKey", "aValue");
        //从当前session中获取key
        String value = (String) session.getAttribute("someKey");
        //这段代码就是告诉我们可以存值取值
        if (value.equals("aValue")) {
            log.info("Retrieved the correct value! [" + value + "]");
        }

        //测试当前用户是否被认证-通过的话执行里面的方法
        if (!currentUser.isAuthenticated()) {
            //Token:令牌; 根据这个用户的账号和密码生成一个令牌---这个不是从ini读取,而是随机读取的
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            //设置记住我
            token.setRememberMe(true);
            try {
                //执行登录操作
                currentUser.login(token);
                //捕获未知的账户异常,比如说这个用户名不存在就会报错
            } catch (UnknownAccountException uae) {
                log.info("There is no user with username of " + token.getPrincipal());
                //捕获密码不对
            } catch (IncorrectCredentialsException ice) {
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
                //捕获账户被锁定了,比如五次密码都不对,就会把账户给锁了
            } catch (LockedAccountException lae) {
                log.info("The account for username " + token.getPrincipal() + " is locked. " + "Please contact your administrator to unlock it.");

            }
            //认证异常-上面都是异常,这个才是最大异常
            catch (AuthenticationException ae) {
                //unexpected condition? error?
            }
        }

        //打印用户信息登录成功的信息
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
        //测试角色
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }
        //检测你是否有什么权限-粗粒度的判断,
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring. Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }
        //检测你是否拥有更高级别的权限-细粒度的判断
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with  license plate(id) 'eagle5'. " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago !");
        }
        //注销
        currentUser.logout();
        //结束系统
        System.exit(0);
    }
}