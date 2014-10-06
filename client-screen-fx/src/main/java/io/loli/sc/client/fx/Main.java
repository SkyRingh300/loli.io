package io.loli.sc.client.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setController(new LoginController());
        Parent root = loader.load();
        primaryStage.setTitle("萝莉图床客户端");
        primaryStage.setScene(new Scene(root, 440, 210));
        primaryStage.setResizable(false);
        primaryStage.show();
        TextField username = (TextField) root.lookup("#username");
        username.requestFocus();

        readFromConfig();

    }

    private void readFromConfig() {

    }


    public static void main(String[] args) {
        launch(args);
    }
}
