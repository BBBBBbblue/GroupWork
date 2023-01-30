package Server.DAO;

import Server.pojo.CartsDetail;
import Server.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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

    /** 用户修改信息方法
     * @param nickname 用户昵称
     * @param email 用户邮箱
     * @param account 用户名
     * @return 字符串方便服务器发送
     */
     String update(String nickname,String email,String account);

    /** 用户充值方法
     * @param account 用户账户
     * @param money  充值金额
     * @return 字符串信息
     */
     String charge(String account,String money);

    /** 用户消费方法
     * @param account 用户账户
     * @param money 消费金额
     * @return 字符串信息，方便返回
     */
     String pay(String account,float money);

    /** 用户查看购物车
     * @param id 用户主键id
     * @return 购物车情况集合
     */
     HashMap<String, LinkedList<CartsDetail>> getCarts(int id);

}
