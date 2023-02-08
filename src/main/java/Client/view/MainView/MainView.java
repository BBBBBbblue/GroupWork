package Client.view.MainView;

import Client.Client;
import Client.controll.MainController;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/12 14:21
 **/
public class MainView {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;
    public MainView(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }
    public  void mainView(){
        System.out.println("1，用户登录 2，用户注册 3，退出");
        new MainController(client,buffer).mainChoose(Integer.parseInt(scanner.nextLine()));
    }

}
