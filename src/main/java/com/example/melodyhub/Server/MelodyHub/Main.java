package com.example.melodyhub.Server.MelodyHub;

import com.google.gson.Gson;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.*;
import java.util.HashSet;
import java.util.UUID;

public class Main {
    private static final int PORT = 8085;
    private static final String HOST = "localhost";
    private static int loXdyPORT=8090;
    private static final String url ="jdbc:postgresql://localhost:5432/test";
    private static final String username = "postgres";
    private static final String password = "1234";
    public static HashSet<Session> clients = new HashSet<>();
    public static void main(String[] args) throws IOException {
        try {
             MelodyHub.connection= DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ServerSocket serverSocket=null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Socket socket = new Socket(HOST, loXdyPORT);
                System.out.println("Client connected." + socket.getLocalSocketAddress());
                Session session = new Session(clientSocket, socket);
                Thread clientThread = new Thread(session);
                clients.add(session);
                clientThread.start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            serverSocket.close();
        }
    }
}
