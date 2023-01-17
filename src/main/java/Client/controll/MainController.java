package Client.controll;

import Client.Client;
import Client.view.UserView.UserView;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.ByteBuffer;

@Data
@NoArgsConstructor
/**
 * @author blue
 * @date 2023/1/12 14:24
 **/

public class MainController {
    private Client client;
    private ByteBuffer buffer;
    public MainController(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }
    public void mainChoose(int i){
        switch (i){
            case 1:
                new UserView(client,buffer).loginView();
                break;
            case 2:
                 new UserView(client,buffer).registerView();
                break;
            case 3:
                System.out.println("再见老板");
                break;
        }
    }
}
