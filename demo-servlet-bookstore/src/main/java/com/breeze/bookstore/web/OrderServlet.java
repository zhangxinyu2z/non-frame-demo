package com.breeze.bookstore.web;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.breeze.bookstore.entity.CartItem;
import com.breeze.bookstore.entity.Order;
import com.breeze.bookstore.entity.OrderItem;
import com.breeze.bookstore.entity.ShoppingCart;
import com.breeze.bookstore.entity.User;
import com.breeze.bookstore.exception.OrderException;
import com.breeze.bookstore.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 13:04
 */
@WebServlet("/order")
public class OrderServlet extends BaseServlet {
    private OrderService orderService = new OrderService();

    /**
     * 提交订单
     */
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShoppingCart cart = (ShoppingCart) request.getSession().getAttribute("cart");
        // 创建一条订单
        Order order = new Order();
        order.setOid(CommonUtils.uuid());
        order.setOrdertime(new Date());
        order.setState(1);
        User user = (User) request.getSession().getAttribute("session_user");
        order.setOwner(user);
        order.setTotal(cart.getTotal());

        List<OrderItem> orderItemList = new ArrayList<>();
        // 从购物车的每个条目中得到订单项所需信息
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            // 设置订单项的id
            orderItem.setIid(CommonUtils.uuid());
            // 设置订单项的商品价格小计
            orderItem.setSubtotal(cartItem.getSubtotal());
            orderItem.setCount(cartItem.getCount());
            orderItem.setEbook(cartItem.getEbook());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
        }
        // 订单中添加所有的订单条目
        order.setOrderItemList(orderItemList);
        // 清空购物车
        cart.clear();
        // 添加订单（持久化）
        orderService.add(order);
        // 保存到request中，转发
        request.setAttribute("order", order);
        return "f:/jsps/order/desc.jsp";
    }

    /**
     * 查看当前用户的所有订单
     */
    public String query(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("session_user");
        List<Order> orderList = orderService.queryOrderListByUid(user.getUid());
        request.setAttribute("orderList", orderList);
        return "f:/jsps/order/list.jsp";
    }

    /**
     * 付款的时候查看单个订单
     */
    public String load(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        Order order = orderService.load(oid);
        request.setAttribute("order", order);
        return "f:/jsps/order/desc.jsp";
    }

    /**
     * 确认收货
     */
    public String confirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String oid = request.getParameter("oid");
        try {
            orderService.confirm(oid);
            request.setAttribute("msg", "确认收货成功！");
        } catch (OrderException e) {
            request.setAttribute("msg", e.getMessage());
        }
        return "f:/jsps/msg.jsp";
    }


}

