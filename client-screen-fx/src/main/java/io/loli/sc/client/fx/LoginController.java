package io.loli.sc.client.fx;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class LoginController implements Initializable {
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button okButton;

    @FXML
    private CheckBox autoLogin;

    private Config config = new Config();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        okButton.disableProperty().bind(Bindings.createBooleanBinding(() -> (username.getText().length() == 0) ||
                (password.getText().length() == 0), username.textProperty(), password.textProperty()));
        okButton.setOnAction((event) -> {
            String uname = username.getText();
            String passwd = password.getText();

        });
        autoLogin.setOnAction((event) -> {
            if (autoLogin.isSelected()) {
                config.setAutoLogin(true);
            } else {
                config.setAutoLogin(false);
            }
        });

        if (config.getAutoLogin()) {
            autoLogin.setSelected(config.getAutoLogin());
        }

    }
}
