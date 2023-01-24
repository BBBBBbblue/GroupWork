package Client.controll;

import Client.Client;
import Client.util.comparator.ProductDePriceComp;
import Client.util.comparator.ProductDeSellComp;
import Client.util.comparator.ProductPriceComp;
import Client.view.MainView.FunctionView;
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
            case 3:new FunctionView(client,buffer).functionView();
            default:break;
        }
    }

    public void shopListChoose(int i){
        switch (i){
            case 1:
                ProductPriceComp comp = new ProductPriceComp();
                new ShopView(client,buffer).productsView(comp);
                break;
            case 2:
                ProductDePriceComp comp1 = new ProductDePriceComp();
                new ShopView(client,buffer).productsView(comp1);
                break;
            case 3:
                ProductDeSellComp comp2 = new ProductDeSellComp();
                new ShopView(client,buffer).productsView(comp2);
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:new ShopView(client,buffer).shopView();
            default:break;
        }

    }
}
