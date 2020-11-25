package client.controllers;

import client.NetworkClient;
import client.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthDialog {
 private Network network;
 private NetworkClient networkClient;

    @FXML public PasswordField passField;
     @FXML public TextField loginField;

   @FXML public void checkAuth(ActionEvent actionEvent) {
       String login = loginField.getText();
       String password = passField.getText();
       if (password.isBlank()||login.isBlank()){
         NetworkClient.showErrorMessage("Поля не должны быть пустыми!","Ошибка при авторизации");
         return;
       }
       String authErrMessage = network.sendAuthCommand (login, password);
       if (authErrMessage == null) {
        networkClient.openChat();
       }
       else {
           NetworkClient.showErrorMessage(authErrMessage, "Ошибка авторизации");

       }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
}
