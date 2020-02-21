package config;
import org.dbunit.DBTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.xml.sax.InputSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUnitConfig extends DBTestCase {
    protected IDatabaseTester tester;
    private Properties prop;
    protected IDataSet beforeData;
    private Connection connection;


    @Before
    public void setUp() throws Exception {
        tester = new JdbcDatabaseTester(prop.getProperty("db.driver"),
                prop.getProperty("db.url"),
                prop.getProperty("db.username"),
                prop.getProperty("db.password"));
        connection = tester.getConnection().getConnection();
        createTables();
    }

    public DBUnitConfig(String name) throws Exception {
        super(name);
        prop = new Properties();
        try {
            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, prop.getProperty("db.driver"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, prop.getProperty("db.url"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, prop.getProperty("db.username"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, prop.getProperty("db.password"));
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_SCHEMA, "");
    }

    @Override
    protected IDataSet getDataSet() {
        return beforeData;
    }

    @Override
    protected DatabaseOperation getTearDownOperation() {
        return DatabaseOperation.DELETE_ALL;
    }

    public void createTables() {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
