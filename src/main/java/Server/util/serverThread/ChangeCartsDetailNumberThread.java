package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/1/30 21:26
 **/
public class ChangeCartsDetailNumberThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;


    public ChangeCartsDetailNumberThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            String id = userMsg.substring(0,userMsg.indexOf('~'));
            userMsg = userMsg.substring(userMsg.indexOf('~')+1);
            int id1 = Integer.parseInt(id);
            int number = Integer.parseInt(userMsg);
            String resMsg = server.getUserDAO().changeCartDetailsNumber(id1,number);
            channel.write(ByteBuffer.wrap(resMsg.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
