package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * @author blue
 * @date 2023/2/2 18:47
 **/
public class ShopAddOrderThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;


    public ShopAddOrderThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {

        float totalPrice = 0;
        String account = userMsg.substring(0,userMsg.indexOf('~'));
        System.out.println(account);
        int userId = Integer.parseInt(userMsg.substring((userMsg.indexOf('~')+1),userMsg.indexOf('@')));
        System.out.println(userId);
        String addr = userMsg.substring((userMsg.indexOf('@')+1),userMsg.indexOf('#'));
        System.out.println(addr);
        String telephone = userMsg.substring((userMsg.indexOf('#')+1),userMsg.indexOf('!'));
        System.out.println(telephone);
        String name = userMsg.substring((userMsg.indexOf('!')+1),userMsg.indexOf('$'));
        System.out.println(name);
        int productId = Integer.parseInt(userMsg.substring((userMsg.indexOf('$')+1),userMsg.indexOf('%')));
        System.out.println(productId);
        int number = Integer.parseInt(userMsg.substring((userMsg.indexOf('%')+1)));
        System.out.println(number);

        totalPrice = server.getUserDAO().countPrice(productId,number);
        if (totalPrice == -1){
            try {
                channel.write(ByteBuffer.wrap("商品库存不足，交易失败".getBytes()));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String res = server.getUserDAO().pay(account,totalPrice);
        System.out.println(res);
        if (!res.substring(0,4).equals("消费成功")){
            try {
                channel.write(ByteBuffer.wrap("余额不足".getBytes()));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String orderRes = server.getUserDAO().addOrder(userId,addr,name,telephone,totalPrice);
        int orderId = Integer.parseInt(orderRes.substring(0,orderRes.indexOf('~')));
        userDAO.shopAddOrderDetail(orderId,productId,number);
        userDAO.updateProduct(productId,number);
        String orderCode = orderRes.substring(orderRes.indexOf('~')+1);
        try {
            channel.write(ByteBuffer.wrap(("订单生成,订单号为："+orderCode+'~'+totalPrice).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
