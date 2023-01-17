package Server.DAO.impl;

import Server.DAO.UserDAO;
import lombok.Data;
import lombok.NoArgsConstructor;
import Server.pojo.User;
import Server.util.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Data
@NoArgsConstructor
/**
 * @author blue
 * @date 2023/1/12 14:35
 **/
public class UserDAOImpl implements UserDAO {
    private static Scanner scanner = new Scanner(System.in);
    private static List<String> accountList = new ArrayList<>();
    private static List<String> telList = new ArrayList<>();
    private String resMsg = null;

    static {
        String sql = "select telephone from Custom";
        String sql1 = "select account from Custom";
        try (Connection c = Connect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             PreparedStatement pss = c.prepareStatement(sql1);
        ) {
            ps.execute();
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                telList.add(resultSet.getString("telephone"));
            }
            pss.execute();
            ResultSet resultSet1 = pss.executeQuery();
            while (resultSet.next()){
                accountList.add(resultSet.getString("account"));
            }
        } catch (SQLException e) {
            System.out.println("初始化失败");
        }
    }

    @Override
    public User login(String account,String pwd) {

        String sql = "select * from Custom where account = ?";
        try (Connection connection = Connect.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
        ) {
            ps.setString(1, account);
            ps.execute();
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            String password = resultSet.getString("password");
            if (pwd.equals(password)) {
                boolean status = (1 == resultSet.getByte("status"));
                if (!status) {
                    System.out.println("用户已注销，请注册新用户");
                    return null;
                }
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setAccount(resultSet.getString("account"));
                user.setPassword(password);
                user.setBalance(resultSet.getFloat("balance"));
                user.setNickname(resultSet.getString("nickname"));
                user.setTelephone(resultSet.getString("telephone"));
                user.setEmail(resultSet.getString("email"));
                user.setSecurityQuestion(resultSet.getString("security_question"));
                user.setSecurityAnswer(resultSet.getString("security_answer"));
                user.setGmtCreate(resultSet.getTimestamp("gmt_create"));
                user.setGmtModified(resultSet.getTimestamp("gmt_modified"));
                return user;
            } else {
                System.out.println("用户名或密码错误,请重试");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("用户名不存在，请重试");
            return null;
        }


    }

    @Override
    public String register(String account,String password,String telephone) {
        if (accountList.contains(account)){
            resMsg = "用户名已存在";
            return resMsg;
        }
        else if (telList.contains(telephone)){
            resMsg = "当前号码已经注册";
            return resMsg;
        }
        String sql = "insert into Custom(account,password,telephone) values (?,?,?)";
        try (   Connection connection = Connect.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ) {
            ps.setString(1,account);
            ps.setString(2,password);
            ps.setString(3,telephone);
            ps.execute();
            resMsg = "注册成功，请尽快完善您的资料";
            return resMsg;
        }catch (SQLException e){
            resMsg = "未知错误";
            return resMsg;
        }
    }



}