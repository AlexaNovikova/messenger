package chat.handler;


import chat.MyServer;
import chat.auto.AuthService;
import chat.auto.BaseAuthService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler {

    public static final String AUTH_CMD_PREFIX ="/auth";
    public static final String AUTHOK_CMD_PREFIX ="/authok";
    public static final String AUTHERR_CMD_PREFIX ="/autherr";

    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private AuthService authService;

    private String name;
    public static List <String> availableContacts = new ArrayList<>();

    public String getName() {
        return name;
    }

    public static List<String> getAvailableContacts() {
        return availableContacts;
    }

    public ClientHandler(MyServer myServer, Socket socket, AuthService authService) {
        try {
            this.myServer = myServer;
            this.socket = socket;
            this.authService = authService;
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(() -> {
                try {
                    authentication();
                     sendContactList(authService.contacts(), availableContacts);
                    readMessages();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException("Проблемы при создании обработчика клиента");
        }
    }

    public void authentication() throws IOException {
        while (true) {
            String str = in.readUTF();
            if (str.startsWith(AUTH_CMD_PREFIX)) {
                String[] parts = str.split("\\s");
                String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                if (nick != null) {
                    if (!myServer.isNickBusy(nick)) {
                        sendMsg(AUTHOK_CMD_PREFIX  + " " + nick);
                        name = nick;
                        myServer.broadcastMsg(name + " зашел в чат");
                        availableContacts.add(nick);
                        myServer.subscribe(this);
                        return;
                    } else {
                        sendMsg("Учетная запись уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                }
            }
        }
    }

    public void sendContactList (List<String> contactList, List<String> availableContactList) throws IOException {
        String str = in.readUTF();
        if (str.startsWith("NICK_INFO")) {
           // List<String> nicks = new ArrayList<>(contactList);
                sendMsg("NICK_INFO" + " " + contactList);
           // List<String> availableNicks = new ArrayList<>(availableContactList);
                sendMsg("NICK_INFO" + " " + availableContactList);
        }
    }

    public void readMessages() throws IOException {
        while (true) {
            String strFromClient = in.readUTF();
            System.out.println("от " + name + ": " + strFromClient);
            if (strFromClient.equals("/end")) {
                return;
            }
                myServer.broadcastMsg(name + ": "+ " " + strFromClient);

        }

    }


    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        myServer.unsubscribe(this);
        myServer.broadcastMsg(name + " вышел из чата");
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

