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
     * @param account 新用户账号
     * @param password 新用户密码
     * @param telephone 新用户手机号码
     * @return 字符串方便服务器发送
     */
     String register(String account,String password,String telephone);

}
