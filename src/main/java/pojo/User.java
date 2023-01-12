package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.text.DecimalFormat;
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * @author blue
 * @date 2023/1/12 14:32
 **/
public class User {
    private int id;
    private String account;
    private String password;
    private String nickname;
    private String telephone;
    private String email;
    private float balance;
    private String securityQuestion;
    private String securityAnswer;
    private boolean status;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
