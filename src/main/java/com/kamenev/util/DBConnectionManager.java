package com.kamenev.util;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnectionManager {
    private static Connection connection;

    public DBConnectionManager() throws SQLException {
        Properties properties = new Properties();
        try {
            properties.load(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/dbConnection.properties"))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String URL = properties.getProperty("URL");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to create JDBC db connection " + e.toString() + e.getMessage());
        }
    }

    public static Connection getConnection() {
        return connection;
    }
/*

    private void createDB(Properties properties, String name) throws SQLException {
        String URL = properties.getProperty("URL") + name;
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, username, password);
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to create JDBC db connection " + e.toString() + e.getMessage());
        }
    }

 */
}
