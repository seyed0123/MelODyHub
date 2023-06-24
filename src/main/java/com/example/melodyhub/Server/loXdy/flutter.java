package com.example.melodyhub.Server.loXdy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;

public class flutter {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server started on port 12345");
        Socket socket = serverSocket.accept();
        System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = in.readLine();
        System.out.println("Received message from client: " + message);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("Message received by server: " + message);

        socket.close();
    }
}
