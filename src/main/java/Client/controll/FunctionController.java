package Client.controll;

import Client.Client;
import Client.util.UserReadReply;
import Client.util.UserReadThread;
import Client.view.MainView.FunctionView;
import Client.view.UserView.UserView;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/17 21:40
 **/
public class FunctionController {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;
    public FunctionController(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }
    public void functionChoose(int i){
        switch (i){
            case 1:
                client.sendMsg("智能客服");
                UserReadReply userReadReply = new UserReadReply(buffer,client);
                userReadReply.start();
                String msg = "0";
                while (!msg.equals("1")){
                    client.sendMsg(msg = scanner.nextLine());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("线程睡出了问题");
                }
                userReadReply.stop();
                new FunctionView(client,buffer).functionView();
                break;
            case 2:
                new UserView(client,buffer).productView();
                break;
            case 3:
                new UserView(client,buffer).updateView();
                break;
            case 4:
                new UserView(client,buffer).chargeView();
                break;
        }
    }
}
