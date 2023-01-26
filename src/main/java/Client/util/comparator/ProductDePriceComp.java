package Client.util.comparator;

import Server.pojo.Product;

import java.util.Comparator;

/**
 * @author blue
 * @date 2023/1/25 0:08
 **/
public class ProductDePriceComp implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        if (o1.getPrice() >= o2.getPrice()){
            return -1;
        }
        else {
            return 1;
        }
    }
}
