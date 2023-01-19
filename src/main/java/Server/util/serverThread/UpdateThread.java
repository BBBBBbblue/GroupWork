package Server.util.serverThread;

import Server.DAO.impl.UserDAOImpl;
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
 * @date 2023/1/18 20:19
 **/
public class UpdateThread extends Thread {
    private String userMsg;
    private SocketChannel channel;
    private ByteBuffer buffer;
    private UserDAOImpl userDAO;
    private Server server;
    private int index;
    private String account;
    private String nickname;
    private String email;

    public UpdateThread(String userMsg, SocketChannel channel, ByteBuffer buffer, UserDAOImpl userDAO, Server server) {
        this.userMsg = userMsg;
        this.channel = channel;
        this.buffer = buffer;
        this.userDAO = userDAO;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            while (true){
                int len = channel.read(buffer);
                if (len == -1){
                    throw new IOException();
                }
                if (buffer.position() != 0){
                    userMsg = new String(buffer.array(),0,len);
                    buffer.clear();
                    index = userMsg.indexOf('~');
                    nickname = userMsg.substring(0,index);
                    email = userMsg.substring(index+1,userMsg.indexOf('!'));
                    account = userMsg.substring(userMsg.indexOf('!')+1);
                    String resMsg = userDAO.update(nickname,email,account);
                    channel.write(ByteBuffer.wrap(resMsg.getBytes()));
                    return;
                }
            }
        }catch (IOException e){
            System.out.println("更新出错");
        }
    }
}
