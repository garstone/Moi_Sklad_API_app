package com.kamenev.service;

import com.kamenev.dao.ProductDAO;
import com.kamenev.dao.SalesReportDAO;
import com.kamenev.dao.StoreDAO;
import com.kamenev.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

public class SalesReportService {
    Connection connection;

    public SalesReportService(Connection connection) {
        this.connection = connection;
    }

    public void add(String model, int income, String date) throws SQLException, ParseException {
        new SalesReportDAO(connection).add(model, income, date);
    }

    public int getIncome(String model, String date) throws SQLException, ParseException {
        if (!(new ProductDAO(connection).find(model))) {
            return -1;
        }
        return (new SalesReportDAO(connection).getIncome(model, date));
    }
}
