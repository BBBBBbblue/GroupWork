package Server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Data

/** 管理员实体对应管理员表
 * @author blue
 * @date 2023/1/12 14:54
 **/
public class Admin {
    private int id;
    private String password;
    private String account;
    private boolean status;
    private int purview;
    private Timestamp gmtCreate;
    private Timestamp gmtModified;
}
