package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/2/2 20:31
 **/
public class AddCartsDetail extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;


    public AddCartsDetail(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        int productId;
        int number;
        int id;
        productId = Integer.parseInt(userMsg.substring(0,userMsg.indexOf('~')));
        number = Integer.parseInt(userMsg.substring((userMsg.indexOf('~')+1),userMsg.indexOf('@')));
        id = Integer.parseInt(userMsg.substring(userMsg.indexOf('@')+1));
        String res = server.getUserDAO().addCartsDetail(productId,number,id);
        try {
            channel.write(ByteBuffer.wrap(res.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
