package Client.view.MainView;

import Client.Client;
import Client.controll.CartsController;
import Client.controll.ShopController;
import Client.util.comparator.ProductPriceComp;
import Server.pojo.CartsDetail;
import Server.pojo.Product;
import sun.awt.image.ByteInterleavedRaster;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author blue
 * @date 2023/1/24 0:56
 **/
public class ShopView {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;

    public ShopView(Client client, ByteBuffer buffer) {
        this.client = client;
        this.buffer = buffer;
    }

    public void shopView() {
        System.out.println("==========================================================");
        System.out.println("1,查看商品 2,查看购物车 3，查找商品 4,返回 ");
        new ShopController(client, buffer).shopChoose(scanner.nextInt());
    }

    public void productsView() {
        client.sendMsg("查看商品");
        client.readProduct(buffer);
        int j;
        ProductPriceComp comp = new ProductPriceComp();
        TreeMap<Product, Integer> treeMap = new TreeMap<>(comp);
        treeMap.putAll(client.getProducts());
        client.productList(treeMap, 1);
        while ((j = scanner.nextInt()) != 0) {
            client.productList(treeMap, j);
        }
        System.out.println("1,价格升序 2，价格降序 3，销量降序 4,购买商品 5,添加购物车 6,返回");
        new ShopController(client, buffer).shopListChoose(scanner.nextInt());
        // TODO: 2023/1/24 功能继续完善
        // TODO: 2023/1/25 分类查找功能 ，可以写成查找功能，有待实现
        // TODO: 2023/1/25 线程加锁的问题可以开始思考
    }

    public void productsView(Comparator comp) {
        int j;
        TreeMap<Product, Integer> treeMap = new TreeMap<>(comp);
        treeMap.putAll(client.getProducts());
        client.productList(treeMap, 1);
        while ((j = scanner.nextInt()) != 0) {
            client.productList(treeMap, j);
        }
        System.out.println("1,价格升序 2，价格降序 3，销量降序 4,购买商品 5,添加购物车 6,返回");
        new ShopController(client, buffer).shopListChoose(scanner.nextInt());
    }

    public void cartsView() {
        int j;
        client.sendMsg("看购物车" + client.getUser().getId());
        client.readCarts(buffer);
        client.cartsList(1);
        while ((j = scanner.nextInt()) != 0) {
            client.cartsList(j);
        }
        System.out.println("1,修改数量 2.移除商品 3.添加订单 4.返回");
        new CartsController(client, buffer).cartsListChoose(scanner.nextInt());
    }


    public void addOrder() {
        System.out.println("==========================");
        int i;
        int j;
        System.out.println("输入商品id");
        i = scanner.nextInt();
        System.out.println("输入商品数量");
        j = scanner.nextInt();
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
        client.sendMsg("商城下单" + client.getUser().getAccount() + '~' + client.getUser().getId() + '@' + addr + '#' + telephone + '!' + name + '$' + i + '%' + j);

        String resMsg = client.readMsg(buffer);
        if (!resMsg.equals("余额不足") && !resMsg.equals("商品库存不足，交易失败")) {
            String analysis = resMsg.substring(0, resMsg.indexOf('~'));
            System.out.println(analysis);
            resMsg = resMsg.substring(resMsg.indexOf('~') + 1);
            float totalPrice = Float.parseFloat(resMsg);
            System.out.println("消费金额为：" + totalPrice);
            client.getUser().setBalance(client.getUser().getBalance() - totalPrice);

        } else {
            System.out.println(resMsg);
        }

        System.out.println("当前账户余额" + client.getUser().getBalance());
        new ShopView(client, buffer).shopView();
    }

    public void addCartsDetail() {
        System.out.println("==========================");
        int i;
        int j;
        System.out.println("输入商品id");
        i = scanner.nextInt();
        System.out.println("输入商品数量");
        j = scanner.nextInt();
        client.sendMsg("加购物车" + i + '~' + j + '@' + client.getUser().getId());
        System.out.println(client.readMsg(buffer));
        new ShopView(client, buffer).shopView();
    }

    public void searchView() {
        System.out.println("======================");
        int choose;
        String msg = null;
        ObjectInputStream ois = null;
        ArrayList<Product> list = new ArrayList<>();
        System.out.println("1,名称查找 2，分类查找");
        if ((choose = scanner.nextInt()) == 1) {
            msg = "名称查找";
        } else {
            msg = "分类查找";
        }
        scanner.nextLine();
        System.out.println("请输入具体信息");
        msg = msg + scanner.nextLine();
        client.sendMsg(msg);
        try{
            if (client.getSelector().select() > 0){
                int len = client.getChannel().read(buffer);
                if (len != -1){
                    buffer.flip();
                    ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                    ois = new ObjectInputStream(bi);
                    list = (ArrayList<Product>) ois.readObject();

                }
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            System.out.println("商品不存在");
            return;
        }finally {
            buffer.clear();
            client.getSelector().selectedKeys().clear();
            try {
                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (list.size() == 0){
            System.out.println("商品不存在");
            new ShopView(client,buffer).shopView();
        }
        else {
            new SearchView(client,buffer).searchList(list,1);
        }


    }

}
