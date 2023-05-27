package com.example.melodyhub.Server.MelodyHub;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class Main {
    private static final int PORT = 8085;
    private static final String KEYSTORE = "src/main/java/com/example/melodyhub/Server/MelodyHub/server.keystore";
    private static final String KEYSTORE_PASSWORD = "123456";
    public static void main(String[] args) throws Exception {
        SSLServerSocketFactory sslServerSocketFactory = createSSLServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);

        System.out.println("SSL server started on port " + PORT);

        while (true) {
            SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
            System.out.println("Client connected: " + sslSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);

            String message = in.readLine();
            System.out.println("Received message: " + message);

            out.println(message);

            in.close();
            out.close();
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
