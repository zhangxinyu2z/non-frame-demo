package com.breeze.bookstore.entity;

import lombok.Data;

/**
 * 订单条目类
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 12:58
 */
@Data
public class OrderItem {
    /**
     * 订单项id
     */
    private String iid;
    /**
     * 订单中商品数量
     */
    private int count;
    /**
     * 小计
     */
    private double subtotal;
    /**
     * 订单项所属订单
     */
    private Order order;
    /**
     * 订单项中商品
     */
    private Ebook ebook;

}
