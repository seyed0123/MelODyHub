package com.example.melodyhub;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;



public class HelloApplication  {
    public static PrintWriter output;
    public static BufferedReader serverInput;
    private static final String HOST = "localhost";
    private static final int PORT = 8085;
    private static final String KEYSTORE = "src/main/java/com/example/melodyhub/client.keystore";
    private static final String KEYSTORE_PASSWORD = "123456";
    public static void main(String[] args)  {
        try {
            Socket socket = new Socket(HOST, PORT);
            serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}