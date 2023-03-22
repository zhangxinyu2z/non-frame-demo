package com.breeze.bookstore.dao;

import cn.itcast.jdbc.TxQueryRunner;
import com.breeze.bookstore.entity.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 15:41
 */
public class UserDao {
    private QueryRunner queryRunner = new TxQueryRunner();

    /**
     * 通过用户名查找用户信息
     *
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        String sql = "select * from tb_user where username=?";
        try {
            return queryRunner.query(sql, new BeanHandler<User>(User.class), username);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 校验邮箱是否存在
     *
     * @param email
     * @return
     */
    public User findUserbyEmail(String email) {
        String sql = "select * from tb_user where email=?";
        try {
            return queryRunner.query(sql, new BeanHandler<>(User.class), email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户注册，添加用户信息
     *
     * @param form
     */
    public void add(User form) {
        String sql = "insert into tb_user values (?,?,?,?,?,?)";
        Object[] params = {form.getUid(), form.getUsername(), form.getPassword(), form.getEmail(), form.getCode(),
                form.isState()};
        try {
            queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 通过激活码查找用户信息，进行激活码验证
     *
     * @param code
     * @return
     */
    public User findUserbyCode(String code) {
        String sql = "select * from tb_user where code=?";
        try {
            return queryRunner.query(sql, new BeanHandler<User>(User.class), code);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户激活成功，修改用户状态
     *
     * @param uid
     * @param state
     */
    public void updateUserState(String uid, boolean state) {
        String sql = "update tb_user set state=? where uid=?";
        try {
            queryRunner.update(sql, state, uid);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
