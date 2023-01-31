package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/1/30 22:09
 **/
public class RemoveCartsDetailThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;
    private int index;
    private String account;
    private String balance;


    public RemoveCartsDetailThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        int id = Integer.parseInt(userMsg);
        String resMsg = server.getUserDAO().removeCartDetail(id);
        try {
            channel.write(ByteBuffer.wrap(resMsg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
