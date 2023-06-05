package com.example.melodyhub.Server.MelodyHub;

import com.example.melodyhub.Account;
import com.example.melodyhub.Artist;
import com.example.melodyhub.User;
import com.google.gson.Gson;
import javafx.scene.chart.XYChart;

import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

public class Session implements Runnable{

    private Account account;
    private Socket socket;
    private BufferedReader input ;
    private PrintWriter output ;
    private Socket loXdySocket;
    private BufferedReader loXdyInput ;
    private PrintWriter loXdyOutput ;
    private Cipher cipherEncrypt;
    private Cipher cipherDecrypt;

    private Gson gson;

    public Session(Socket socket,Socket loXdy) {
        this.loXdySocket= loXdy;
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            loXdyInput = new BufferedReader(new InputStreamReader(loXdy.getInputStream()));
            loXdyOutput = new PrintWriter(loXdy.getOutputStream(), true);
            gson = new Gson();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendMessage(String message)
    {
        try {
            byte[] encrypt = cipherEncrypt.doFinal(message.getBytes());
            String base64Data = Base64.getEncoder().encodeToString(encrypt);
            output.println(gson.toJson(base64Data));
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    private String getMessage()
    {
        try {
            String base64 = gson.fromJson(input.readLine(),String.class);
            byte[] decodedData = Base64.getDecoder().decode(base64);
            byte[] decrypted = cipherDecrypt.doFinal(decodedData);
            return new String(decrypted);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    private void setKey()
    {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            Cipher encryptCipher = Cipher.getInstance("RSA");
            ObjectInputStream objIn = new ObjectInputStream(socket.getInputStream());
            RSAPublicKey publicKey = (RSAPublicKey) objIn.readObject();
            //objIn.close();
            encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);


            byte[] encrypted = encryptCipher.doFinal(secretKey.getEncoded());
            String base64Data = Base64.getEncoder().encodeToString(encrypted);
            output.println(gson.toJson(base64Data));

            cipherEncrypt = Cipher.getInstance("AES");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, secretKey);
            cipherDecrypt = Cipher.getInstance("AES");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKey);


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void run() {
        setKey();
        System.out.println(getMessage());
        sendMessage("hello encrypted client");
        try {
            while (true)
            {
                String job =input.readLine();
                if(Objects.equals(job, "login user"))
                {
                    String username = input.readLine();
                    String password = input.readLine();
                    User user;
                    if((user=MelodyHub.userLogin(username,password))!=null)
                    {
                        account=user;
                        break;
                    }
                }else if(Objects.equals(job, "login artist"))
                {
                    String username = input.readLine();
                    String password = input.readLine();
                    Artist artist;
                    if((artist=MelodyHub.artistLogin(username,password))!=null)
                    {
                        account=artist;
                        break;
                    }
                }
                else if(Objects.equals(job, "break"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
