package Client.view.UserView;

import Client.Client;
import Client.Service.UserService;
import Client.Service.impl.UserServiceImpl;
import Client.controll.FunctionController;
import Client.util.UserReadObject;
import Client.util.UserReadThread;
import Client.view.MainView.FunctionView;
import Client.view.MainView.MainView;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;
import java.util.Scanner;
@Data
@NoArgsConstructor
/**
 * @author blue
 * @date 2023/1/12 14:30
 **/
public class UserView {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserServiceImpl();
    private Client client;
    private ByteBuffer buffer;
    public UserView(Client client,ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }
    public void loginView(){
        client.sendMsg("用户登录");
        System.out.println("请输入你的用户名");
        String account = scanner.nextLine();
        System.out.println("请输入你的密码");
        String password = scanner.nextLine();
        client.sendMsg(account+"~"+password);
        client.readObject(buffer);
        if (client.getUser()!= null){
            System.out.println("登录成功");
            System.out.println("当前登录用户为:"+client.getUser().getAccount());
            System.out.println("当前昵称:"+client.getUser().getNickname());
            System.out.println("当前用户账户余额为:"+client.getUser().getBalance());
            new FunctionView(client,buffer).functionView();
        }
        else {
            System.out.println("用户名或密码错误，登录失败");
            new MainView(client,buffer).mainView();
        }
    }

    public void registerView(){
        client.sendMsg("用户注册");
//        UserReadThread urd = new UserReadThread(buffer,client);
//        urd.start();
        System.out.println("请输入不超过10位用户名");
        String account = scanner.nextLine();
        while (account.length() > 10 || account.contains("~") || account.contains("@")) {
            System.out.println("用户名格式错误");
            account = scanner.nextLine();
        }
        System.out.println("请输入密码");
        String password = scanner.nextLine();
        System.out.println("请输入电话号码");
        String telephone = scanner.nextLine();
        client.sendMsg(account+"~"+password+"@"+telephone);
        client.readMsg(buffer);
        new MainView(client,buffer).mainView();
    }

}
