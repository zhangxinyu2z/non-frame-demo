package com.breeze.bookstore.web;

import cn.itcast.servlet.BaseServlet;
import com.breeze.bookstore.entity.CartItem;
import com.breeze.bookstore.entity.Ebook;
import com.breeze.bookstore.entity.ShoppingCart;
import com.breeze.bookstore.service.EbookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 11:16
 */
@WebServlet("/CartServlet")
public class CartServlet extends BaseServlet {
    /**
     * 添加购物车
     * 页面提交传递商品id和数量
     */
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
        int count = Integer.valueOf(request.getParameter("count"));
        String bid = request.getParameter("bid");
        Ebook book = new EbookService().lookBookInfo(bid);
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setCount(count);
        cart.add(cartItem);
        return "f:/jsps/cart/list.jsp";
    }

    /**
     * 删除购物车商品条目
     * 得到传递的bid
     */
    public String delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
        String bid = request.getParameter("bid");
        cart.delete(bid);
        return "f:/jsps/cart/list.jsp";
    }

    /**
     * 清空购物车
     */
    public String clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
        cart.clear();
        return "f:/jsps/cart/list.jsp";
    }
}
