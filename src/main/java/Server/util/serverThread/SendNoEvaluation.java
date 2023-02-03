package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

/**
 * @author blue
 * @date 2023/2/3 22:43
 **/
public class SendNoEvaluation extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;

    public SendNoEvaluation(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        int userId = Integer.parseInt(userMsg);
        ArrayList<Integer> list = userDAO.noEvaluationOrder(userId);
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bo);
            oos.writeObject(list);
            channel.write(ByteBuffer.wrap(bo.toByteArray()));
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
