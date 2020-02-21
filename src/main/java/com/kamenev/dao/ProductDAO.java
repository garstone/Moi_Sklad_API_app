package com.kamenev.dao;

import com.kamenev.model.Product;

import java.sql.*;
import java.util.HashSet;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public long getId(String model) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT id FROM products WHERE model = (?)");
        pst.setString(1, model);
        ResultSet set = pst.executeQuery();
        set.next();
        return set.getLong(1);
    }

    public String getById(long id) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT model FROM products where id = " + id);
        ResultSet set = pst.executeQuery();
        set.next();
        return set.getString(1);
    }

    public boolean find(String model) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("SELECT model FROM products WHERE model = (?)");
        pst.setString(1, model);
        ResultSet set = pst.executeQuery();
        return set.next();
    }

    public HashSet<String> findAll() throws SQLException {
        HashSet<String> set = new HashSet<>();
        PreparedStatement pst = connection.prepareStatement("SELECT * FROM products");
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            set.add(rst.getString(2));
        }
        return set;
    }

    public int insert(String model) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("INSERT INTO products (model) VALUES (?)");
        pst.setString(1, model);
        pst.executeUpdate();
        pst.close();
        return 1;
    }

}
