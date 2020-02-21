package com.kamenev.controller;

import com.kamenev.service.SalesReportService;
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

@WebServlet("/salesreport")
public class SalesReportServlet extends HttpServlet {
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
        String date = obj.getString("date");
        SalesReportService salesReportService = new SalesReportService(connection);
        try {
            int income = salesReportService.getIncome(model, date);
            if (income < 0) {
                response.setContentType("text/html;charset=utf-8");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Неверные данные...");
                return;
            }
            System.out.println("Прибыль на " + date + " с продаж " +
                    model + " = " + income);
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Замечательно, наша прибыль = " + income + "!!!");
        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
    }
}
