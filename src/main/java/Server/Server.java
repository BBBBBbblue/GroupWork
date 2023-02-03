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

/** 商城服务端
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

    private Server init() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.bind(new InetSocketAddress(IP, PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            userDAO.init();
        } catch (IOException e) {
            System.out.println("初始化失败");
        }
        return this;
    }

    private void userConnect() throws Exception {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        String msg = "欢迎来到小鸭子商城";
        response(msg, socketChannel);
    }

    public void response(String msg, SocketChannel channel) {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        try {
            channel.write(buffer);
        } catch (IOException e) {
            System.out.println("用户已经断开");
        }
        buffer.clear();
    }

    private void analysis(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        String str = null;
        try {
            str = channel.getLocalAddress().toString();
            int len = channel.read(buffer);
            if (len == -1) {
                throw new IOException();
            }
            String userMsg = new String(buffer.array(), 0, len);
            String analysisMsg = null;
            if (userMsg.length() > 4) {
                analysisMsg = userMsg.substring(0, 4);
                userMsg = userMsg.substring(4);
            } else {
                analysisMsg = userMsg;
            }
            buffer.clear();
            switch (analysisMsg) {
                case "智能客服":
                    response("智能客服坤坤，为您服务", channel);
                    ReplyThread thread = new ReplyThread(userMsg, channel, buffer, serverDAO, this);
                    thread.start();
                    break;
                case "用户登录":
                    new LoginThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "用户注册":
                    new RegisterThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "修改资料":
                    new UpdateThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "用户充值":
                    new ChargeThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "用户结账":
                    new PayThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "查看商品":
                    new SendProductThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "看购物车":
                    int i = Integer.parseInt(userMsg);
                    new SendCartsThread(i, channel, buffer, userDAO, this).run();
                    break;
                case "改购物车":
                    new ChangeCartsDetailNumberThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "删购物车":
                    new RemoveCartsDetailThread(userMsg, channel, buffer, userDAO, this).run();
                    break;
                case "修改地址":
                    new UpdateAddrThread(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case "添加地址":
                    new AddAddrThread(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case "订单车车":
                    new CartsAddOrderThread(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case "商城下单":
                    new ShopAddOrderThread(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case "加购物车":
                    new AddCartsDetail(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case "名称查找":
                    new NameSearchThread(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case "分类查找":
                    new PropertySearch(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case "查看订单":
                    new SendOrdersThread(userMsg,channel,buffer,userDAO,this).run();
                    break;
                case"添加售后":
                    new AddAfterSaleThread(userMsg,channel,buffer,userDAO,this).run();
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println(str + ":断开连接，本次服务取消");
            key.cancel();
            try {
                channel.socket().close();
                channel.close();
            } catch (IOException ex) {
                System.out.println("关闭异常");
            }
        }
    }

    private void start() {

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
                if (key.isReadable()) {
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
