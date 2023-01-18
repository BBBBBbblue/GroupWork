package Client.view.MainView;

import Client.Client;
import Client.Service.UserService;
import Client.Service.impl.UserServiceImpl;
import Client.controll.FunctionController;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/17 21:44
 **/
public class FunctionView {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserServiceImpl();
    private Client client;
    private ByteBuffer buffer;
    public FunctionView(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }

    public void functionView(){
        System.out.println("==========================================");
        System.out.println("1,智能客服 2,商城主页 3，修改用户资料 4,充值");
        new FunctionController(client,buffer).functionChoose(scanner.nextInt());
    }
}
