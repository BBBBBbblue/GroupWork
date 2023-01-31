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
 * @date 2023/1/31 20:23
 **/
public class CartsAddOrderThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;


    public CartsAddOrderThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        ArrayList<Integer> list;
        float totalPrice = 0;
        String account = userMsg.substring(0,userMsg.indexOf('~'));
        System.out.println(account);
        int userId = Integer.parseInt(userMsg.substring((userMsg.indexOf('~')+1),userMsg.indexOf('@')));
        System.out.println(userId);
        String addr = userMsg.substring((userMsg.indexOf('@')+1),userMsg.indexOf('#'));
        System.out.println(addr);
        String telephone = userMsg.substring((userMsg.indexOf('#')+1),userMsg.indexOf('!'));
        System.out.println(telephone);
        String name = userMsg.substring(userMsg.indexOf('!')+1);
        System.out.println(name);
        try {
            channel.write(ByteBuffer.wrap("对接完毕,开始交易".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            try {
                int len = channel.read(buffer);
                if (len == -1){
                    throw new IOException();
                }
                else {
                    if (buffer.position() != 0){
                        ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                        ObjectInputStream ois = new ObjectInputStream(bi);
                        list =(ArrayList<Integer>) ois.readObject();
                        System.out.println(list);
                        ois.close();
                        for (Integer integer : list) {
                            float price = userDAO.priceCount(integer);
                            if (price == 0){
                                throw new IOException();
                            }
                            totalPrice = totalPrice + price;
                        }
                        for (Integer k : list) {
                            userDAO.removeCartDetail(k);
                        }
                        System.out.println(totalPrice);
                        break;
                    }
                }
            }catch (IOException | ClassNotFoundException e){
                System.out.println("有商品售完或下架");
                return;
            }
        }
        String res = server.getUserDAO().pay(account,totalPrice);
        try {
            channel.write(ByteBuffer.wrap(res.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orderRes = server.getUserDAO().addOrder(userId,addr,name,telephone,totalPrice);
        int orderId = Integer.parseInt(orderRes.substring(0,orderRes.indexOf('~')));
        String orderCode = orderRes.substring(orderRes.indexOf('~')+1);
        try {
            channel.write(ByteBuffer.wrap(("订单生成,订单号为："+orderCode).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Integer m : list) {
            userDAO.cartsAddOrderDetail(orderId,m);
        }

    }
}
