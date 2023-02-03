package Client.controll;

import Client.Client;
import Client.view.MainView.FunctionView;
import Client.view.MainView.ShopView;

import java.nio.ByteBuffer;

/**
 * @author blue
 * @date 2023/2/3 19:21
 **/
public class SearchController {
    private Client client;
    private ByteBuffer buffer;

    public SearchController(Client client, ByteBuffer buffer) {
        this.client = client;
        this.buffer = buffer;
    }

    public void searchListChoose(int i) {
        switch (i) {
            case 1:
                new ShopView(client,buffer).addOrder();
                break;
            case 2:
                new ShopView(client,buffer).addCartsDetail();
                break;
            case 3:
                new ShopView(client,buffer).shopView();
                break;
            default:
                break;
        }
    }
}
