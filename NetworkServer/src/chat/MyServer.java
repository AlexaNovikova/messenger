package chat;

import chat.auto.AuthService;
import chat.auto.BaseAuthService;
import chat.handler.ClientHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyServer {
    private final int PORT = 8189;

    private List<ClientHandler> clients;
    private AuthService authService;

    public AuthService getAuthService() {
        return authService;
    }

    public List<String> getClients() {
        List<String> contacts = new ArrayList<>();
        for (ClientHandler client : clients) {
            contacts.add(client.getName());
        }
        return contacts;
    }

    public MyServer() {
        try (ServerSocket server = new ServerSocket(PORT)) {
            authService = new BaseAuthService();
            authService.start();
            clients = new ArrayList<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                Socket socket = server.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket, authService);
                System.out.println(authService.contacts());

            }
        } catch (IOException e) {
            System.out.println("Ошибка в работе сервера");
        } finally {
            if (authService != null) {
                authService.stop();
            }
        }
    }

    public synchronized boolean isNickBusy(String nick) {
        for (ClientHandler o : clients) {
            if (o.getName().equals(nick)) {
                return true;
            }
        }
        return false;
    }

    public synchronized void broadcastMsg( String msg) {
       //if (msg.startsWith("/w")) {
            // String client = msg.split("//s+", 2)[1];
          //  for (ClientHandler o : clients) {
          //      if (o.equals(client)) o.sendMsg(msg.split("//s+", 2)[2]);
           // }
      //  } else {
            for (ClientHandler o : clients) {
                o.sendMsg(msg);
            }
       // }
    }



    public synchronized void unsubscribe(ClientHandler o) {
        clients.remove(o);

    }

    public synchronized void subscribe(ClientHandler o) {
        clients.add(o);

    }
}