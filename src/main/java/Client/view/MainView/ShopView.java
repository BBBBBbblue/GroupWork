package Client.view.MainView;

import Client.Client;
import Client.controll.ShopController;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/24 0:56
 **/
public class ShopView {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;

    public ShopView(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }

    public void shopView(){
        System.out.println("==============================");
        System.out.println("1,查看商品 2,查看购物车 ");
        new ShopController(client,buffer).shopChoose(scanner.nextInt());
    }

    public void productsView(){
        client.sendMsg("查看商品");
        client.readProduct(buffer);
        client.productList();
        // TODO: 2023/1/24 功能继续完善
    }

    public void cartsView(){
        // TODO: 2023/1/24 购物车视图有待构建

    }
}
