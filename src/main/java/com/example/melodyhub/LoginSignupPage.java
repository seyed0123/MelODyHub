package com.example.melodyhub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;



public class LoginSignupPage extends Application {

    public static Socket socket;
    public static PrintWriter output;
    public static BufferedReader input;
    public static ObjectOutputStream objOut;
    public static ObjectInputStream objIn;
    public static final String HOST = "localhost";
    public static final int PORT = 8085;
    public static Cipher cipherEncrypt;
    public static Cipher cipherDecrypt;
    public static Gson gson;
    public static ObjectMapper objectMapper;
    public static void sendMessage(String message)
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
    public static String getMessage()
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
    public static void startCom()
    {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            Cipher decryptCipher = Cipher.getInstance("RSA");
            decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
            objOut.writeObject(publicKey);
            objOut.flush();
            //objOut.close();

            String base64 = gson.fromJson(input.readLine(),String.class);
            byte[] encryptedMessage = Base64.getDecoder().decode(base64);
            byte[] decryptedMessage = decryptCipher.doFinal(encryptedMessage);

            cipherDecrypt = Cipher.getInstance("AES");
            cipherDecrypt.init(Cipher.DECRYPT_MODE,  new SecretKeySpec(decryptedMessage, "AES"));
            cipherEncrypt= Cipher.getInstance("AES");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(decryptedMessage, "AES"));
        }catch (NoSuchAlgorithmException | NoSuchPaddingException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setSocket() throws IOException
    {
        socket = new Socket(HOST, PORT);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
        objOut = new ObjectOutputStream(socket.getOutputStream());
        objIn = new ObjectInputStream(socket.getInputStream());
        gson = new Gson();
        objectMapper = new ObjectMapper();
        startCom();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginSignupPage.class.getResource("LoginSignupPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login / Signup");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        setSocket();
        launch(args);
    }
    public static void uploadImage(Socket socket,String path)
    {
        try {
            // Load the image from a file
            File file = new File(path);
            BufferedImage image = ImageIO.read(file);

            // Convert the image to a byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // Send the byte array over the socket
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(imageBytes.length);
            dos.write(imageBytes);
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void downloadImage(Socket socket,String path)
    {
        try {
            InputStream in = socket.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            int length = dis.readInt();
            byte[] receivedBytes = new byte[length];
            dis.readFully(receivedBytes);

            // Convert the byte array back to an image
            BufferedImage receivedImage = ImageIO.read(new ByteArrayInputStream(receivedBytes));
            // Save the image to a file
            File file = new File(path);
            ImageIO.write(receivedImage, "png", file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void downloadSong(Socket socket,String path)
    {
        try{
            byte[] buffer = new byte[4096];
            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(path);

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void uploadSong(Socket socket,String path)
    {
        try{
            // Load the MP3 file from a file
            File file = new File(path);
            byte[] fileBytes = Files.readAllBytes(file.toPath());

            // Send the byte array over the socket
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeInt(fileBytes.length);
            dos.write(fileBytes);
            dos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}