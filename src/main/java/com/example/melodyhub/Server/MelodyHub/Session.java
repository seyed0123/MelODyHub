package com.example.melodyhub.Server.MelodyHub;

import com.example.melodyhub.*;
import com.example.melodyhub.Server.loXdy.LoXdy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.chart.XYChart;
import org.json.JSONObject;

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
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Session implements Runnable{

    private Account account;
    private Socket socket;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private BufferedReader input ;
    private PrintWriter output ;
    private Socket loXdySocket;
    private BufferedReader loXdyInput ;
    private PrintWriter loXdyOutput ;
    private Cipher cipherEncrypt;
    private Cipher cipherDecrypt;

    private Gson gson;

    private ObjectMapper objectMapper;

    public Session(Socket socket,Socket loXdy) {
        this.loXdySocket= loXdy;
        this.socket = socket;
        try {
            objIn = new ObjectInputStream(socket.getInputStream());
            objOut = new ObjectOutputStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            loXdyInput = new BufferedReader(new InputStreamReader(loXdy.getInputStream()));
            loXdyOutput = new PrintWriter(loXdy.getOutputStream(), true);
            gson = new Gson();
            objectMapper = new ObjectMapper();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
            return null;
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
            e.printStackTrace();
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
        String job;
        int count=0;
        try {
            while (true)
            {
                if(count>10)
                {
                    sendMessage("your are kicked");
                    socket.close();
                }
                job = getMessage();

                if(job==null)
                    return;
                System.out.println(job+" -->"+socket.getLocalSocketAddress());count++;
                if(Objects.equals(job, "login user"))
                {
                    String username = getMessage();
                    String password = getMessage();
                    User user;
                    if((user=MelodyHub.userLogin(username,password))!=null)
                    {
                        if(checkTOTP(user.getEmail())) {
                            account = user;
                            sendMessage("login OK");
                            sendMessage(objectMapper.writeValueAsString(user));
                            break;
                        }
                        else
                        {
                            sendMessage("code is wrong");
                        }
                    }else
                    {
                        sendMessage("login failed");
                    }
                }else if(Objects.equals(job, "login artist"))
                {
                    String username = getMessage();
                    String password = getMessage();
                    Artist artist;
                    if((artist=MelodyHub.artistLogin(username,password))!=null)
                    {
                        if(checkTOTP(artist.getEmail())) {
                            account = artist;
                            sendMessage("login OK");
                            sendMessage(objectMapper.writeValueAsString(artist));
                            break;
                        }
                        else
                        {
                            sendMessage("code is wrong");
                        }
                    }
                    sendMessage("login failed");
                }else if(Objects.equals(job, "login podcaster"))
                {
                    String username = getMessage();
                    String password = getMessage();
                    Podcaster podcaster;
                    if((podcaster=MelodyHub.podcasterLogin(username,password))!=null)
                    {
                        if(checkTOTP(podcaster.getEmail())) {
                            account = podcaster;
                            sendMessage("login OK");
                            sendMessage(objectMapper.writeValueAsString(podcaster));
                            break;
                        }else
                        {
                            sendMessage("code wrong");
                        }
                    }
                    sendMessage("login failed");
                }else if(Objects.equals(job, "create user"))
                {
                    JSONObject json = new JSONObject(getMessage());
                    String username = json.getString("username");
                    ResultSet res = MelodyHub.sendQuery("select * from person where username = '"+username+"';");
                    if(res==null)
                    {
                        String password = json.getString("password");
                        String email = json.getString("email");
                        String phoneNumber = json.getString("phone");
                        String gender = json.getString("gender");
                        String date = json.getString("date");
                        User user = new User(null,username,password,email,phoneNumber,"",new ArrayList<>(),"",gender
                                , Date.valueOf(date),null,false,new ArrayList<>(),new ArrayList<>());
                        MelodyHub.createUser(user);
                        sendMessage("done");
                    }else {
                        sendMessage("failed");
                    }
                }else if(job.equals("create artist"))
                {
                    JSONObject json = new JSONObject(getMessage());
                    String username = json.getString("username");
                    ResultSet res =MelodyHub.sendQuery("select * from artist where username = '"+username+"';");
                    if(res==null)
                    {
                        String password = json.getString("password");
                        String email = json.getString("email");
                        String phoneNumber = json.getString("phone");
                        Artist artist = new Artist(null,username,password,email,phoneNumber,"","",false,0,0,"");
                        MelodyHub.createArtist(artist);
                        sendMessage("done");
                    }else
                    {
                        sendMessage("failed");
                    }
                } else if (job.equals("create podcaster")) {
                    JSONObject json = new JSONObject(getMessage());
                    String username = json.getString("username");
                    ResultSet res =MelodyHub.sendQuery("select * from podcaster where username = '"+username+"';");
                    if(res==null)
                    {
                        String password = json.getString("password");
                        String email = json.getString("email");
                        String phoneNumber = json.getString("phone");
                        Podcaster podcaster = new Podcaster(null,username,password,email,phoneNumber,"",false,"",0);
                        MelodyHub.createPodcaster(podcaster);
                        sendMessage("done");
                    }else
                    {
                        sendMessage("failed");
                    }

                }else if (job.equals("forget pass")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String work = jsonObject.getString("work");
                    String username = jsonObject.getString("username");
                    String type = jsonObject.getString("type");
                    boolean flag = false;
                    UUID uuid;
                    if ((uuid = MelodyHub.findUserUsername(username)) == null && (uuid = MelodyHub.findArtistUsername(username)) == null && (uuid = MelodyHub.findPodcasterUsername(username)) == null) {
                        sendMessage("account not found");
                    }else if(Objects.equals(work, "TOTP"))
                    {
                        flag = checkTOTP(MelodyHub.findUser(uuid).getEmail());
                    } else if (Objects.equals(work, "answer")) {
                        String answer = jsonObject.getString("answer");
                        int quesN = jsonObject.getInt("number");
                        flag = UserPerform.checkAnswer(uuid,quesN,answer);
                    }
                    if(flag)
                    {
                        sendMessage("you are you");
                        String password = getMessage();
                        MelodyHub.updatePass(type,uuid,password);
                        sendMessage("password updated");
                    }else {
                        sendMessage("sorry you aren't you");
                    }
                }
            }
            while (true)
            {
                job = getMessage();
                if(job==null)
                    return;
                System.out.println(job+" -->"+socket.getLocalSocketAddress());
                if(job.equals("add answer"))
                {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    UUID id = account.getId();
                    int quesId = jsonObject.getInt("quesId");
                    String ans = jsonObject.getString("answer");
                    UserPerform.addAnswer(id,quesId,ans);
                } else if (job.equals("crate playlist")) {
                    JSONObject jsonObject = new JSONObject(getMessage());
                    String name = jsonObject.getString("name");
                    boolean personal = jsonObject.getBoolean("personal");
                    UUID artist = MelodyHub.findArtistUsername(jsonObject.getString("artist"));
                    UUID firstOwner = MelodyHub.findUserUsername(jsonObject.getString("firstOwner"));
                    MelodyHub.createPlaylist(new PlayList(null,name,personal,0,0,artist.toString(),firstOwner.toString()));
                } else if (job.equals("break")) {
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkTOTP(String email) throws IOException {
        sendMessage("TOTP");
        loXdyOutput.println("TOTP");
        loXdyOutput.println(email);
        loXdyOutput.println(getMessage());
        String check = loXdyInput.readLine();
        if(Objects.equals(check, "true")) {
            return true;
        }else
            return false;
    }
}
