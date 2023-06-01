package com.example.melodyhub;

import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;



public class HelloApplication  {

    private static final String HOST = "localhost";
    private static final int PORT = 8085;
    private static final String KEYSTORE = "src/main/java/com/example/melodyhub/client.keystore";
    private static final String KEYSTORE_PASSWORD = "123456";
    public static void main(String[] args) throws Exception {
        // Load the client's keystore
        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream(KEYSTORE), KEYSTORE_PASSWORD.toCharArray());

        // Create a key manager factory
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(keystore, KEYSTORE_PASSWORD.toCharArray());

        // Create an SSL context
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
        PrivateKey key = (PrivateKey)keystore.getKey("mykey",
                "testkey".toCharArray());
        Certificate[] certChain = keystore.getCertificateChain("mykey");

        // Create an SSL socket factory
        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        // Create an SSL socket
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 8445);
        BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        String data = in.readLine();
        // Send data to the server
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));
        out.write("Hello, server!");
        out.flush();

        // Receive a response from the server
//        BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
//        String response = in.readLine();
//        System.out.println(response);

        // Closethe SSL socket
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