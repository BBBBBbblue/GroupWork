package Client;

import Client.util.UserReadThread;
import Client.view.MainView.MainView;
import Server.pojo.CartsDetail;
import Server.pojo.Product;
import Server.pojo.User;
import lombok.Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
/**
 * @author blue
 * @date 2023/1/14 21:12
 **/
public class Client {
    private final static String IP = "127.0.0.1";
    private final static int PORT = 8888;
    private Selector selector;
    private SocketChannel channel;
    private static Scanner scanner = new Scanner(System.in);
    private User user;
    private HashMap<Product,Integer> products;
    private HashMap<String,LinkedList<CartsDetail>> carts;
    private ArrayList<CartsDetail> updateCartsDetail;


    public void init(){
        try {
            selector = Selector.open();
            InetSocketAddress address = new InetSocketAddress(IP,PORT);
            channel = SocketChannel.open();
            channel.connect(address);
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("初始化失败");
        }
    }

    public void sendMsg(String msg){

        try {
            ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
            channel.write(buffer);
        } catch (IOException e) {
            System.out.println("服务器异常");
        }
    }

    public void productList(TreeMap<Product,Integer> maps,int i){
        System.out.print("====================================================================");
        System.out.println();
        System.out.print("商品名称"+"\t");
        System.out.print("商品价格"+"\t");
        System.out.print("销量"+"\t");
        System.out.print("库存"+"\t");
        System.out.print("商品ID"+"\t");
        int count = 0;
        for (Iterator<Map.Entry<Product,Integer>> iterator = maps.entrySet().iterator();iterator.hasNext();)
        {
                Map.Entry<Product, Integer> entry = iterator.next();
            if (count / 3 ==(i-1)) {
                System.out.println();
                System.out.print(entry.getKey().getProductName() + "\t");
                System.out.print(entry.getKey().getPrice() + "\t");
                System.out.print(entry.getKey().getSellCount() + "\t");
                System.out.print(entry.getKey().getInventory() + "\t");
                System.out.print(entry.getKey().getId() + "\t");
            }
            count++;
            if (count / 3 > (i-1)){
                break;
            }
        }
        System.out.println();
        System.out.print("\t"+"页数"+i+"/"+(int)Math.ceil(maps.size()/3)+"\t"+"跳转到第__页"+"\t");

    }

    public void cartsList(int i){
        if (i > carts.size()){
            System.out.println();
            System.out.println("=============================================");
            System.out.println("当前页面不存在");
            System.out.println();
            System.out.print("\t"+"页数"+i+"/"+carts.size()+"\t"+"跳转到第__页"+"\t");
            return;
        }

        int count = 1;
        for (Map.Entry<String, LinkedList<CartsDetail>> entry : carts.entrySet()) {
            if (count == i) {
                System.out.println();
                System.out.println("=============================================");
                System.out.println("商户名称：" + entry.getKey());
                System.out.println();
                System.out.print("商品名称" + "\t" + "购买数量" + "\t" + "商品单价" + "\t"+"详情id");
                for (CartsDetail cartsDetail : entry.getValue()) {
                    System.out.println();
                    System.out.print(cartsDetail.getProductName() + "\t" + cartsDetail.getNumber() + "\t" + cartsDetail.getPrice()+"\t"+cartsDetail.getId());
                }
                break;
            }
            count++;
        }
        System.out.println();
        System.out.print("\t"+"页数"+i+"/"+carts.size()+"\t"+"跳转到第__页"+"\t");
    }

    public String readMsg(ByteBuffer buffer){
        String resMsg = null;
        try {
            if (selector.select()>0){
                int len = channel.read(buffer);
                if (len != -1){
                    buffer.flip();
                    resMsg = new String(buffer.array(),0,len);
                }
                else {
                    throw new IOException();
                }

            }
        } catch (IOException e) {
            resMsg = new String("主机已经断开");
        } finally {
            selector.selectedKeys().clear();
            buffer.clear();
            return resMsg;
        }
    }


    public void readProduct(ByteBuffer buffer){
        try {
            if (selector.select()>0) {
                int len = channel.read(buffer);
                if (len != -1) {
                    buffer.flip();
                    ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    setProducts((HashMap<Product, Integer>) ois.readObject());
                    ois.close();
                }
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        } finally {
            buffer.clear();
            selector.selectedKeys().clear();
        }
    }

    public void readCarts(ByteBuffer buffer){
        try{
            if (selector.select() > 0){
                int len = channel.read(buffer);
                if (len != -1){
                    buffer.flip();
                    ByteArrayInputStream bi = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream ois = new ObjectInputStream(bi);
                    setCarts((HashMap<String, LinkedList<CartsDetail>>) ois.readObject());
                    ois.close();
                }
            }
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }finally {
            buffer.clear();
            selector.selectedKeys().clear();
        }
    }

    public void readObject(ByteBuffer buffer){
        try {
            if (selector.select()>0){
                int len = channel.read(buffer);
                if (len != -1){
                    buffer.flip();
                    ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    setUser((User) ois.readObject());
                    ois.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            System.out.println("类不存在");
        } finally {
            buffer.clear();
            selector.selectedKeys().clear();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.init();
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        System.out.println(client.readMsg(buffer));
        new MainView(client,buffer).mainView();
        }
    }

