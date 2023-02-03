package Client.view.MainView;

import Client.Client;
import Client.controll.FunctionController;
import Client.controll.SearchController;
import Server.pojo.OrderDetail;
import Server.pojo.Product;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author blue
 * @date 2023/2/3 19:13
 **/
public class SearchView {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;

    public SearchView(Client client, ByteBuffer buffer) {
        this.client = client;
        this.buffer = buffer;
    }

    public void searchList(ArrayList<Product> list, int i) {
        int j;
        System.out.print("====================================================================");
        System.out.println();
        System.out.print("商品名称" + "\t");
        System.out.print("商品价格" + "\t");
        System.out.print("销量" + "\t");
        System.out.print("库存" + "\t");
        System.out.print("商品ID" + "\t");
        int count = 0;
        for (Product product : list) {

            if (count / 3 == (i - 1)) {
                System.out.println();
                System.out.print(product.getProductName() + "\t");
                System.out.print(product.getPrice() + "\t");
                System.out.print(product.getSellCount() + "\t");
                System.out.print(product.getInventory() + "\t");
                System.out.print(product.getId() + "\t");
            }
            count++;
            if (count / 3 > (i - 1)) {
                break;
            }
        }
        System.out.println();
        System.out.print("\t" + "页数" + i + "/" + (int) Math.ceil(list.size() / 3) + "\t" + "跳转到第__页" + "\t");
        while ((j = scanner.nextInt()) != 0) {
            this.searchList(list, j);
        }
        System.out.println("1,购买商品 2,添加购物车 3,返回");
        new SearchController(client, buffer).searchListChoose(scanner.nextInt());
    }

    public void orderList(HashMap<Integer, ArrayList<OrderDetail>> list, int i) {
        int count = 1;int j;
        for (Map.Entry<Integer, ArrayList<OrderDetail>> entry : list.entrySet()) {
            if (count != i){
                count++;
                continue;
            }else {
                int status = entry.getValue().get(0).getStatus();
                System.out.println("============================================");
                System.out.println("订单号：" + entry.getKey() + "\t" + "总价:" + entry.getValue().get(0).getPrice());
                for (OrderDetail detail : entry.getValue()) {
                    System.out.print("商品名：" + detail.getProductName() + "\t");
                    System.out.print("数量：" + detail.getNumber());
                }
                System.out.println();
                if (status == 1) {
                    System.out.println("订单状态：进行中");
                } else if (status == 2) {
                    System.out.println("订单状态：待评价");
                } else if (status == 3) {
                    System.out.println("订单状态：已完成");
                } else {
                    System.out.println("订单状态：售后中");
                }
                break;
            }
        }
        System.out.print("\t" + "页数" + i + "/" + list.size() + "\t" + "跳转到第__页" + "\t");
        while ((j = scanner.nextInt()) != 0) {
            this.orderList(list,j);
        }
        System.out.println("1,申请售后 2,评价订单 3,返回");
        new FunctionController(client,buffer).otherChoose(scanner.nextInt());
    }
}
