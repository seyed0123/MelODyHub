package com.example.melodyhub.Server.MelodyHub;

import com.example.melodyhub.Account;
import com.example.melodyhub.Artist;
import com.example.melodyhub.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class Session implements Runnable{

    private Account account;
    private Socket socket;
    private BufferedReader input ;
    private PrintWriter output ;
    private Socket loXdySocket;
    private BufferedReader loXdyInput ;
    private PrintWriter loXdyOutput ;

    public Session(Socket socket,Socket loXdy) {
        this.loXdySocket= loXdy;
        this.socket = socket;
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            loXdyInput = new BufferedReader(new InputStreamReader(loXdy.getInputStream()));
            loXdyOutput = new PrintWriter(loXdy.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
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
