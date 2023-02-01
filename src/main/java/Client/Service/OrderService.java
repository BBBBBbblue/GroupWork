package Client.Service;

import Server.pojo.Order;
import Server.pojo.OrderDetail;
import Server.pojo.Product;
import Server.pojo.User;

import java.util.HashMap;
import java.util.List;

/**
 * @author xingmeng
 * @date 2023/1/31 19:01
 */
public interface OrderService {
    /**
     * 获得当前用户全部订单
     * @param userId 当前用户id
     * @return 当前用户全部订单
     */
    List<Order> getAllOrder(int userId);

    /**
     * 获得一条订单实体类
     * @param orderId 订单id
     * @return 取得的订单实体类
     */
    Order getOrder(int orderId);

    /**
     * 获得当前用户不同状态下订单
     * @param userId 当前用户id
     * @param status 订单状态
     * @return 当前用户不同状态下订单
     */
    List<Order> getOrderByStatus(int userId,int status);

    /**
     * 获得订单详情
     * @param orderId 想要查看订单详情的订单id
     * @return 订单详情
     */
    List<OrderDetail> getOrderDetailByOrderId(int orderId);

    /**
     * 更新订单
     * @param order 更新后的订单实体类
     */
    void updateOrder(Order order);

    /**
     * 提交订单
     * @param order 提交的订单实体类
     * @param orderDetail 提交的订单详情实体类
     */
    void createOrder(Order order,List<OrderDetail> orderDetail);

    /**
     * 提交购物车
     * @param user 当前用户实体类
     * @param order 提交的订单实体类
     * @param idToProduct 商品信息
     */
    void createCartOrder(User user, Order order,HashMap<Integer, Product> idToProduct);
}
