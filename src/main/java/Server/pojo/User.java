package Server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
/** 用户实体对应顾客表
 * @author blue
 * @date 2023/1/12 14:32
 **/
public class User implements Serializable {
    private int id;
    private String account;
    private String password;
    private String nickname;
    private long telephone;
    private String email;
    private float balance;
    private String securityQuestion;
    private String securityAnswer;
    private boolean status;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
    private static final long serialVersionUID = 1L;

}
