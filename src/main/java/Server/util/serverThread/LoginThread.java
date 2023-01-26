package Server.util.serverThread;

import Server.DAO.UserDAO;
import Server.DAO.impl.ServerDAOImpl;
import Server.DAO.impl.UserDAOImpl;
import Server.Server;
import Server.pojo.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/1/16 18:44
 **/
public class LoginThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;
    private int index;
    private String account;
    private String password;

    public LoginThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        try {
                    index = userMsg.indexOf('~');
                    account = userMsg.substring(0,index);
                    password = userMsg.substring(index+1);
                    User user = userDAO.login(account,password);
                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bo);
                    oos.writeObject(user);
                    channel.write(ByteBuffer.wrap(bo.toByteArray()));

        } catch (NullPointerException e) {
            server.response("用户名密码错误，或账号已经注销，请重试",channel);
            run();
        }catch (IOException e){
            System.out.println("用户已经断开");
        }


    }
}
