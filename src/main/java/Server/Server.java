package Server;

import Server.DAO.impl.ServerDAOImpl;
import Server.DAO.impl.UserDAOImpl;
import Server.pojo.User;
import Server.util.serverThread.*;
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
import java.util.ArrayList;
import java.util.Iterator;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * @author blue
 * @date 2023/1/14 20:46
 **/
public class Server {
    private final ServerDAOImpl serverDAO = new ServerDAOImpl();
    private final UserDAOImpl userDAO = new UserDAOImpl();
    private final String IP = "localhost";
    private final int PORT = 8888;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private static ArrayList<User> list = new ArrayList<>();
    public Server init(){
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(IP,PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            userDAO.init();;
        }catch (IOException e){
            System.out.println("初始化失败");
        }
        return this;
    }

    public void userConnect() throws Exception{
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector,SelectionKey.OP_READ);
        String msg = "欢迎来到小鸭子商城";
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
            str = channel.getLocalAddress().toString();
            int len = channel.read(buffer);
            if (len == -1){
                throw new IOException();
            }
            String userMsg = new String(buffer.array(),0,len);
            buffer.clear();
            switch (userMsg){
                case "智能客服":
                    response("智能客服坤坤，为您服务",channel);
                    ReplyThread thread = new ReplyThread(userMsg,channel,buffer,serverDAO,this);
                    thread.start();
                break;
                case "用户登录":
                    LoginThread loginThread = new LoginThread(userMsg,channel,buffer,userDAO,this);
                    loginThread.start();
                break;
                case "用户注册":
                    RegisterThread registerThread = new RegisterThread(userMsg,channel,buffer,userDAO,this);
                    registerThread.start();
                    break;
                case "修改资料":
                    UpdateThread updateThread = new UpdateThread(userMsg,channel,buffer,userDAO,this);
                    updateThread.start();
                    break;
                case"充值":
                    ChargeThread chargeThread = new ChargeThread(userMsg,channel,buffer,userDAO,this);
                    chargeThread.start();
                    break;
                case"结账":
                    PayThread payThread = new PayThread(userMsg,channel,buffer,userDAO,this);
                    payThread.start();
                    break;
                case"查看商品":
                    SendProductThread sendProductThread = new SendProductThread(userMsg,channel,buffer,userDAO,this);
                    sendProductThread.start();
                    break;
                default:break;
            }
        }catch (IOException e){
            System.out.println(str+":断开连接，本次服务取消");
            key.cancel();
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
        Server s = new Server();
        s.init().start();
    }
}
