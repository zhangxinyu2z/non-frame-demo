package com.breeze.bookstore.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.breeze.bookstore.entity.Ebook;
import com.breeze.bookstore.entity.Order;
import com.breeze.bookstore.entity.OrderItem;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-25 13:03
 */
public class OrderDao {
    private QueryRunner queryRunner = new TxQueryRunner();

    /**
     * 添加订单
     *
     * @param order
     */
    public void addOrder(Order order) {
        String sql = "insert into orders values (?,?,?,?,?,?)";
        Timestamp orderTime = new Timestamp(order.getOrdertime().getTime());
        Object[] params = {order.getOid(), orderTime, order.getTotal(), order.getState(),
                order.getOwner().getUid(), order.getAddress()};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 添加订单项目
     * 多个订单插入使用批处理，批处理需要的参数是二维数组
     * 二维数组看作是一张表，一维数组是每张表的一行数据。
     *
     * @param orderItemList
     */
    public void addOrderItemList(List<OrderItem> orderItemList) {
        String sql = "insert into orderitem values (?,?,?,?,?)";
        Object[][] params = new Object[orderItemList.size()][];
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            params[i] = new Object[]{orderItem.getIid(), orderItem.getCount(), orderItem.getSubtotal(),
                    orderItem.getOrder().getOid(), orderItem.getEbook().getBid()};
        }
        try {
            // execute batch
            queryRunner.batch(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询当前用户的所有订单
     */
    public List<Order> queryOrderListByUid(String uid) {
        String sql = "select * from orders where uid=?";
        try {
            List<Order> orderList = queryRunner.query(sql, new BeanListHandler<Order>(Order.class), uid);
            for (Order order : orderList) {
                queryOrderItems(order);
            }
            return orderList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询订单对应的所有订单项
     * 查订单项还要得到book的具体信息，所以查两张表
     * 使用beanlistHandler无法完成映射，所以使用MapListHandler得到表的所有信息
     * 表的一行是一个Map，每个Map保存着每列对应的值
     *
     * @param order
     */
    private void queryOrderItems(Order order) throws SQLException {
        String sql = "select * from orderitem i, book b where i.bid = b.bid and oid=?";
        List<Map<String, Object>> lineMapList = queryRunner.query(sql, new MapListHandler(), order.getOid());
        List<OrderItem> orderItemList = toOrderItemList(lineMapList);
        order.setOrderItemList(orderItemList);
    }

    /**
     * 查出来的数据有多行，把表的每一行的数据映射到每一个OrderItem中,用list集合来存储
     *
     * @param lineMapList 表的所有行数据
     * @return
     */
    private List<OrderItem> toOrderItemList(List<Map<String, Object>> lineMapList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (Map<String, Object> lineMap : lineMapList) {
            OrderItem orderItem = toOrderItem(lineMap);
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    /**
     * 将每行的数据，映射到一个OrderItem中
     *
     * @param lineMap 表的每行的数据
     * @return
     */
    private OrderItem toOrderItem(Map<String, Object> lineMap) {
        OrderItem orderItem = CommonUtils.toBean(lineMap, OrderItem.class);
        Ebook book = CommonUtils.toBean(lineMap, Ebook.class);
        orderItem.setEbook(book);
        return orderItem;
    }

    /**
     * 查询单个订单
     *
     * @param oid
     * @return
     */
    public Order load(String oid) {
        String sql = "select * from orders where oid=?";
        try {
            Order order = queryRunner.query(sql, new BeanHandler<Order>(Order.class), oid);
            queryOrderItems(order);
            return order;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据订单编号查询订单状态
     *
     * @param oid
     * @return
     */
    public int getOrderState(String oid) {
        String sql = "select state from orders where oid=?";
        try {
            Number number = (Number) queryRunner.query(sql, new ScalarHandler(), oid);
            return number.intValue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改订单状态
     *
     * @param oid
     */
    public void updateOrderState(String oid, int state) {
        String sql = "update orders set state=? where oid=?";
        try {
            queryRunner.update(sql, state, oid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
