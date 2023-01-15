package Server.DAO.impl;

import Server.DAO.ServerDAO;
import Server.util.Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author blue
 * @date 2023/1/15 19:07
 **/
public class ServerDAOImpl implements ServerDAO {
    @Override
    public String response(String question) {
        String sql = "select answer from reply where question = ?";
        String ans = "坤坤不明白你的意思";
        try(    Connection connection = Connect.getConnection();
                PreparedStatement ps = connection.prepareStatement(sql);
                ){
            ps.setString(1,question);
            ps.execute();
            ResultSet resultSet = ps.executeQuery();
            resultSet.next();
            ans = resultSet.getString("answer");
        }catch (SQLException e){
            System.out.println("用户问题为："+question+",数据库没有对应回复，请尽快更新");
        }
        return ans;
    }
}
