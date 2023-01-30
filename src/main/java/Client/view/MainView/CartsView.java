package Client.view.MainView;

import Client.Client;
import Server.pojo.CartsDetail;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/30 20:28
 **/
public class CartsView {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;

    public CartsView(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }

    public void changeNumber(){
        int id;int count;String name;
        System.out.println("输入商户名称");
        name = scanner.nextLine();
        System.out.println("输入详情id");
        id = scanner.nextInt();
        System.out.println("输入修改后数量");
        while ((count = scanner.nextInt()) <= 0){
            System.out.println("最少购买一件，重新输入！");
        }
        client.sendMsg("改购物车"+id+"~"+count);
        System.out.println(client.readMsg(buffer));
        for (CartsDetail cartsDetail : client.getCarts().get(name)) {
            if (cartsDetail.getId() == id){
                cartsDetail.setNumber(count);
            }
        }
        new ShopView(client,buffer).shopView();
    }

    public void removeProduct(){

    }

    public void addOrder(){

    }
}
