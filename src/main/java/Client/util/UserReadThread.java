package Client.util;

import Client.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

@Data
@NoArgsConstructor
/** 客户端接收消息线程
 * @author blue
 * @date 2023/1/16 18:12
 **/
public class UserReadThread extends Thread {
    private  ByteBuffer buffer;
    private  Client client;
    public UserReadThread(ByteBuffer buffer, Client client){
        this.buffer = buffer;
        this.client = client;
    }

    @Override
    public void run() {
        while (true){
            client.readMsg(buffer);
            buffer.clear();
        }
    }
}
