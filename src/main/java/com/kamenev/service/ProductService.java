package com.kamenev.service;

import com.kamenev.dao.ProductDAO;
import com.kamenev.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

public class ProductService {
    private Connection connection;

    public ProductService(Connection connection) {
        this.connection = connection;
    }

    public long getId(String model) throws SQLException {
        ProductDAO dao = new ProductDAO(connection);
        return dao.getId(model);
    }

    public String getById(long id) throws SQLException {
        ProductDAO dao = new ProductDAO(connection);
        return dao.getById(id);
    }

    public boolean find(String model) throws SQLException {
        ProductDAO dao = new ProductDAO(connection);
        return dao.find(model);
    }

    public HashSet<String> findAll() throws SQLException {
        ProductDAO dao = new ProductDAO(connection);
        return dao.findAll();
    }

    public int insert(String model) throws SQLException {
        ProductDAO dao = new ProductDAO(connection);
        return dao.insert(model);
    }
}
