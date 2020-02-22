package com.kamenev.service;

import com.kamenev.dao.ProductDAO;
import com.kamenev.dao.StoreDAO;
import com.kamenev.model.Store;
import com.kamenev.util.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;

public class StoreService {
    private Connection connection;

    public StoreService(Connection connection) {
        this.connection = connection;
    }

    public int add(String model, int quantity, int price, String date) throws SQLException {
        try {
            StoreDAO dao = new StoreDAO(connection);
            ProductDAO pDAO = new ProductDAO(connection);
            if (!pDAO.find(model)) {
                return -1;
            }
            return dao.add(model, quantity, price, date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int add(String model, int quantity, int price, String date, String updated_at) throws SQLException {
        try {
            StoreDAO dao = new StoreDAO(connection);
            ProductDAO pDAO = new ProductDAO(connection);
            if (!pDAO.find(model)) {
                return -1;
            }
            return dao.add(model, quantity, price, date, updated_at);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // FIFO реализация, возвращает суммарную цену закупки всех товаров
    public int pop(String model, int quantity) throws SQLException {
        int q = 0;
        int price = 0;
        HashSet<Long> ids = new HashSet<>();
        StoreDAO dao = new StoreDAO(connection);
        long productId = dao.getProductId(model);
        ArrayList<Long> idsDB = dao.getAllByProductId(productId); // ids from database
        for (Long id : idsDB) {
            Store party = getById(id);
            int partyPrice = party.getPrice();
            int partyQuantity = party.getQuantity();
            if (q + party.getQuantity() < quantity) { // если не добрали, идем дальше
                q += party.getQuantity();
                price += partyPrice * partyQuantity;
                dao.delete(id);
            } else if (party.getQuantity() == quantity - q){ // если норм то выходим из цикла
                dao.delete(id);
                return price + partyPrice * partyQuantity;
            } else {
                int c = quantity - q; //столько товаров забираем из этой строчки
                dao.update(id, partyQuantity - c); // обновляем бд
                return price + partyPrice * c;
            }
        }
        return -1; // не хватило товаров =(
    }

    public Store getById(long id) throws SQLException {
        StoreDAO dao = new StoreDAO(connection);
        return dao.getById(id);
    }

    // расчет прибыли с продажи и добавление в репорт базу
    public int sale(String model, int quantity, int price, String date) throws SQLException, ParseException {
        int count = getCount(model);
        if (count == -1 || count < quantity) {
            return -1;
        }
        int income = price * quantity - pop(model, quantity);
        addToSalesReport(model, income, date);
        return income;
    }

    // how much products are at the store
    private int getCount(String model) throws SQLException {
        ProductDAO pDAO = new ProductDAO(connection);
        if (!pDAO.find(model)) {
            return -1;
        }
        StoreDAO dao = new StoreDAO(connection);
        return dao.getCount(pDAO.getId(model));
    }

    private void addToSalesReport(String model, int income, String date) throws SQLException, ParseException {
        SalesReportService salesReportService = new SalesReportService(connection);
        salesReportService.add(model, income, date);
    }
}
