package Client.Service.impl;

import Client.Service.OrderService;
import Server.DAO.CartsDao;
import Server.DAO.OrderDao;
import Server.DAO.impl.CartsDaoImpl;
import Server.DAO.impl.OrderDaoImpl;
import Server.pojo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author xingmeng
 * @date 2023/1/31 19:08
 */
public class OrderServiceImpl implements OrderService {
    private OrderDao dao = new OrderDaoImpl();
    @Override
    public List<Order> getAllOrder(int userId) {
        List<Order> orders = dao.selectOrderByUserId(userId,1);
        List<Order> tem = dao.selectOrderByUserId(userId,2);
        orders.addAll(tem);
        tem = dao.selectOrderByUserId(userId,3);
        orders.addAll(tem);
        tem = dao.selectOrderByUserId(userId,4);
        orders.addAll(tem);
        return orders;
    }

    @Override
    public Order getOrder(int orderId) {
        Order order = dao.selectOrderById(orderId);
        return order;
    }

    @Override
    public List<Order> getOrderByStatus(int userId, int status) {
        List<Order> orders = dao.selectOrderByUserId(userId,status);
        return orders;
    }

    @Override
    public List<OrderDetail> getOrderDetailByOrderId(int orderId) {
        List<OrderDetail> orderDetails = dao.selectOrderDetailByOrderId(orderId);
        return orderDetails;
    }

    @Override
    public void updateOrder(Order order) {
        dao.updateOrder(order);
    }

    @Override
    public void createOrder(Order order, List<OrderDetail> orderDetails) {
        int id = dao.createOrder(order);
        for (OrderDetail orderDetail:orderDetails) {
            orderDetail.setOrderId(id);
            dao.minusProductInventory(orderDetail.getProductId(),orderDetail.getNumber());
            dao.createOrderDetail(orderDetail);
        }
    }

    @Override
    public void createCartOrder(User user, Order order, HashMap<Integer, Product> idToProduct) {
        CartsDao cartsDao = new CartsDaoImpl();
        List<CartsDetail> cartsDetails = cartsDao.selectCartsByUserId(user.getId(),1);
        List<OrderDetail> orderDetails = new ArrayList<>();
        double price = 0;
        for (CartsDetail cartsDetail:cartsDetails) {
            cartsDao.deleteCartsDetailById(cartsDetail.getId());
            OrderDetail orderDetail = new OrderDetail();
            int productId = cartsDetail.getProductId();
            orderDetail.setProductId(productId);
            int number = cartsDetail.getNumber();
            orderDetail.setNumber(number);
            orderDetails.add(orderDetail);
            Product product = idToProduct.get(new Integer(productId));
            price += number * product.getPrice();
        }
        order.setPrice(price);
        this.createOrder(order,orderDetails);
    }
}
