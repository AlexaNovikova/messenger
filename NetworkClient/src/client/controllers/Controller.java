package client.controllers;

import client.NetworkClient;
import client.models.Network;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    public ListView <String>listContacts;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField textField;

    private Network network;

    ObservableList<String> words= FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void addContacts ( ) {
        List<String> contacts = new ArrayList<>();
        contacts = network.getNicksFromServer();
        if (contacts!=null) {
            listContacts.getItems().add("Список контактов:");
            for (String contact : contacts) {
                String nick = contact;
                listContacts.getItems().addAll(nick);
            }}
        List<String> availableContacts = new ArrayList<>();
        availableContacts=network.getNicksFromServer();
        if (availableContacts!=null){
            listContacts.getItems().addAll("В сети:");
               for (String availableContact: availableContacts)
               { String contact = availableContact;
                listContacts.getItems().addAll(contact);
            }
        }


    }
    public void addMessage (){
        String message = textField.getText();
        if (!message.isBlank())
        {
       // listView.getItems().addAll(message);
        textField.setText("");
            try {
                network.getDataOutputStream().writeUTF(message);
            } catch (IOException e) {
                e.printStackTrace();
                String errorMessage = "Ошибка при отправке";
                NetworkClient.showErrorMessage(e.getMessage(),errorMessage);
            }
        }

    }

    public void appendMessage (String message){
     //   listView.getItems().addAll(message);
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    listView.getItems().addAll(message);
                }
            });
    }
    public void Exit (){
        network.setDataOutputStream("exit");
        network.Close();
        Platform.exit();
    }
    public void Reset(){
    listView.getItems().clear();
    }
    public void messageWindow (){
        Alert alert= new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("О программе.");
        alert.setHeaderText(null);
        alert.setContentText("Простой мессенджер позволяет пользователям обмениваться сообщениями. Версия: 1.0. Разработчик: Новикова А.А.");
        alert.setHeight(300);
        alert.showAndWait();

    }
}
