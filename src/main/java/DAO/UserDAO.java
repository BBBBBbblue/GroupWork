package DAO;

import pojo.User;

/**
 * @author blue
 * @date 2023/1/12 14:34
 **/
public interface UserDAO {
    /** 用户登录方法
     * @return 返回一个用户
     */
     User login();

    /** 新用户注册方法
     * @return 布尔值判断注册是否成功
     */
     boolean register();

}
