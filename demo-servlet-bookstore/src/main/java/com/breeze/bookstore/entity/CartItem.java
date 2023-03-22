package com.breeze.bookstore.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车不同商品条目
 *
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 10:37
 */
@Data
public class CartItem {
    /**
     * 购物车商品
     */
    private Ebook ebook;

    /**
     * 购物车商品数量
     */
    private int count;

    /**
     * 购物车单个条目价格小计
     * 二进制金额问题
     *
     * @return
     */
    public double getSubtotal() {
        BigDecimal b1 = new BigDecimal(ebook.getPrice());
        BigDecimal b2 = new BigDecimal(count);
        return b1.multiply(b2).doubleValue();
    }

}
