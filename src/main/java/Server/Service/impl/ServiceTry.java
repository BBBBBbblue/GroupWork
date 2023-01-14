package Server.Service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * @author blue
 * @date 2023/1/14 20:46
 **/
public class ServiceTry {
    private final String IP = "localhost";
    private final int PORT = 8888;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    public ServiceTry init(){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(IP,PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            System.out.println("初始化失败");
        }
        return this;
    }

    public void userConnect() throws Exception{
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        String msg = "智能客服坤坤，竭诚为您服务";
        response(msg,socketChannel);

    }
    public void response(String msg,SocketChannel channel){
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        try {
            channel.write(buffer);
        } catch (IOException e) {
            System.out.println("回复出错了");
        }
        buffer.clear();
    }
    public void userSendMsg(SelectionKey key){
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String str = null;
    }
    public void start(){
        try {
            selector.select();
        } catch (IOException e) {
            System.out.println("选择器出错");
        }
        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (true) {
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    try {
                        userConnect();
                    } catch (Exception e) {
                        System.out.println("连接出错了");
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ServiceTry s = new ServiceTry();
        s.init().start();
    }
}
