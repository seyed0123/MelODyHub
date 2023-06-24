package com.example.melodyhub.Server.MelodyHub;
import com.example.melodyhub.*;
import com.example.melodyhub.Server.MelodyHub.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

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
            PreparedStatement stmt = connection.prepareStatement("select command from log order by timestamp;");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                connection.close();
            }
            while (true)
            {
                //doJob(new JSONObject(rs.getString("command")));
                if(!rs.next())
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}