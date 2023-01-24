package Server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**购物车详情实体
 * @author xingmeng
 * @date 2023/1/24 19:16
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartsDetail {
    private int id;
    private int productId;
    private int cartsId;
    private int number;
    private int status;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
