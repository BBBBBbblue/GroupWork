package Server.DAO.impl;

import Server.DAO.UserDAO;
import Server.pojo.Product;
import Server.util.comparator.ProductPriceComp;
import lombok.Data;
import lombok.NoArgsConstructor;
import Server.pojo.User;
import Server.util.Connect;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
/**
 * @author blue
 * @date 2023/1/12 14:35
 **/
public class UserDAOImpl implements UserDAO {
    private static Scanner scanner = new Scanner(System.in);
    private List<String> accountList = new ArrayList<>();
    private List<String> telList = new ArrayList<>();
    private String resMsg = null;
    private HashMap<String,Float> userMap = new HashMap<>();
    private SocketChannel channel;
    private HashMap<Product,Integer> products;


    public void init() {
        products = new HashMap<Product, Integer>();
        String sql = "select * from Custom";
        try (Connection c = Connect.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
        ) {
            ps.execute();
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                telList.add(resultSet.getString("telephone"));
                accountList.add(resultSet.getString("account"));
            }
        } catch (SQLException e) {
            System.out.println("初始化失败");
        }
        System.out.println(telList.toString());
        System.out.println(accountList.toString());

        String sql1 = "select * from product";
        try(    Connection connection = Connect.getConnection();
                PreparedStatement pss = connection.prepareStatement(sql1);
                ){
            pss.execute();
            ResultSet resultSet = pss.executeQuery();
            while (resultSet.next()){
                if (resultSet.getInt("status") == 1) {
                    Product product = new Product();
                    product.setId(resultSet.getInt("id"));
                    product.setProductName(resultSet.getString("product_name"));
                    product.setPrice(resultSet.getFloat("price"));
                    product.setSellCount(resultSet.getInt("sellCount"));
                    product.setInventory(resultSet.getInt("inventory"));
                    product.setCategoriesId(resultSet.getInt("categories_id"));
                    product.setMerchantId(resultSet.getInt("merchant_id"));
                    products.put(product,product.getMerchantId());
                }
            }
            System.out.println(products.entrySet());
        }catch (SQLException e){
            System.out.println("加载商品出错");
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
                userMap.put(account,user.getBalance());
                System.out.println(userMap.entrySet());
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
            telList.add(telephone);
            accountList.add(account);
            return resMsg;
        }catch (SQLException e){
            resMsg = "未知错误";
            return resMsg;
        }
    }

    @Override
    public String update(String nickname, String email,String account) {
        String msg = null;
        String sql = "update Custom set nickname = ?,email = ? where account = ?";
        try(    Connection connection = Connect.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ){
            ps.setString(1,nickname);
            ps.setString(2,email);
            ps.setString(3,account);
            ps.execute();
            msg = "修改成功";
            return msg;
        }catch (SQLException e){
            msg = "修改失败";
            return msg;
        }
    }

    @Override
    public String charge(String account,String money) {
        String resMsg = "未知错误";
        String sql = "update Custom set balance = ? where account = ?";
        float balance = userMap.get(account);
        float addMoney = Float.parseFloat(money);
        balance = balance + addMoney;
        try(    Connection connection = Connect.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ){
            ps.setFloat(1,balance);
            ps.setString(2,account);
            ps.execute();
            resMsg = "充值完成";
            userMap.put(account,balance);
            return resMsg;
        }catch (SQLException e){
            return resMsg;
        }
    }

    @Override
    public String pay(String account, float money) {
        float balance = userMap.get(account);
        if (money > balance ){
            return new String("账户余额不足");
        }
        else {
            String sql = "update Custom set balance = ? where account = ?";
            balance = balance - money;
            try(    Connection connection = Connect.getConnection();
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ){
                ps.setFloat(1,balance);
                ps.setString(2,account);
                ps.execute();
                userMap.put(account,balance);
                System.out.println(userMap.entrySet());
                return new String("消费成功");
            }catch (SQLException e){
                return new String("未知错误");
            }
        }
    }
}