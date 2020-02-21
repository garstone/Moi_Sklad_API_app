package com.kamenev.controller;

import com.google.gson.Gson;
import com.kamenev.model.Store;
import com.kamenev.service.ProductService;
import com.kamenev.service.StoreService;
import com.kamenev.util.DBConnectionManager;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/purchase")
public class PurchaseServlet extends HttpServlet {
    Logger logger = LogManager.getRootLogger();
    private Connection connection = DBConnectionManager.getConnection();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = IOUtils.toString(req.getReader());
            Store purchase = new Gson().fromJson(json, Store.class);
            String model = purchase.getModel();
            int quantity = purchase.getQuantity();
            int price = purchase.getPrice();
            String date = purchase.getDate();
            StoreService storeService = new StoreService(connection);
            if (storeService.add(model, quantity, price, date) == -1) {
                resp.setContentType("text/html;charset=utf-8");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().println("Данная модель не добавлена в базу данных, закупку невозможно осуществить");
            }
            resp.setContentType("text/html;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println("Закупка успешно добавлена в базу данных.");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
