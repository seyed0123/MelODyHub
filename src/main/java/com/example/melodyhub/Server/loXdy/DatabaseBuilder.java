package com.example.melodyhub.Server.loXdy;
import org.json.JSONObject;

import java.sql.*;

public class DatabaseBuilder implements Runnable{
    private  final String url ="jdbc:postgresql://localhost:5432/test";
    private  final String username = "postgres";
    private  final String password = "Seyed5516";
    private Connection connection;
    @Override
    public void run() {
        try {
            connection= DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            PreparedStatement stmt = connection.prepareStatement("");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                connection.close();
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void doJob(JSONObject jsonObject)
    {
    }
}