package Server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**订单实体
 * @author xingmeng
 * @date 2023/1/24 19:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int id;
    private int userId;
    private String orderCode;
    private String receiveAddr;
    private String receiveName;
    private long telPhone;
    private double price;
    private int status;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
