package Client.Service.impl;

import Client.Client;
import Client.Service.UserService;
import Client.view.MainView.FunctionView;
import Client.view.MainView.SearchView;
import Server.pojo.OrderDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

@NoArgsConstructor
@Data
/**
 * @author blue
 * @date 2023/1/12 14:34
 **/
public class UserServiceImpl implements UserService {
    private Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;
    private HashMap<Integer, ArrayList<OrderDetail>> list = null;

    public UserServiceImpl(Client client, ByteBuffer buffer) {
        this.client = client;
        this.buffer = buffer;
    }


    @Override
    public void searchOrders() {
        client.sendMsg("查看订单" + client.getUser().getId());
        ObjectInputStream ois = null;

        try {
            if (client.getSelector().select() > 0) {
                int len = client.getChannel().read(buffer);
                if (len != -1) {
                    buffer.flip();
                    ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                    ois = new ObjectInputStream(bi);
                    list = (HashMap<Integer, ArrayList<OrderDetail>>) ois.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            buffer.clear();
            client.getSelector().selectedKeys().clear();
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (list.size() == 0) {
            System.out.println("当前没有订单");
            new FunctionView(client, buffer).functionView();
        } else {
            new SearchView(client, buffer).orderList(list, 1);
        }

    }

    @Override
    public void addAfterSale() {
        System.out.println("======================");
        System.out.println("输入订单号");
        int orderId = scanner.nextInt();
        System.out.println("请输入售后原因");
        scanner.nextLine();
        String msg = scanner.nextLine();
        client.sendMsg("添加售后" + orderId + '~' + msg);
        System.out.println(client.readMsg(buffer));
        new FunctionView(client, buffer).functionView();
    }

    @Override
    public void judgement() {
        System.out.println("======================");
        client.sendMsg("待评价单" + client.getUser().getId());
        ArrayList<Integer> list = null;
        ObjectInputStream ois = null;
        try {
            if (client.getSelector().select() > 0) {
                int len = client.getChannel().read(buffer);
                if (len != -1) {
                    buffer.flip();
                    ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                    ois = new ObjectInputStream(bi);
                    list = (ArrayList<Integer>) ois.readObject();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            buffer.clear();
            client.getSelector().selectedKeys().clear();
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (list.size() == 0) {
            System.out.println("当前没有待评价订单");
            new FunctionView(client, buffer).functionView();
        } else {
            System.out.println("当前待评价订单号如下");
            for (Integer integer : list) {
                System.out.print(integer+"   ");
            }
            System.out.println("输入订单号");
            int orderId = scanner.nextInt();
            System.out.println("输入分数");
            float point;
            while (( point = scanner.nextFloat()) > 100 || point < 0){
                System.out.println("请输入0-10之间的数字");
            }
            scanner.nextLine();
            System.out.println("请输入评价");
            String msg = scanner.nextLine();
            client.sendMsg("评价订单"+orderId+'~'+point+'@'+client.getUser().getId()+'!'+msg);
            System.out.println(client.readMsg(buffer));
            new FunctionView(client,buffer).functionView();
        }
    }
}
