package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;
import Server.pojo.OrderDetail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author blue
 * @date 2023/2/3 21:10
 **/
public class SendOrdersThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;

    public SendOrdersThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        int userId = Integer.parseInt(userMsg);
        HashMap<Integer, ArrayList<OrderDetail>> list = userDAO.orderList(userId);
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bo);
            oos.writeObject(list);
            channel.write(ByteBuffer.wrap(bo.toByteArray()));
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
