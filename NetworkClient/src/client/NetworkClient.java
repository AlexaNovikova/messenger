package client;

import client.controllers.AuthDialog;
import client.controllers.Controller;
import client.models.Network;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class NetworkClient extends Application {
   public Stage primaryStage;
    private Stage authStage;
    private Network network;
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {
       this.primaryStage=primaryStage;
        FXMLLoader authloader = new FXMLLoader(getClass().getResource("views/AuthDialog.fxml"));
        authStage = new Stage();
        Parent page = authloader.load();
        authStage.setTitle("Авторизация");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(primaryStage);
        Scene scene=new Scene(page);
        authStage.setScene(scene);
       //  authStage.show();
        FXMLLoader loader =new FXMLLoader(getClass().getResource("views/sample1.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 421, 400));
        primaryStage.show();
        authStage.show();
        network = new Network();
        if (!network.Connect()) {
            showErrorMessage("","Ошибка подключения к серверу.");
        }
        controller = loader.getController();
        controller.setNetwork(network);
        AuthDialog authDialog=authloader.getController();
        authDialog.setNetwork(network);
        authDialog.setNetworkClient(this);
//       Thread thread= new Thread(()->
//       {
//           String serverMessage = null;
//           try {
//               serverMessage = network.getDataInputStream().readUTF();
//               controller.appendMessage(serverMessage);
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
//
//       });
      //Thread thread= new Thread(() -> network.waitMessage(controller));
       // network.waitMessage(controller);
       //thread.setDaemon(true);
        //thread.start();
        primaryStage.setOnCloseRequest((windowEvent -> {
            network.setDataOutputStream("exit");
            network.Close();
      //      thread.interrupt();
            Platform.exit();
        }));
    }

   public static void showErrorMessage (String message, String errorMessage){
    Alert alert= new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Ошибка.");
    alert.setHeaderText(errorMessage);
    alert.setContentText(message);
    alert.setHeight(300);
    alert.showAndWait();

}

    public Network getNetwork() {
        return network;
    }

    public static void main(String[] args) {
        launch(args);

    }
    public void openChat (){
        authStage.close();
        primaryStage.setTitle(network.getUsername());
        primaryStage.show();
        controller.addContacts();
        network.waitMessage(controller);
    }
}
