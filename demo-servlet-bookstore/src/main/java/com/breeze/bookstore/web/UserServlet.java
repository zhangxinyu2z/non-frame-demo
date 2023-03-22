package com.breeze.bookstore.web;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;
import com.breeze.bookstore.entity.ShoppingCart;
import com.breeze.bookstore.entity.User;
import com.breeze.bookstore.exception.UserException;
import com.breeze.bookstore.service.UserService;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 15:48
 */
@WebServlet("/UserServlet")
public class UserServlet extends BaseServlet {
    private UserService userService = new UserService();

    /**
     * 用户登录
     * 如果查询到用户信息， 重定向到主页
     * 没有，则给出提示信息到登录界面
     */
    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        User form = CommonUtils.toBean(request.getParameterMap(), User.class);
        try {
            User user = userService.login(form);
            request.getSession().setAttribute("session_user", user);
            // 给用户一辆购物车
            request.getSession().setAttribute("cart",new ShoppingCart());
            return "r:/index.jsp";
        } catch (UserException e) {
            request.setAttribute("msg", e.getMessage());
            request.setAttribute("form", form);
            return "f:/jsps/user/login.jsp";
        }
    }


    /**
     * 用户注册
     * 获取用户信息，进行表单校验，注册
     * 1、封装表单数据
     * 2、生成用户id和激活码
     * 3、输入校验   使用Map保存，key为字段，value为错误信息
     * 4、调用userservice的regist方法
     * 5、发送激活右键
     * 6、注册成功
     */
    public String register(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        User form = CommonUtils.toBean(request.getParameterMap(), User.class);
        form.setUid(CommonUtils.uuid());
        form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
        Map<String, String> errors = new HashMap<>(16);
        String username = form.getUsername();
        if (username == null || username.trim().isEmpty()) {
            errors.put("username", "用户名不能为空");
        } else if (username.length() < 3 || username.length() > 10) {
            errors.put("username", "用户名长度在3~10个字符");
        }
        // 其它的就不判断了
        if (errors.size() > 0) {
            // 保存错误信息到request域中，保存form数据到request域中，回显，转发到注册界面
            request.setAttribute("errors", errors);
            request.setAttribute("form", form);
            return "f:/jsps/user/regist.jsp";
        }

        try {
            userService.register(form);
        } catch (UserException e) {
            request.setAttribute("msg", e.getMessage());
            request.setAttribute("form", form);
            return "f:/jsps/user/regist.jsp";
        }

        // 发送激活邮件
        sendActiveEmail(form);
        request.setAttribute("msg", "注册成功，请到邮箱激活");
        return "f:/jsps/msg.jsp";
    }

    /**
     * 用户退出
     */
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        request.getSession().invalidate();
        return "r:/index.jsp";
    }



    /**
     * 处理用户激活
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String activateEmail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        try {
            userService.activeEmail(code);
            request.setAttribute("msg", "active success");
        } catch (UserException e) {
            request.setAttribute("msg", e.getMessage());
        }
        return "f:/jsps/msg.jsp";
    }

    private void sendActiveEmail(User form) throws IOException {
        // 发邮件
        Properties props = new Properties();
        props.load(this.getClass().getClassLoader()
                .getResourceAsStream("email_template.properties"));
        //获取服务器主机
        String host = props.getProperty("host");
        //获取用户名
        String uname = props.getProperty("uname");
        //获取密码
        String pwd = props.getProperty("pwd");
        //获取发件人
        String from = props.getProperty("from");
        //获取收件人
        String to = form.getEmail();
        //获取主题
        String subject = props.getProperty("subject");
        //获取邮件内容
        String content = props.getProperty("content");
        //替换占位符，如果有多个，按次序
        content = MessageFormat.format(content, form.getCode());
        //得到session
        Session session = MailUtils.createSession(host, uname, pwd);
        //创建邮件对象
        Mail mail = new Mail(from, to, subject, content);
        try {
            //发邮件
            MailUtils.send(session, mail);
        } catch (MessagingException e) {
        }
    }
}
