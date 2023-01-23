package Client.controll;

import Client.Client;
import Client.view.MainView.ShopView;
import Client.view.UserView.UserView;

import java.nio.ByteBuffer;

/**
 * @author blue
 * @date 2023/1/24 1:06
 **/
public class ShopController {
    private Client client;
    private ByteBuffer buffer;

    public ShopController(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }

    public void shopChoose(int i){
        switch (i){
            case 1:new ShopView(client,buffer).productsView();
                break;
            case 2:new ShopView(client,buffer).cartsView();
                break;
            default:break;
        }
    }
}
