package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;
import Server.pojo.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * @author blue
 * @date 2023/2/3 18:15
 **/
public class NameSearchThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;


    public NameSearchThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        ArrayList<Product> list = userDAO.nameSearch(userMsg);
        if (list == null){
            try {
                channel.write(ByteBuffer.wrap("商品不存在".getBytes()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {

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
}
