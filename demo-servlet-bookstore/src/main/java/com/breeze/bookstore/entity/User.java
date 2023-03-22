package com.breeze.bookstore.entity;

import lombok.Data;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 15:36
 */
@Data
public class User {
    private String uid;
    private String username;
    private String password;
    private String email;
    /**
     * 激活码
     */
    private String code;
    /**
     * 用户登录状态
     */
    private boolean state;
}
