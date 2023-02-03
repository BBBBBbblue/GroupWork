package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
import Server.Server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author blue
 * @date 2023/2/3 23:21
 **/
public class JudgementThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;
    private int index;
    private String account;
    private String balance;


    public JudgementThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        int orderId = Integer.parseInt(userMsg.substring(0, userMsg.indexOf('~')));

        float point = Float.parseFloat(userMsg.substring(userMsg.indexOf('~') + 1, userMsg.indexOf('@')));

        int userId = Integer.parseInt(userMsg.substring(userMsg.indexOf('@') + 1, userMsg.indexOf('!')));

        userMsg = userMsg.substring(userMsg.indexOf('!') + 1);

        String resMsg = userDAO.judgement(orderId, userMsg, point, userId);
        try {
            channel.write(ByteBuffer.wrap(resMsg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        userDAO.completeOrder(orderId);


    }
}
