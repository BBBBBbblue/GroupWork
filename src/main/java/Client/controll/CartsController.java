package Client.controll;

import Client.Client;
import Client.view.MainView.CartsView;
import Client.view.MainView.ShopView;

import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author blue
 * @date 2023/1/30 20:29
 **/
public class CartsController {
    private static Scanner scanner = new Scanner(System.in);
    private Client client;
    private ByteBuffer buffer;

    public CartsController(Client client, ByteBuffer buffer){
        this.client = client;
        this.buffer = buffer;
    }

    public void cartsListChoose(int i){
        switch (i){
            case 1:
                new CartsView(client,buffer).changeNumber();
                break;
            case 2:
                new CartsView(client,buffer).removeProduct();
                break;
            case 3:
                new CartsView(client,buffer).addOrder();;
                break;
            case 4:
                new ShopView(client,buffer).shopView();
                break;
            default:break;
        }
    }
}
