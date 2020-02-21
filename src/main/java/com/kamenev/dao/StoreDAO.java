package com.kamenev.dao;

import com.kamenev.model.Product;
import com.kamenev.model.Store;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.SortedSet;

public class StoreDAO {
    private Connection connection;

    public StoreDAO(Connection connection) {
        this.connection = connection;
    }

/*
    public int add(String model, int quantity, int price) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("INSERT INTO store (product_id, " +
                "quantity, " +
                "price, " +
                "date) VALUES (?, ?, ?, ?)");
        ProductDAO pDAO = new ProductDAO(connection);
        pst.setLong(1, pDAO.getId(model));
        pst.setInt(2, quantity);
        pst.setInt(3, price);
        pst.setDate(4, (Date) new SimpleDateFormat("yyyy.MM.dd").format(Calendar.getInstance().getTime()));
        pst.executeUpdate();
        pst.close();
        return 1;
    }

 */

    public int add(String model, int quantity, int price, String date) throws SQLException, ParseException {
        PreparedStatement pst = connection.prepareStatement("INSERT INTO store (product_id, " +
                "quantity, " +
                "price, " +
                "date) VALUES (?, ?, ?, ?)");
        ProductDAO pDAO = new ProductDAO(connection);
        pst.setLong(1, pDAO.getId(model));
        pst.setInt(2, quantity);
        pst.setInt(3, price);
        pst.setDate(4, new java.sql.Date(new SimpleDateFormat("yyyy.MM.dd").parse(date).getTime()));
        pst.executeUpdate();
        pst.close();
        return 1;
    }

    public int add(String model, int quantity, int price, String date, String updated_at) throws SQLException, ParseException {
        PreparedStatement pst = connection.prepareStatement("INSERT INTO store (product_id, " +
                "quantity, " +
                "price, " +
                "date, " +
                "updated_at) VALUES (?, ?, ?, ?, ?)");
        ProductDAO pDAO = new ProductDAO(connection);
        pst.setLong(1, pDAO.getId(model));
        pst.setInt(2, quantity);
        pst.setInt(3, price);
        pst.setDate(4, new java.sql.Date(new SimpleDateFormat("yyyy.MM.dd").parse(date).getTime()));
        pst.setDate(5, new java.sql.Date(new SimpleDateFormat("yyyy.MM.dd").parse(updated_at).getTime()));
        pst.executeUpdate();
        pst.close();
        return 1;
    }

    public int delete(long id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("DELETE FROM store WHERE id = (?)");
        pst.setLong(1, id);
        pst.executeUpdate();
        return 1;
    }

    // return price
    public int update(long id, int quantity) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("UPDATE store SET quantity = (?) WHERE id = (?)");
        pst.setInt(1, quantity);
        pst.setLong(2, id);
        pst.executeUpdate();
        return 1;
    }

    public long getProductId(String model) throws SQLException {
        ProductDAO pDAO = new ProductDAO(connection);
        return pDAO.getId(model);
    }

    public ArrayList<Long> getAllByProductId(long productId) throws SQLException {
        ArrayList<Long> idsDB = new ArrayList<>();
        PreparedStatement pst = connection.prepareStatement("SELECT id FROM store WHERE " +
                "product_id = (?) ORDER BY date");
        pst.setLong(1, productId);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            idsDB.add(rs.getLong(1));
        }
        return idsDB;
    }

    public Store getById(long id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM store WHERE id = (?)");
        pst.setLong(1, id);
        ResultSet rs = pst.executeQuery();
        rs.next();
        return new Store(rs.getLong(2), rs.getInt(3), rs.getInt(4),
                rs.getString(5));
    }

    public int getCount(long id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT quantity FROM store WHERE product_id = (?)");
        pst.setLong(1, id);
        ResultSet rs = pst.executeQuery();
        int count = 0;
        while (rs.next()) {
            count += rs.getInt(1);
        }
        return count;
    }
}
