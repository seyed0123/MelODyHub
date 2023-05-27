package com.example.melodyhub;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.security.KeyStore;

public class HelloApplication  {

    private static final String HOST = "localhost";
    private static final int PORT = 8085;
    private static final String KEYSTORE = "src/main/java/com/example/melodyhub/client.keystore";
    private static final String KEYSTORE_PASSWORD = "123456";
    public static void main(String[] args) throws Exception {
        SSLSocketFactory sslSocketFactory = createSSLSocketFactory();
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(HOST, PORT);

        BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        PrintWriter out = new PrintWriter(sslSocket.getOutputStream(),true);

        String message = "Hello, server!";
        out.println(message);

        String response = in.readLine();
        System.out.println("Received response: " + response);

        in.close();
        out.close();
        sslSocket.close();
    }
    private static SSLSocketFactory createSSLSocketFactory() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(KEYSTORE), KEYSTORE_PASSWORD.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }
}