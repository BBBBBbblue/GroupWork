package Server.util;

import Server.DAO.impl.ServerDAOImpl;
import Server.Server;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Data
@NoArgsConstructor
/**
 * @author blue
 * @date 2023/1/15 23:03
 **/
public class ReplyThread extends Thread{
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private ServerDAOImpl serverDAO;
    private Server server;

    public ReplyThread(String userMsg, SocketChannel channel, ByteBuffer buffer, ServerDAOImpl serverDAO, Server server){
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.serverDAO = serverDAO;
        this.server = server;
    }

    @Override
    public void run() {
            try {
                while (true) {
                    int len = channel.read(buffer);
                    if (len == -1) {
                        throw new IOException();
                    }
                    if (buffer.position() != 0) {
                        userMsg = new String(buffer.array(), 0, len);
                        if (userMsg.equals("1")) {
                            buffer.clear();
                            server.response("坤坤期待下次为您服务",channel);
                            break;
                        }
                        String ans = serverDAO.response(userMsg);
                        server.response(ans, channel);
                        buffer.clear();

                    }
                }
            } catch (IOException e) {
                System.out.println("服务结束");
            }

        }

}
