package Server.DAO;

import Server.pojo.CartsDetail;
import Server.pojo.OrderDetail;
import Server.pojo.Product;
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

    /** 用户更改购物车订单详情
     * @param id 购物车详情的主键id
     * @param number 更改后数量
     * @return 字符串方便用户端读取
     */
     String changeCartDetailsNumber(int id,int number);

    /** 用户删除购物车内容
     * @param id 购物车详情主键
     * @return 修改状态字符串
     */
     String removeCartDetail(int id);

    /** 用户修改地址方法
     * @param id 地址主键id
     * @param newAddr 修改后地址信息
     * @return 字符串表明修改状态
     */
     String updateAddr(int id,String newAddr);

    /** 用户增加地址方法
     * @param detail 增加收货地址
     * @param userId 用户id
     * @return 字符串表明增加状态
     */
     String addAddr(String detail,int userId);

    /** 计算价格方法
     * @param id 购物车详情id
     * @return 计算完后价格
     */
     float priceCount(int id);

    /** 增加订单方法
     * @param id 用户id
     * @param addr 地址
     * @param name 收件人
     * @param telephone 电话
     * @return 订单增加状态
     */
     String addOrder(int id ,String addr,String name,String telephone,float price);

    /** 增加订单详情
     * @param orderId 订单号
     * @param cartDetailsId 购物车详情id
     * @return 状态
     */
     String cartsAddOrderDetail(int orderId,int cartDetailsId);

    /** 单个商品计算总价方法
     * @param id 商品id
     * @param number 商品数量
     * @return 总价
     */
     float countPrice(int id,int number);

    /** 单个商品订单详情添加方法
     * @param orderId  订单号
     * @param productId 商品号
     * @param number 数量
     */
     void shopAddOrderDetail(int orderId,int productId,int number);

    /** 更改商品销量库存
     * @param productId  商品id
     * @param number 数量
     */
     void updateProduct(int productId, int number);

    /** 增加订单详情方法
     * @param productId  商品id
     * @param number 数量
     * @param id 用户id
     * @return 信息
     */
     String addCartsDetail(int productId,int number,int id);

    /** 商品名称查找
     * @param name 品名
     * @return 商品集合
     */
     ArrayList<Product> nameSearch(String name);

    /** 商品属性查找
     * @param name 属性名
     * @return 商品集合
     */
     ArrayList<Product> cateSearch(String name);

    /** 用户售后方法
     * @param orderId 订单号
     * @param useMsg 退单原因
     * @return 生成状态
     */
     String addAfterSale(int orderId,String useMsg);

    /** 查看订单方法
     * @param id 用户id
     * @return 返回订单集合
     */
     HashMap<Integer,ArrayList<OrderDetail>> orderList(int id);

    /** 查看待评价订单
     * @param id
     * @return
     */
     ArrayList<Integer> noEvaluationOrder(int id);

}
