package com.breeze.bookstore.service;

import com.breeze.bookstore.dao.UserDao;
import com.breeze.bookstore.entity.User;
import com.breeze.bookstore.exception.UserException;

/**
 * @author dhjy
 * @version v1.0
 * @date created in 2021-05-24 15:48
 */
public class UserService {
    private UserDao userDao = new UserDao();

    /**
     * 用户注册
     * 1、根据用户名查询用户，如果查到，说明被被注册过，抛出自定义异常
     * 2、根据邮箱查询，查看邮箱是否注册过
     * 3、添加用户信息
     *
     * @param form
     * @throws UserException
     */
    public void register(User form) throws UserException {
        User user = userDao.findUserByName(form.getUsername());
        if (user != null) {
            throw new UserException("用户名已被注册");
        }
        user = userDao.findUserbyEmail(form.getEmail());
        if (user != null) {
            throw new UserException("Email已被注册");
        }
        userDao.add(form);
    }

    /**
     * 用户激活
     * 1、根据激活码查询是否存在该用户，如果没有查到，说明该用户没有注册成功
     * 2、对用户状态进行判断，如果及已经激活了，抛出提示。
     * 3、更新用户注册状态
     *
     * @param code
     * @throws UserException
     */
    public void activeEmail(String code) throws UserException {
        User user = userDao.findUserbyCode(code);
        if (user == null) {
            throw new UserException("激活码无效，请重新激活！");
        }
        if (user.isState()) {
            throw new UserException("您已经激活过了，无需重复激活！");
        }
        userDao.updateUserState(user.getUid(), true);
    }

    /**
     * 通过username查询用户是否存在，存在抛出错误提示
     * 比较密码是否相同，不同抛出错误提示
     * 查看用户激活状态，提示信息
     * 返回user
     * @param form
     * @return
     */
    public User login(User form) throws UserException {
        User user = userDao.findUserByName(form.getUsername());
        if (user == null) {
            throw new UserException("用户或密码错误");
        }
        if(!user.getPassword().equals(form.getPassword())) {
            throw new UserException("用户或密码错误");
        }
        if(!user.isState()) {
            throw new UserException("您的账户还没有激活");
        }
        return user;
    }
}
