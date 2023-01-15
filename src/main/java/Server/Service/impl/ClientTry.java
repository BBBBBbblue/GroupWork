package Server.Service.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/14 21:12
 **/
public class ClientTry {
    private final static String IP = "127.0.0.1";
    private final static int PORT = 8888;
    private Selector selector;
    private SocketChannel channel;

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
                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            System.out.println("用户读数据出错");
        }
    }

    public static void main(String[] args) {
        final ClientTry clientTry = new ClientTry();
        clientTry.init();
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        Thread t = new Thread(){
            @Override
            public void run() {
                while (true){
                    clientTry.readMsg(buffer);
                    buffer.clear();
                }
            }
        };
        t.start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            clientTry.sendMsg(scanner.nextLine());
        }



    }
}
