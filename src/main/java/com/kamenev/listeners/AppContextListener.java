package com.kamenev.listeners;

import com.kamenev.util.DBConnectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.postgresql.util.PSQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebListener
public class AppContextListener implements ServletContextListener {
    private Connection connection;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        Logger logger = LogManager.getRootLogger();

        ServletContext context = servletContextEvent.getServletContext();
        System.out.println("Context from AppContextListener initialized");
        System.out.println("Context path is: localhost:8080" + context.getContextPath());

        try {
            DBConnectionManager connectionManager = new DBConnectionManager();
            context.setAttribute("DBConnection", connectionManager.getConnection());

            this.connection = connectionManager.getConnection();

            System.out.println("Connection to database established successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //createDatabase();
        createTables();

    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        Connection con = (Connection) servletContextEvent.getServletContext().getAttribute("DBConnection");
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*

    public void createDatabase() {
        System.out.println("Creating database...");
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE moi_sklad";
            statement.executeUpdate(sql);
            System.out.println("Database created successfully");
        } catch (SQLException e) {
            if (e.getErrorCode() == 0) {
                System.out.println("The table exists already");
            } else {
                e.printStackTrace();
            }
        }
    }

 */

    public void createTables() {
        System.out.println("Creating tables if they don't exist...");
        try {
            Statement statement = connection.createStatement();
            String sqlProduct = "CREATE TABLE IF NOT EXISTS products (" +
                    "id bigserial not null PRIMARY KEY," +
                    "model varchar(100) not null" +
                    ")";
            String sqlPurchase = "CREATE TABLE IF NOT EXISTS store (" +
                    "id bigserial not null PRIMARY KEY," +
                    "product_id int8 not null," +
                    "quantity int," +
                    "price int," +
                    "date date," +
                    "updated_at date" +
                    ")";
            String sqlSalesReport = "CREATE TABLE IF NOT EXISTS salesreport (" +
                    "id bigserial not null PRIMARY KEY," +
                    "product_id int8 not null," +
                    "income int8 not null," +
                    "date date)";
            statement.executeUpdate(sqlProduct);
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate(sqlPurchase);
            statement.close();
            statement = connection.createStatement();
            statement.executeUpdate(sqlSalesReport);
            statement.close();
            System.out.println("If tables don't exist they created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
