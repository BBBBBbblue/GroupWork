package Client.Service;

/**
 * @author blue
 * @date 2023/1/12 14:33
 **/
public interface UserService {

    /**
     * 用户查看订单方法
     */
     void searchOrders();

    /**
     * 用户增加售后方法
     */
     void addAfterSale();

    /**
     * 评价商品方法
     */
     void judgement();
}
