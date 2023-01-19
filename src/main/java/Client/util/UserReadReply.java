package Client.util;

import Client.Client;

import java.nio.ByteBuffer;

/**
 * @author blue
 * @date 2023/1/17 22:08
 **/
public class UserReadReply extends Thread{
    private ByteBuffer buffer;
    private Client client;

    public UserReadReply(ByteBuffer buffer, Client client) {
        this.buffer = buffer;
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println( client.readMsg(buffer));
            buffer.clear();
        }
    }
}
