package Server.DAO.impl;

import Server.DAO.OrderDao;
import Server.pojo.Order;
import Server.pojo.OrderDetail;
import Server.util.Connect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xingmeng
 * @date 2023/1/30 20:20
 */
public class OrderDaoImpl implements OrderDao {
    @Override
    public int createOrder(Order order) {
        String sql = "insert into orders(user_id,ordercode,receive_addr,receive_name,telephone,price) values (?,?,?,?,?,?)";
        int tem = 0;
        try(
                Connection conn = Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ){
            pstmt.setInt(1,order.getUserId());
            pstmt.setString(2,order.getOrderCode());
            pstmt.setString(3,order.getReceiveAddr());
            pstmt.setString(4,order.getReceiveName());
            pstmt.setInt(5,order.getTelPhone());
            pstmt.setDouble(6,order.getPrice());
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
    public void createOrderDetail(OrderDetail orderDetail) {
        String sql = "insert into orders_detail(product_id,orders_id,number) values (?,?,?)";
        try(
                Connection conn = Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,orderDetail.getProductId());
            pstmt.setInt(2,orderDetail.getOrderId());
            pstmt.setInt(3,orderDetail.getNumber());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> selectOrderByUserId(int userId, int status) {
        List<Order> orders = new ArrayList<>();
        String sql = "select * from orders where user_id = ? and status = ?";
        try(
                Connection conn = Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,userId);
            pstmt.setInt(2,status);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                Order order = new Order();
                order.setId(rs.getInt(1));
                order.setUserId(rs.getInt(2));
                order.setOrderCode(rs.getString(3));
                order.setReceiveAddr(rs.getString(4));
                order.setReceiveName(rs.getString(5));
                order.setTelPhone(rs.getInt(6));
                order.setPrice(rs.getDouble(7));
                order.setStatus(rs.getInt(8));
                order.setGmtCreate(rs.getTimestamp(9));
                order.setGmtModified(rs.getTimestamp(10));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<OrderDetail> selectOrderDetailByOrderId(int orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "select * from orders where orders_id = ?";
        try(
                Connection conn = Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setId(rs.getInt(1));
                orderDetail.setProductId(rs.getInt(2));
                orderDetail.setOrderId(rs.getInt(3));
                orderDetail.setNumber(rs.getInt(4));
                orderDetail.setGmtCreate(rs.getTimestamp(5));
                orderDetail.setGmtModified(rs.getTimestamp(6));
                orderDetails.add(orderDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    @Override
    public void updateOrderStatus(int orderId, int status) {
        String sql = "update orders set status = ?,gmt_modified = ? where id = ?";
        try(
                Connection conn =Connect.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        ){
            pstmt.setInt(1,status);
            pstmt.setTimestamp(2,new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3,orderId);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
