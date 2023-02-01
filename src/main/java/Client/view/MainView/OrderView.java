package Client.view.MainView;

import Client.Client;
import Client.controll.OrderController;
import Server.pojo.Order;
import Server.pojo.OrderDetail;
import Server.pojo.Product;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author xingmeng
 * @date 2023/1/31 19:26
 */
public class OrderView {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;

    public OrderView(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }

    public void createOrder(){
        OrderDetail orderDetail = new OrderDetail();
        Order order = new Order();
        System.out.println("输入购买的商品id");
        int id = scanner.nextInt();
        Product product = client.idToProduct.get(new Integer(id));
        if(product == null){
            System.out.println("商品id输错了");
            new OrderView(client,buffer).createOrder();
        }
        System.out.println("输入购买数量");
        int number = scanner.nextInt();
        if(product.getInventory() < number){
            System.out.println("库存不够");
            new OrderView(client,buffer).createOrder();
        }
        double price = number * product.getPrice();
        System.out.println("输入收件人姓名");
        String name = scanner.nextLine();
        System.out.println("输入收件地址");
        String addr = scanner.nextLine();
        order.setReceiveName(name);
        order.setReceiveAddr(addr);
        order.setPrice(price);
        orderDetail.setProductId(id);
        orderDetail.setNumber(number);
        List<OrderDetail> orderDetails = new ArrayList<>();
        orderDetails.add(orderDetail);
        client.hh.put("order",order);
        client.hh.put("orderDetails",orderDetails);
        new OrderController(client,buffer).createOrder();
    }

    public void showOrders(){
        List<Order> orders = (List<Order>) client.hh.get("orders");
        Integer tem = (Integer)client.hh.get("pageNum");
        int pageNum = tem.intValue();
        System.out.println("===================================");
        System.out.println("订单id 订单号 收件人 地址 联系电话 总价 下单日期");
        for (int i = 3*(pageNum - 1); i < 3 * pageNum; i++) {
            Order order = orders.get(i);
            System.out.println(order.getId() + " " + order.getOrderCode() + " " + order.getReceiveName() + " " + order.getReceiveAddr()
                    + " " + order.getTelPhone() + " " + order.getPrice() + " " + order.getGmtCreate());
        }
        int maxPage;
        if(orders.size() % 3 == 0){
            maxPage = orders.size()/3;
        }else {
            maxPage = orders.size()/3 + 1;
        }
        if(pageNum > 1){
            System.out.print("1,上一页  ");
        }
        System.out.print("第" + pageNum + "页/总" + maxPage + "页");
        if(pageNum < maxPage){
            System.out.print(" 2,下一页  ");
        }
        System.out.print("3，查看订单详情  ");
        System.out.println("4,返回");
        int orderKey = scanner.nextInt();
        client.hh.put("orderKey",new Integer(orderKey));
        new OrderController(client,buffer).orderController();
    }

    public void orderDetail(){
        System.out.println("输入您想要查看的订单id:");
        int orderId = scanner.nextInt();
        client.hh.put("orderId",new Integer(orderId));
        new OrderController(client,buffer).showOrderDetail();
    }

    public void showOrderDetails(){
        List<OrderDetail> orderDetails = (List<OrderDetail>) client.hh.get("orderDetails");
        System.out.println("商品名称  商品价格  购买数量");
        for (OrderDetail orderDetail:orderDetails) {
            Product tem = client.idToProduct.get(new Integer(orderDetail.getProductId()));
            System.out.println(tem.getProductName() + "  " + tem.getPrice() + "  " + orderDetail.getNumber());
        }
        Integer tem = (Integer) client.hh.get("key");
        int key = tem.intValue();
        if(key == 1){
            System.out.print("1,修改订单  ");
        }
        if(key == 2){
            System.out.print("2，评价订单  ");
        }
        if(key == 3){
            System.out.print("3,申请售后  ");
        }
        System.out.println("4,返回");
        int orderDetailKey = scanner.nextInt();
        client.hh.put("orderDetailKey",orderDetailKey);
        new OrderController(client,buffer).orderDetailController();
    }

    public void changeOrder(){
        Order order = (Order)client.hh.get("order");
        System.out.println("是否要变更收件地址，是y，否n：");
        String tem = scanner.nextLine();
        if(tem.equals("y")){
            System.out.println("输入新的收件地址");
            order.setReceiveAddr(scanner.nextLine());
        }
        System.out.println("是否要变更收件人姓名，是y，否n：");
        tem = scanner.nextLine();
        if(tem.equals("y")){
            System.out.println("输入新的收货人姓名");
            order.setReceiveName(scanner.nextLine());
        }
        System.out.println("是否要变更订单状态，是y，否n：");
        tem = scanner.nextLine();
        if(tem.equals("y")){
            System.out.println("输入新的订单状态，1，进行中 2，待评价 3，已完成 4，待售后");
            int i = scanner.nextInt();
            if(i < 1 || i > 4){
                System.out.println("修改失败");
                new OrderView(client,buffer).changeOrder();
                return;
            }
            order.setStatus(i);
        }
        client.hh.put("order",order);
        new OrderController(client,buffer).changeOrder();
    }

    public void judgeOrder(){
        //评价订单
    }

    public void afterSale(){
        //售后
    }
}
