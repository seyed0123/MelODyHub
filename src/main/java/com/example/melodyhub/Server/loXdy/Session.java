package com.example.melodyhub.Server.loXdy;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

import org.json.JSONObject;

public class Session implements Runnable{

    private Socket socket ;
    private BufferedReader input ;
    private PrintWriter output ;

    public Session(Socket socket) {
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        String job;
        try {
            while (true)
            {
                job=input.readLine();
                System.out.println(job);
                if(Objects.equals(job, "TOTP"))
                {
                    String email =input.readLine();
                    String encoded=LoXdy.TOTPGenerator(email);
                    int totp = Integer.parseInt(input.readLine());
                    if(totp!=-1)
                        output.println(LoXdy.checkTOTP(totp,encoded));
                } else if (Objects.equals(job, "send email")) {
                    String json = input.readLine();
                    JSONObject jsonObject = new JSONObject(json);
                    String email = jsonObject.getString("email");
                    String subject = jsonObject.getString("subject");
                    String body = jsonObject.getString("body");
                    LoXdy.sendEmail(email,subject,body);
                } else if(Objects.equals(job, "break"))
                    break;
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
