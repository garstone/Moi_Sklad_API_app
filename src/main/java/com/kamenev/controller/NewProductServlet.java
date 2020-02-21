package com.kamenev.controller;

import com.google.gson.Gson;
import com.kamenev.model.Product;
import com.kamenev.service.ProductService;
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

@WebServlet(urlPatterns = {"/newproduct"})
public class NewProductServlet extends HttpServlet {
    Logger logger = LogManager.getRootLogger();
    private Connection connection = DBConnectionManager.getConnection();

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = IOUtils.toString(request.getReader());
        JSONObject obj = new JSONObject(json);
        String model = obj.getString("model");
        ProductService productService = new ProductService(connection);
        try {
            if (productService.find(model)) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Данная модель уже добавлена в базу данных.");
            } else {
                productService.insert(model);
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("Данная модель успешно добавлена в базу данных.");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
