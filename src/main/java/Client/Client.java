package Client;

import Client.util.UserReadThread;
import Server.pojo.User;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

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
                    ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
                    ObjectInputStream ois = new ObjectInputStream(bis);
                    User user =(User) ois.readObject();
                    System.out.println(user.getAccount());
                    // TODO: 2023/1/17 进一步优化
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
        UserReadThread userReadThread = new UserReadThread(buffer, client);
        userReadThread.start();

        while (scanner.hasNext()) {
            client.sendMsg(scanner.nextLine());
            // TODO: 2023/1/16 字符串用~拼接 优化客户端视图
        }





    }
}
