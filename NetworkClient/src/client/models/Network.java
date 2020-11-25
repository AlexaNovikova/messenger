package client.models;

import client.controllers.Controller;
import com.sun.glass.events.WheelEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Network {

    public static final String AUTH_CMD_PREFIX ="/auth";
    public static final String AUTHOK_CMD_PREFIX ="/authok";
    public static final String AUTHERR_CMD_PREFIX ="/autherr";


    private static final String SERVER_ADRESS = "localhost";
    private static final int SERVER_PORT = 8189;

    private final int port;
    private final String host;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Socket socket;
    private  String username;

    public Network() {
        this(SERVER_PORT, SERVER_ADRESS);
    }

    public Network(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }

    public void setDataOutputStream(String str) {
        try {
            this.dataOutputStream.writeUTF(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean Connect() {
        try {
            socket = new Socket(SERVER_ADRESS, SERVER_PORT);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            return true;
        } catch (IOException e) {
            System.out.println("Ошибка установки соединения!");
            e.printStackTrace();
            return false;
        }
    }


    public void Close() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitMessage(Controller controller) {
        {
            Thread thread = new Thread(() -> {
                while (!socket.isClosed()) {
                    try {
                        String message = dataInputStream.readUTF();
                        if (!message.split("\\s+", 2)[1].equals("NICK_INFO")) {
                            try {

                                if (message.split("\\s+", 4)[1].equals("/w")) {
                                    String recipient = message.split("\\s+", 4)[2];
                                    if (recipient.equals(this.username) || message.split("\\s+", 4)[0].equals(this.username + ":"))
                                        controller.appendMessage(message.split("\\s+", 4)[0] + message.split("\\s+", 4)[3]);
                                }
//                         getNicksFromServer();
//                          controller.addContacts();
                                if (!message.split("\\s+", 4)[1].equals("/w")) controller.appendMessage(message);

                            } catch (ArrayIndexOutOfBoundsException e) {
                                controller.appendMessage(message);
                            }
                        }


                    } catch (SocketException e) {

                        // user closed connection
                        return;
                    } catch (IOException e) {
                        System.out.println("Соединение потеряно!");
                        e.printStackTrace();

                    }
                }
            }

            );
            thread.setDaemon(true);
            thread.start();
        }
    }

    public String sendAuthCommand(String login, String password) {
        try {
            dataOutputStream.writeUTF(String.format("%s %s %s", AUTH_CMD_PREFIX, login, password));
            String response = dataInputStream.readUTF();
            if (response.startsWith(AUTHOK_CMD_PREFIX)){
                this.username= response.split("\\s+", 2)[1];
                return null;
            }
            else return response;
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public List<String > getNicksFromServer (){
        try {
            dataOutputStream.writeUTF("NICK_INFO");
            String responce = dataInputStream.readUTF();
            List <String> contactsFromServer = new ArrayList<>();
                if (responce.startsWith("NICK_INFO")) {
                    String [] nickFromServer = responce.split("\\s+",7);
                     contactsFromServer.addAll(Arrays.asList(nickFromServer));
                    contactsFromServer.remove(0);
                    return contactsFromServer;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }

     return null;
    }

    public String getUsername() {
        return username;
    }

}




