package Client.view.MainView;

import Client.Client;
import Server.pojo.CartsDetail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/30 20:28
 **/
public class CartsView {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;

    public CartsView(Client client, ByteBuffer buffer) {
        this.client = client;
        this.buffer = buffer;
    }

    public void changeNumber() {
        int id;
        int count;
        String name;
        System.out.println("输入商户名称");
        name = scanner.nextLine();
        System.out.println("输入详情id");
        id = scanner.nextInt();
        System.out.println("输入修改后数量");
        while ((count = scanner.nextInt()) <= 0) {
            System.out.println("最少购买一件，重新输入！");
        }
        client.sendMsg("改购物车" + id + "~" + count);
        System.out.println(client.readMsg(buffer));
        for (CartsDetail cartsDetail : client.getCarts().get(name)) {
            if (cartsDetail.getId() == id) {
                cartsDetail.setNumber(count);
            }
        }
        new ShopView(client, buffer).shopView();
    }

    public void removeProduct() {
        int id;
        String name;
        System.out.println("输入商户名称");
        name = scanner.nextLine();
        System.out.println("输入详情id");
        id = Integer.parseInt(scanner.nextLine());
        client.sendMsg("删购物车" + id);
        System.out.println(client.readMsg(buffer));
        for (Iterator<CartsDetail> iterator = client.getCarts().get(name).iterator(); iterator.hasNext(); ) {
            if (iterator.next().getId() == id) {
                iterator.remove();
                break;
            }
        }
        new ShopView(client, buffer).shopView();

    }

    public void addOrder() {
        System.out.println("==========================");
        ArrayList<Integer> list = new ArrayList<>();
        int i;
        System.out.println("输入详情id");
        while ((i = scanner.nextInt()) != 0) {
            list.add(i);
        }
        String addr = null;
        String telephone;
        String name;
        if (client.getUser().getAddr().size() > 0) {
            System.out.println("1,使用默认地址 2，新地址");
            int choose = scanner.nextInt();
            if (choose == 1) {
                for (String s : client.getUser().getAddr().keySet()) {
                    addr = s;
                    break;
                }
                telephone = client.getUser().getTelephone();
            } else {
                scanner.nextLine();
                System.out.println("请输入地址");
                addr = scanner.nextLine();
                System.out.println("输入手机号码");
                telephone = scanner.nextLine();
            }
            scanner.nextLine();
            System.out.println("输入收货人姓名");
            name = scanner.nextLine();
        } else {
            scanner.nextLine();
            System.out.println("请输入地址");
            addr = scanner.nextLine();
            System.out.println("输入手机号码");
            telephone = scanner.nextLine();
            System.out.println("输入收货人姓名");
            name = scanner.nextLine();
        }
        client.sendMsg("订单车车" + client.getUser().getAccount() + '~' + client.getUser().getId()+'@'+addr+'#'+telephone+'!'+name);
        System.out.println(client.readMsg(buffer));
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bo);
            oos.writeObject(list);
            client.getChannel().write(ByteBuffer.wrap(bo.toByteArray()));
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String resMsg = client.readMsg(buffer);
        String analysis = resMsg.substring(0, 4);
        resMsg = resMsg.substring(4);

        if (analysis.equals("消费成功")) {
            float totalPrice = Float.parseFloat(resMsg);
            System.out.println(totalPrice);
            client.getUser().setBalance(client.getUser().getBalance()-totalPrice);
        }
        System.out.println(client.readMsg(buffer));
        System.out.println(client.getUser().getBalance());
        client.sendMsg("看购物车" + client.getUser().getId());
        client.readCarts(buffer);
        new ShopView(client, buffer).shopView();


    }
}
