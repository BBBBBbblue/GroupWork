package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author blue
 * @date 2023/1/11 13:41
 **/
public class Connect {
    private static Connection connection;
    private static String url;
    private static String driver;
    private static String username;
    private static String password;

    static {
        File configFile = new File("D:\\java_groupWork\\src\\main\\java\\util\\util.properties");
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(configFile));
        }catch (IOException e){
            System.out.println("读取配置文件失败");
        }
        driver = (String) config.get("driver");
        url = (String) config.get("url");
        username = (String) config.get("username");
        password = (String) config.get("password");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("驱动加载失败，请检查jdbc驱动");
        }
    }

    public static Connection getConnection (){
        try {
            connection = DriverManager.getConnection(url,username,password);
        } catch (SQLException e) {
            System.out.println("配置数据出错，请检查路径及用户密码");
            return null;
        }
        return connection;
    }

}
