package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.start.Main;
import com.seregsagapitov.utils.LocaleManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {

    ResourceBundle resourceBundle;
    private Stage stage;
    @FXML
    public Label labelPass;
    @FXML
    public PasswordField passField;
    @FXML
    private AnchorPane PassPain;
    @FXML
    private Button EnterMain;

    @FXML
    void EnterMain(ActionEvent event) throws SQLException, InterruptedException {
        if (passField.getText().equals(ConnectDB.selectPassword())) {

            Main main = new Main();
            resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
            main.createGUI(LocaleManager.RU_LOCALE);
        } else {
            labelPass.setVisible(true);
            passField.clear();
            passField.requestFocus();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    }
}
