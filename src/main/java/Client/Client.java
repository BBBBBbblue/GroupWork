package Client;

import Client.util.UserReadThread;
import Client.view.MainView.MainView;
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

    public void productList(){
        System.out.print("商品名称"+"\t");
        System.out.print("商品价格"+"\t");
        System.out.print("销量"+"\t");
        System.out.print("库存"+"\t");
        System.out.println("商品ID"+"\t");
        for (Iterator<Map.Entry<Product,Integer>> iterator = products.entrySet().iterator();iterator.hasNext();)
        {
            Map.Entry<Product,Integer> entry = iterator.next();
            System.out.print(entry.getKey().getProductName()+"\t");
            System.out.print(entry.getKey().getPrice()+"\t");
            System.out.print(entry.getKey().getSellCount()+"\t");
            System.out.print(entry.getKey().getInventory()+"\t");
            System.out.print(entry.getKey().getId()+"\t");
            System.out.println();
        }
        System.out.println("\t"+"\t"+"页数"+"1"+"/"+products.size()/4);
        // TODO: 2023/1/24 重载方法实现翻页

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
            buffer.clear();
            selector.selectedKeys().clear();
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
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println(client.readMsg(buffer));
        new MainView(client,buffer).mainView();
        }






    }

