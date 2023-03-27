package com.breeze.bookstore.entity;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 10:37
 */
public class ShoppingCart {
    /**
     * 购物车数据，使用商品id做key,商品条目为value
     */
    private Map<String, CartItem> items = new LinkedHashMap<>();

    /**
     * 添加条目到购物车
     * 如果购物车存在该商品，获取旧的条目，数量加1再替换
     * 如果不存在，添加新的条目
     */
    public void add(CartItem cartItem) {
        String bid = cartItem.getEbook().getBid();
        if (items.containsKey(bid)) {
            CartItem cartItem_old = items.get(bid);
            cartItem_old.setCount(cartItem_old.getCount() + 1);
            items.put(bid, cartItem_old);
        } else {
            items.put(bid, cartItem);
        }
    }

    /**
     * 清空购物车
     */
    public void clear() {
        items.clear();
    }

    /**
     * 删除单个条目
     *
     * @param bid
     */
    public void delete(String bid) {
        items.remove(bid);
    }

    /**
     * 获取所有的购物车条目
     *
     * @return
     */
    public Collection<CartItem> getCartItems() {
        return items.values();
    }

    /**
     * 购物车所有条目价格合计
     * 解决二进制运算溢出问题
     *
     * @return
     */
    public double getTotal() {
        BigDecimal cartTotal = new BigDecimal(0);
        for (CartItem cartItem : items.values()) {
            BigDecimal subTotal = new BigDecimal(cartItem.getSubtotal());
            cartTotal = cartTotal.add(subTotal);
        }
        return cartTotal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
