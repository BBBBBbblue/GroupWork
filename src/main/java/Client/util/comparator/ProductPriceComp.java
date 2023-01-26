package Client.util.comparator;

import Server.pojo.Product;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Comparator;

@EqualsAndHashCode(callSuper=false)

@Data
@NoArgsConstructor
/**  商品价格比较器
 * @author blue
 * @date 2023/1/19 22:44
 **/
public class ProductPriceComp implements Comparator<Server.pojo.Product>{

    @Override
    public int compare(Product o1, Product o2) {
        if (o1.getPrice() >= o2.getPrice()){
            return 1;
        }
        else {
            return -1;
        }

    }
}
