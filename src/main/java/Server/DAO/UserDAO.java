package Server.DAO;

import Server.pojo.User;

/**
 * @author blue
 * @date 2023/1/12 14:34
 **/
public interface UserDAO {
    /** 用户登录方法
     * @param account 用户名
     * @param pwd 密码
     * @return 返回一个用户
     */
     User login(String account,String pwd);

    /** 新用户注册方法
     * @return 布尔值判断注册是否成功
     */
     boolean register();

}
