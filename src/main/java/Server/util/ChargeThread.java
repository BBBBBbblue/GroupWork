package Server.util;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/1/18 21:10
 **/
public class ChargeThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;
    private int index;
    private String account;
    private String balance;


    public ChargeThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true){
                int len = channel.read(buffer);
                if (len == -1){
                    throw new IOException();
                }
                if (buffer.position() != 0){
                    userMsg = new String(buffer.array(),0,len);
                    buffer.clear();
                    index = userMsg.indexOf('~');
                    account = userMsg.substring(0,index);
                    balance = userMsg.substring(index+1);
                    String resMsg = userDAO.charge(account,balance);
                    channel.write(ByteBuffer.wrap(resMsg.getBytes()));
                    return;
                }

            }
        }catch (IOException e){
            System.out.println("服务器出错了");
        }
    }
}
