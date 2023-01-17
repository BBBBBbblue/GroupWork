package Client.util;

import Client.Client;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
@Data
@NoArgsConstructor
/**
 * @author blue
 * @date 2023/1/17 20:38
 **/
public class UserReadObject extends Thread {
    private ByteBuffer buffer;
    private Client client;
    public UserReadObject(ByteBuffer buffer, Client client){
        this.buffer = buffer;
        this.client = client;
    }

    @Override
    public void run() {
         client.readObject(buffer);
         buffer.clear();
    }
}
