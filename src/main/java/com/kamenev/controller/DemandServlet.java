package com.kamenev.controller;

import com.google.gson.Gson;
import com.kamenev.model.Product;
import com.kamenev.model.Store;
import com.kamenev.service.StoreService;
import com.kamenev.util.DBConnectionManager;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

@WebServlet("/demand")
public class DemandServlet extends HttpServlet {
    Logger logger = LogManager.getRootLogger();
    private Connection connection = DBConnectionManager.getConnection();

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = IOUtils.toString(request.getReader());
        Store demand = new Gson().fromJson(json, Store.class);
        int income;
        try {
            String model = demand.getModel();
            int quantity = demand.getQuantity();
            int price = demand.getPrice();
            String date = demand.getDate();
            income = new StoreService(connection).sale(model, quantity, price, date);
            if (income == -1) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Неверное количество товара.");
                return;
            }
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("{\"income\": " + income + "}");
        } catch (SQLException | IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
