package Server.Service.impl;

import Server.DAO.impl.ServerDAOImpl;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
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
    private final ServerDAOImpl serverDAO = new ServerDAOImpl();
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
        String msg = "欢迎来到商城，请自由挑选";
        response(msg,socketChannel);

    }
    public void response(String msg,SocketChannel channel){
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        try {
            channel.write(buffer);
        } catch (IOException e) {
            System.out.println("用户已经断开");
        }
        buffer.clear();
    }
    public void analysis(SelectionKey key){
        SocketChannel channel = (SocketChannel)key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String str = null;
        try{
            int len = channel.read(buffer);
            if (len == -1){
                throw new IOException();
            }
            String userMsg = new String(buffer.array(),0,len);
            buffer.clear();
            switch (userMsg){
                case "智能客服":
                    response("智能客服坤坤，为您服务",channel);
                while (true) {

                    len = channel.read(buffer);
                    if (len == -1) {
                        throw new IOException();
                    }
                    if (buffer.position() == 0) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (userMsg.equals(1)) {
                            break;
                        }
                        userMsg = new String(buffer.array(), 0, len);
                        String ans = serverDAO.response(userMsg);
                        response(ans, channel);
                        buffer.clear();
                    }
                }
                break;
                default:break;
            }
        }catch (IOException e){
            System.out.println("用户断开连接，本次服务取消");
            key.channel();
            try {
                channel.socket().close();
                channel.close();
            } catch (IOException ex) {
                System.out.println("关闭异常");
            }
        }
    }


    public void userSendMsg(SelectionKey key){
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        String str = null;
    }
    public void start(){

        while (true) {
            try {
                selector.select();
            } catch (IOException e) {
                System.out.println("选择器出错");
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    try {
                        userConnect();
                    } catch (Exception e) {
                        System.out.println("已断开与客户连接");
                    }
                }
                if (key.isReadable()){
                    analysis(key);
                }
                iterator.remove();
            }
        }
    }

    public static void main(String[] args) {
        ServiceTry s = new ServiceTry();
        s.init().start();
    }
}
