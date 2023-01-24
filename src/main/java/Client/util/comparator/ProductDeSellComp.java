package Client.util.comparator;

import Server.pojo.Product;

import java.util.Comparator;

/**
 * @author blue
 * @date 2023/1/25 0:11
 **/
public class ProductDeSellComp implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        if (o1.getSellCount() > o2.getSellCount() ){
            return -1;
        }
        else if (o1.getSellCount() < o2.getSellCount()){
            return 1 ;
        }
        else {
            return 0;
        }
    }
}
