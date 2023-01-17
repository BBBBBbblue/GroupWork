package Client;

import Client.util.UserReadThread;
import Client.view.MainView.MainView;
import Server.pojo.User;
import lombok.Data;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;
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

    public void readMsg(ByteBuffer buffer){
        try {
            if (selector.select()>0){
                int len = channel.read(buffer);
                if (len != -1){
                    buffer.flip();
                    System.out.println(new String(buffer.array(),0,len));
                }
            }
        } catch (IOException e) {
            System.out.println("主机已经断开");
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
        client.readMsg(buffer);
        new MainView(client,buffer).mainView();


        }






    }

