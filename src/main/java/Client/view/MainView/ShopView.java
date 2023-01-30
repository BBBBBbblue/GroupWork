package Client.view.MainView;

import Client.Client;
import Client.controll.ShopController;
import Client.util.comparator.ProductPriceComp;
import Server.pojo.Product;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Scanner;
import java.util.TreeMap;

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
        System.out.println("==========================================================");
        System.out.println("1,查看商品 2,查看购物车 3,返回 ");
        new ShopController(client,buffer).shopChoose(scanner.nextInt());
    }

    public void productsView(){
        client.sendMsg("查看商品");
        client.readProduct(buffer);
        int j;
        ProductPriceComp comp = new ProductPriceComp();
        TreeMap<Product,Integer> treeMap = new TreeMap<>(comp);
        treeMap.putAll(client.getProducts());
        client.productList(treeMap,1);
        while ((j =scanner.nextInt()) != 0){
            client.productList(treeMap,j);
        }
        System.out.println("1,价格升序 2，价格降序 3，销量降序 4,购买商品 5,添加购物车 6,返回");
        new ShopController(client,buffer).shopListChoose(scanner.nextInt());
        // TODO: 2023/1/24 功能继续完善
        // TODO: 2023/1/25 分类查找功能 ，可以写成查找功能，有待实现
        // TODO: 2023/1/25 线程加锁的问题可以开始思考
    }

    public void productsView(Comparator comp){
        int j;
        TreeMap<Product,Integer> treeMap = new TreeMap<>(comp);
        treeMap.putAll(client.getProducts());
        client.productList(treeMap,1);
        while ((j =scanner.nextInt()) != 0){
            client.productList(treeMap,j);
        }
        System.out.println("1,价格升序 2，价格降序 3，销量降序 4,购买商品 5,添加购物车 6,返回");
        new ShopController(client,buffer).shopListChoose(scanner.nextInt());
    }

    public void cartsView(){
        int j;
        client.sendMsg("看购物车"+client.getUser().getId());
        client.readCarts(buffer);
        client.cartsList(1);
        while ((j = scanner.nextInt()) != 0){
            client.cartsList(j);
        }
        System.out.println("1,修改数量 2.移除商品 3.添加订单 4.返回");
        new ShopController(client,buffer).cartsListChoose(scanner.nextInt());


    }
}
