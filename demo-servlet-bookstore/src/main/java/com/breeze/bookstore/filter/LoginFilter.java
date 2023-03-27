package com.breeze.bookstore.filter;

import com.breeze.bookstore.entity.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-26 18:44
 */
@WebFilter(filterName = "LoginFilter",urlPatterns = {"/ShoppingCartServlet","/jsps/cart/*","/jsps/order/*","/OrderServlet"})
public class LoginFilter implements Filter {
    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        User user = (User) httpServletRequest.getSession().getAttribute("session_user");
        if (user != null) {
            chain.doFilter(req, resp);
        } else {
            httpServletRequest.setAttribute("msg","您还没有登录");
            httpServletRequest.getRequestDispatcher("/jsps/user/login.jsp").forward(httpServletRequest,resp);
        }

    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
