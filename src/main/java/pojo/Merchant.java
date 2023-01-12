package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@NoArgsConstructor
@Data
@AllArgsConstructor
/** 商户实体对应商户表
 * @author blue
 * @date 2023/1/12 15:02
 **/
public class Merchant {
    private int id;
    private String merchantName;
    private String account;
    private String password;
    private String telephone;
    private String email;
    private float charge;
    private boolean status;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
