package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/1/31 18:14
 **/
public class AddAddrThread extends Thread{
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;


    public AddAddrThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        String sid = userMsg.substring(0,userMsg.indexOf('~'));
        int id = Integer.parseInt(sid);
        userMsg = userMsg.substring(userMsg.indexOf('~')+1);
        String resMsg = server.getUserDAO().addAddr(userMsg,id);
        try {
            channel.write(ByteBuffer.wrap(resMsg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
