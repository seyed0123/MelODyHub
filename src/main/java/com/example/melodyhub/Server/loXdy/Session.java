package com.example.melodyhub.Server.loXdy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Session implements Runnable{

    private Socket socket;
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
        try {
            while (true)
            {
                String job;
                job=input.readLine();
                if(Objects.equals(job, "TOTP"))
                {
                    String email =input.readLine();
                    String encoded=LoXdy.TOTPGenerator(email);
                    int totp = input.read();
                    output.println(LoXdy.checkTOTP(totp,encoded));
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
