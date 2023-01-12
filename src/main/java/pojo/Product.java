package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@AllArgsConstructor
@NoArgsConstructor
/** 商品实体对应商品表
 * @author blue
 * @date 2023/1/12 15:05
 **/
public class Product {
    private int id;
    private String productName;
    private float price;
    private int inventory;
    private int sellCount;
    private int categoriesId;
    private int merchantId;
    private int status;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
