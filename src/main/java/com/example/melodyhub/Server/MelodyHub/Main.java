package com.example.melodyhub.Server.MelodyHub;

import com.google.gson.Gson;

import javax.net.ssl.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.UUID;

public class Main {
    private static final int PORT = 8085;
    private static final String HOST = "localhost";
    private static int loXdyPORT=8090;
    private static final String url ="jdbc:postgresql://localhost:5432/test";
    private static final String username = "postgres";
    private static final String password = "Seyed5516";
    public static HashSet<UUID> clients;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Socket socket = new Socket(HOST, loXdyPORT);
            System.out.println("Client connected.");

            Thread clientThread = new Thread(new Session(clientSocket,socket));
            clientThread.start();
        }
//        serverSocket.close();
    }
}
