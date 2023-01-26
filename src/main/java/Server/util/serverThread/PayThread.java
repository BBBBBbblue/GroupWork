package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/1/19 20:26
 **/
public class PayThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;
    private int index;
    private String account;
    private String money;

    public PayThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        try{
                    index = userMsg.indexOf('~');
                    account = userMsg.substring(0,index);
                    money = userMsg.substring(index+1);
                    float price = Float.parseFloat(money);
                    String resMsg = userDAO.pay(account,price);
                    channel.write(ByteBuffer.wrap(resMsg.getBytes()));
        }catch (IOException e){
            System.out.println("充值服务器出错");
        }
    }
}
