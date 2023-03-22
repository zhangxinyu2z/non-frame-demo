package com.breeze.bookstore.service;

import cn.itcast.jdbc.JdbcUtils;
import com.breeze.bookstore.dao.OrderDao;
import com.breeze.bookstore.entity.Order;
import com.breeze.bookstore.exception.OrderException;

import java.sql.SQLException;
import java.util.List;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 13:03
 */
public class OrderService {
    private OrderDao orderDao = new OrderDao();


    /**
     * 添加订单，订单和订单项是一起的
     */
    public void add(Order order) {
        try {
            // start transaction
            JdbcUtils.beginTransaction();
            orderDao.addOrder(order);
            orderDao.addOrderItemList(order.getOrderItemList());
            // commit transaction
            JdbcUtils.commitTransaction();
        } catch (Exception e) {
            // rollback transaction
            try {
                JdbcUtils.rollbackTransaction();
            } catch (SQLException throwables) {

            }
            // 事务回滚了，要提示导致事务回顾的原因
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过用户id查询所有订单
     */
    public List<Order> queryOrderListByUid(String uid) {
        return orderDao.queryOrderListByUid(uid);
    }

    /**
     * 查询单个订单
     */
    public Order load(String oid) {
        return orderDao.load(oid);
    }

    /**
     * 确认订单
     * @param oid
     * @throws OrderException
     */
    public void confirm(String oid) throws OrderException {
        int orderState = orderDao.getOrderState(oid);
        if(orderState != 3) {
            throw new OrderException("订单确认失败！请勿非法操作！");
        }
        // 状态为4，交易成功
        orderDao.updateOrderState(oid,4);
    }
}
