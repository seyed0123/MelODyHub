package com.example.melodyhub.Server.MelodyHub;

import com.google.gson.Gson;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.sql.DriverManager;

public class Main {
    private static final int PORT = 8085;
    private static final String KEYSTORE = "src/main/java/com/example/melodyhub/Server/MelodyHub/server.keystore";
    private static final String KEYSTORE_PASSWORD = "123456";
    private static final String url ="jdbc:postgresql://localhost:5432/test";
    private static final String username = "postgres";
    private static final String password = "Seyed5516";
    public static void main(String[] args) throws Exception {
        // Load the server's keystore
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream(KEYSTORE), KEYSTORE_PASSWORD.toCharArray());

        // Create a key manager factory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keystore, KEYSTORE_PASSWORD.toCharArray());

        // Create an SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        // Create an SSL socket factory
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

        // Create an SSL server socket
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(8445);

        while (true) {
            // Wait for a client connection
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
            out.write("Hello, server!");
            out.flush();
            // Receive data from the client
            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            String data = in.readLine();

            // Send a response back to the client
//            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
//            out.write("Hello, client!");
//            out.flush();

            // Close the SSL socket
            sslSocket.close();
        }
    }
    private static SSLServerSocketFactory createSSLServerSocketFactory() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(KEYSTORE), KEYSTORE_PASSWORD.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        return sslContext.getServerSocketFactory();
    }
}
