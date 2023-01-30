package Server.DAO;

import Server.pojo.CartsDetail;

import java.util.List;

/**
 * @author xingmeng
 * @date 2023/1/30 12:52
 */
public interface CartsDao {
    /**
     * 根据用户id查询购物车详情
     * @param userId 当前用户id
     * @return 查找出的当前用户购物车信息
     */
    List<CartsDetail> selectCartsByUserId(int userId,int status);

    /**
     * 根据订单详情id获取订单实体类
     * @param cartDetailId 订单详情id
     * @return 获得的顶单详情实体类
     */
    CartsDetail selectCartsByCartDetailId(int cartDetailId);

    /**
     * 创建购物车
     * @param userId 用户id
     * @return 返回生成的id
     */
    int createCarts(int userId);

    /**
     * 添加商品到购物车
     * @param carts 被添加的购物车详情实体类
     */
    void createCartsDetail(CartsDetail carts);

    /**
     * 移出购物车
     * @param cartsDetailId 被移出的购物车详情id
     */
    void deleteCartsDetailById(int cartsDetailId);

    /**
     * 修改购物车详情
     * @param carts 被修改的购物车详情实体类
     */
    void updateCartsDetailById(CartsDetail carts);
}
