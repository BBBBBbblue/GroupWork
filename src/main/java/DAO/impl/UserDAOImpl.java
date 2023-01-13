package DAO.impl;

import DAO.UserDAO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pojo.User;
import util.Connect;

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
    private User loginUser;
    private static List<Long> telList = new ArrayList<>();

    public UserDAOImpl(User user) {
        loginUser = user;
    }

    static {
        String sql = "select telephone from Custom";
        try (Connection c = Connect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
        ) {
            ps.execute();
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                telList.add(resultSet.getLong("telephone"));
            }
        } catch (SQLException e) {
            System.out.println("初始化失败");
        }
    }

    @Override
    public User login() {
        System.out.println("请输入用户名");
        String account = scanner.nextLine();
        System.out.println("请输入密码");
        String pwd = scanner.nextLine();
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
                user.setTelephone(resultSet.getLong("telephone"));
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
    public boolean register() {
        System.out.println("请输入不超过10位用户名");
        String account = scanner.nextLine();
        if (account.length() > 10) {
            System.out.println("用户名格式错误");
            return false;
        }
        System.out.println("请输入密码");
        String password = scanner.nextLine();
        String sql = "select * from Custom where account = ?";
        String s = "insert into Custom(account,password,nickname,telephone) values (?,?,?,?)";
        try (
                Connection connection = Connect.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                PreparedStatement pss = connection.prepareStatement(s);
        ) {
            ps.setString(1, account);
            ps.execute();
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                System.out.println("用户名已存在");
                return false;
            } else {
                pss.setString(1, account);
                pss.setString(2, password);
                System.out.println("输入你的昵称");
                String nickname = scanner.nextLine();
                pss.setString(3, nickname);
                System.out.println("请输入你的电话");
                long tel = scanner.nextLong();
                if (telList.contains(tel)) {
                    System.out.println("电话号码已存在");
                    return false;
                }
                pss.setLong(4, tel);
                pss.execute();
                System.out.println("注册成功,为了账户安全，请尽快完善信息");
                telList.add(tel);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("服务器异常");
            return false;
        }

    }
}
