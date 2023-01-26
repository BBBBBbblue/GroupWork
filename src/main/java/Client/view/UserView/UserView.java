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
/** 用户操作界面
 * @author blue
 * @date 2023/1/12 14:30
 **/
public class UserView {
    private static Scanner scanner = new Scanner(System.in);
    private static UserService userService = new UserServiceImpl();
    private Client client;
    private ByteBuffer buffer;

    public UserView(Client client, ByteBuffer buffer) {
        this.client = client;
        this.buffer = buffer;
    }

    public void loginView() {
        System.out.println("请输入你的用户名");
        String account = scanner.nextLine();
        System.out.println("请输入你的密码");
        String password = scanner.nextLine();
        client.sendMsg("用户登录"+account + "~" + password);
        client.readObject(buffer);
        if (client.getUser() != null) {
            System.out.println("登录成功");
            System.out.println("当前登录用户为:" + client.getUser().getAccount());
            System.out.println("当前昵称:" + client.getUser().getNickname());
            System.out.println("当前用户账户余额为:" + client.getUser().getBalance());
            new FunctionView(client, buffer).functionView();
        } else {
            System.out.println("用户名或密码错误，登录失败");
            new MainView(client, buffer).mainView();
        }
    }

    public void registerView() {
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
        client.sendMsg("用户注册"+account + "~" + password + "@" + telephone);
        System.out.println(client.readMsg(buffer));
        new MainView(client, buffer).mainView();
    }

    public void updateView() {
        System.out.println("当前用户状态");
        System.out.println("===========================================================");
        System.out.println("登录用户为:" + client.getUser().getAccount());
        System.out.println("当前昵称:" + client.getUser().getNickname());
        System.out.println("当前余额:" + client.getUser().getBalance());
        System.out.println("当前电话为:" + client.getUser().getTelephone());
        System.out.println("注册邮箱号码为:" + client.getUser().getEmail());
        System.out.println("密码保护问题:" + client.getUser().getSecurityQuestion());
        System.out.println("密码保护答案:" + client.getUser().getSecurityAnswer());
        System.out.println("==============================");
        System.out.println("请输入修改后昵称");
        String nickname = scanner.nextLine();
        System.out.println("请输入修改后邮箱");
        String email = scanner.nextLine();
        client.sendMsg("修改资料"+nickname + "~" + email + "!" + client.getUser().getAccount());
        String str = client.readMsg(buffer);
        System.out.println(str);
        if (str.equals("修改成功")){
            client.getUser().setNickname(nickname);
            client.getUser().setEmail(email);
        }
        new FunctionView(client, buffer).functionView();
    }

    public void chargeView() {
        System.out.println("请输入你的充值金额！！！！");
        int money = scanner.nextInt();
        client.sendMsg("用户充值"+client.getUser().getAccount() + "~" + money + ".00");
        String msg = client.readMsg(buffer);
        System.out.println(msg);
        if (msg.equals("充值完成")) {
            client.getUser().setBalance(client.getUser().getBalance()+money);
            System.out.println("当前用户余额：" + client.getUser().getBalance());
        }
        new FunctionView(client, buffer).functionView();
    }



}
