package Client.util;

import Client.Client;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/17 22:08
 **/
public class UserReadReply extends Thread{
    private static Scanner scanner = new Scanner(System.in);
    private ByteBuffer buffer;
    private Client client;

    public UserReadReply(ByteBuffer buffer, Client client) {
        this.buffer = buffer;
        this.client = client;
    }

    @Override
    public void run() {
        String msg = "0";
        while (!msg.equals("1")) {
            System.out.println( client.readMsg(buffer));
            buffer.clear();
            client.sendMsg(msg = scanner.nextLine());
        }
        System.out.println( client.readMsg(buffer));
        buffer.clear();
    }
}
