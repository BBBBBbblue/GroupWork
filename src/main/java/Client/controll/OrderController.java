package Client.controll;

import Client.Client;
import Client.Service.OrderService;
import Client.Service.impl.OrderServiceImpl;
import Client.view.MainView.OrderView;
import Client.view.MainView.ShopView;
import Server.pojo.Order;
import Server.pojo.OrderDetail;
import Server.pojo.User;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xingmeng
 * @date 2023/1/31 18:39
 */
public class OrderController {
    private Client client;
    private ByteBuffer buffer;
    private OrderServiceImpl orderService;

    public OrderController(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
        orderService = new OrderServiceImpl();
    }

    public void showOrderList(){
        Integer tem =  (Integer) client.hh.get("key");
        User user = client.getUser();
        List<Order> orders = new ArrayList<>();
        int key = tem.intValue();
        switch (key){
            case 1:
                orders = orderService.getAllOrder(user.getId());
                client.hh.put("orders",orders);
                new OrderView(client,buffer).showOrders();
                break;
            case 2:
                orders = orderService.getOrderByStatus(user.getId(),1);
                client.hh.put("orders",orders);
                new OrderView(client,buffer).showOrders();
                break;
            case 3:
                orders = orderService.getOrderByStatus(user.getId(),2);
                client.hh.put("orders",orders);
                new OrderView(client,buffer).showOrders();
                break;
            case 4:
                orders = orderService.getOrderByStatus(user.getId(),3);
                client.hh.put("orders",orders);
                new OrderView(client,buffer).showOrders();
                break;
            case 5:
                orders = orderService.getOrderByStatus(user.getId(),4);
                client.hh.put("orders",orders);
                new OrderView(client,buffer).showOrders();
                break;
            case 6:new ShopView(client,buffer).shopView();
            default:break;
        }
    }

    public void orderController(){
        Integer tem = (Integer) client.hh.get("orderKey");
        int detailKey = tem.intValue();
        tem = (Integer) client.hh.get("pageNum");
        int pageNum = tem.intValue();
        switch (detailKey){
            case 1:
                pageNum++;
                client.hh.put("pageNum",new Integer(pageNum));
                new OrderController(client,buffer).showOrderList();
                break;
            case 2:
                pageNum--;
                client.hh.put("pageNum",new Integer(pageNum));
                new OrderController(client,buffer).showOrderList();
                break;
            case 3:
                new OrderView(client,buffer).orderDetail();
                break;
            case 4:new ShopView(client,buffer).orderView();
            default:break;
        }
    }

    public void showOrderDetail(){
        Integer tem = (Integer)client.hh.get("orderId");
        int orderId = tem.intValue();
        List<OrderDetail> orderDetails = orderService.getOrderDetailByOrderId(orderId);
        if(orderDetails.size()>0) {
            client.hh.put("orderDetails", orderDetails);
            new OrderView(client, buffer).showOrderDetails();
        }else {
            System.out.println("订单id错误");
            new OrderController(client,buffer).showOrderList();
        }
    }

    public void orderDetailController(){
        Integer tem = (Integer)client.hh.get("orderDetailKey");
        int orderDetailKey = tem.intValue();
        switch (orderDetailKey){
            case 1:
                List<OrderDetail> orderDetails = (List<OrderDetail>) client.hh.get("orderDetails");
                int orderId = orderDetails.get(0).getOrderId();
                Order order = orderService.getOrder(orderId);
                client.hh.put("order",order);
                new OrderView(client,buffer).changeOrder();
                break;
            case 2:
                new OrderView(client,buffer).judgeOrder();
                break;
            case 3:
                new OrderView(client,buffer).afterSale();
                break;
            case 4:
                this.showOrderList();
                break;
            default:break;
        }
    }

    public void changeOrder(){
        Order order = (Order)client.hh.get("order");
        orderService.updateOrder(order);
        System.out.println("修改订单成功");
        this.showOrderList();
    }
}
