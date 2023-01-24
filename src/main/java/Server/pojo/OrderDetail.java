package Server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**订单实体详情实体
 * @author xingmeng
 * @date 2023/1/24 19:49
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDetail {
    private int id;
    private int productId;
    private int orderId;
    private int number;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
