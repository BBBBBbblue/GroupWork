package Server.DAO.impl;

import Server.DAO.CartsDao;
import Server.pojo.CartsDetail;
import Server.util.Connect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xingmeng
 * @date 2023/1/30 19:00
 */
public class CartsDaoImpl implements CartsDao {
    @Override
    public List<CartsDetail> selectCartsByUserId(int userId,int status) {
        List<CartsDetail> carts = new ArrayList<>();
        String sql = "select * from carts_detail where carts_id = (select id from Carts where user_id = ?) and status = ?";
        try(
                Connection conn =Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ){
            pstmt.setInt(1,userId);
            pstmt.setInt(2,status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                CartsDetail cart = new CartsDetail();
                cart.setId(rs.getInt(1));
                cart.setProductId(rs.getInt(2));
                cart.setCartsId(rs.getInt(3));
                cart.setNumber(rs.getInt(4));
                cart.setStatus(rs.getInt(5));
                cart.setGmtCreate(rs.getTimestamp(6));
                cart.setGmtModified(rs.getTimestamp(7));
                carts.add(cart);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carts;
    }

    @Override
    public CartsDetail selectCartsByCartDetailId(int cartDetailId) {
        CartsDetail cart = new CartsDetail();
        String sql = "select * from carts_detail where id = ?";
        try(
                Connection conn =Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,cartDetailId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                cart.setId(rs.getInt(1));
                cart.setProductId(rs.getInt(2));
                cart.setCartsId(rs.getInt(3));
                cart.setNumber(rs.getInt(4));
                cart.setStatus(rs.getInt(5));
                cart.setGmtCreate(rs.getTimestamp(6));
                cart.setGmtModified(rs.getTimestamp(7));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    @Override
    public int createCarts(int userId) {
        String sql = "insert into Carts(user_id) values (?)";
        int tem = 0;
        try(
                Connection conn =Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            pstmt.setInt(1,userId);
            pstmt.execute();
            ResultSet rs = pstmt.getGeneratedKeys();
            if(rs.next()){
                tem = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tem;
    }

    @Override
    public void createCartsDetail(CartsDetail carts) {
        String sql = "insert into carts_detail(product_id,carts_id,number) values (?,?,?)";
        try(
                Connection conn =Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,carts.getProductId());
            pstmt.setInt(2,carts.getCartsId());
            pstmt.setInt(3,carts.getNumber());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCartsDetailById(int cartsDetailId) {
        String sql = "update carts_detail set status = 0,gmt_modified = ? where id = ?";
        try(
                Connection conn =Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setTimestamp(1,new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2,cartsDetailId);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCartsDetailById(CartsDetail carts) {
        String sql = "update carts_detail set number = ?,gmt_modified = ? where id = ?";
        try(
                Connection conn =Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,carts.getNumber());
            pstmt.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3,carts.getId());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
