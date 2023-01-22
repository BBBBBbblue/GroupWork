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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

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
                    ObjectInputStream oos = new ObjectInputStream(bis);
                    setProducts((HashMap<Product, Integer>) oos.readObject());
                    // TODO: 2023/1/22 商品的分页查询
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

