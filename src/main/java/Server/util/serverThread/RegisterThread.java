package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/1/17 23:14
 **/
public class RegisterThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;
    private int index;
    private String account;
    private String password;
    private String telephone;

    public RegisterThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
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
                    userMsg = userMsg.substring(index+1);
                    index = userMsg.indexOf('@');
                    password = userMsg.substring(0,index);
                    telephone = userMsg.substring(index+1);
                    String resMsg = userDAO.register(account,password,telephone);
                    channel.write(ByteBuffer.wrap(resMsg.getBytes()));

                    return;
                }
            }
        }catch (IOException e){
            System.out.println("注册出错");
        }
    }
}
