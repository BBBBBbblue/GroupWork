package Server.DAO;

import Server.pojo.Order;
import Server.pojo.OrderDetail;

import java.util.List;

/**
 * @author xingmeng
 * @date 2023/1/30 12:53
 */
public interface OrderDao {
    /**
     * 创建订单
     * @param order 需要添加的订单实体类
     * @return 返回生成的主键id
     */
    int createOrder(Order order);

    /**
     * 创建订单详情
     * @param orderDetail 需要添加的订单详情实体类
     */
    void createOrderDetail(OrderDetail orderDetail);

    /**
     * 查询当前用户订单
     * @param userId 当前用户id
     * @param status 订单状态
     * @return 查询出的订单实体类集合
     */
    List<Order> selectOrderByUserId(int userId,int status);

    /**
     * 根据订单id获得订单实体类
     * @param orderId 订单id
     * @return
     */
    Order selectOrderById(int orderId);

    /**
     * 查询订单详情
     * @param orderId 订单id
     * @return 查出的订单详情实体类集合
     */
    List<OrderDetail> selectOrderDetailByOrderId(int orderId);

    /**
     * 变更订单信息
     * @param order 需要修改的订单实体类
     */
    void updateOrder(Order order);

    /**
     * 减少商品库存
     * @param productId
     * @param number
     */
    void minusProductInventory(int productId,int number);
}
