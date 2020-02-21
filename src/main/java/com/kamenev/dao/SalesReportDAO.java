package com.kamenev.dao;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SalesReportDAO {
    Connection connection;

    public SalesReportDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(String model, int income, String date) throws SQLException, ParseException {
        PreparedStatement pst = connection.prepareStatement("INSERT INTO salesreport (product_id," +
                "income, " +
                "date) VALUES (?, ?, ?)");
        long id = (new ProductDAO(connection)).getId(model);
        pst.setLong(1, id);
        pst.setInt(2, income);
        pst.setDate(3, new java.sql.Date(new SimpleDateFormat("yyyy.MM.dd").parse(date).getTime()));
        pst.executeUpdate();
    }

    public int getIncome(String model, String date) throws SQLException, ParseException {
        PreparedStatement pst = connection.prepareStatement("SELECT income FROM salesreport WHERE " +
                "product_id = (?) AND date <= (?)");
        long id = (new ProductDAO(connection)).getId(model);
        pst.setLong(1, id);
        Date datesql = new java.sql.Date(new SimpleDateFormat("yyyy.MM.dd").parse(date).getTime());
        pst.setDate(2, datesql);
        ResultSet set = pst.executeQuery();
        int income = 0;
        while (set.next()) {
            income += set.getInt(1);
        }
        return income;
    }
}
