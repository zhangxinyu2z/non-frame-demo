package com.breeze.bookstore.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单类
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 12:55
 */
@Data
public class Order {
    private String oid;
    /**
     * 下单时间
     */
    private Date ordertime;
    private double total;
    /**
     * 1、未付款
     * 2、已付款，未发货
     * 3、已发货，未确认收货
     * 4、确认收货，交易成功
     */
    private int state;
    /**
     * 订单归属用户
     */
    private User owner;
    private String address;

    /**
     * 当前订单所有条目
     */
    private List<OrderItem> orderItemList;

}
