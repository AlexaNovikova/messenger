package client.controllers;
import ClientServer.Command;
import client.NetworkClient;
import client.models.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Timer;

public class AuthDialogController {

    @FXML public TextField loginField;
    @FXML public PasswordField passField;

    private Network network;
    private NetworkClient networkClient;

    @FXML
    public void checkAuth() {

            String login = loginField.getText();
            String password = passField.getText();
            if (login.isBlank() || password.isBlank()) {
                NetworkClient.showErrorMessage("Поля не должны быть пустыми", "Ошибка ввода");
                return;
            }

            String authErrorMessage = network.sendAuthCommand(login, password);
            if (authErrorMessage == null) {
                networkClient.openChat();
            } else {
                if (authErrorMessage.equals("Время ожидания истекло!")){
                    NetworkClient.showErrorMessage(authErrorMessage, "Ошибка подключения!");
                    network.close();
                    System.exit(1);
                }
                NetworkClient.showErrorMessage(authErrorMessage, "Ошибка авторизации");

            }


    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setNetworkClient(NetworkClient networkClient) {
        this.networkClient = networkClient;
    }
}
