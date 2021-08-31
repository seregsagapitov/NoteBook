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

    //    public void setStage(Stage stage) {
//        this.stage = stage;
//    }
    @FXML
    void EnterMain(ActionEvent event) throws SQLException, InterruptedException {
        if (passField.getText().equals(ConnectDB.selectPassword())) {
            Parent root = null;
            Main main = new Main();
            resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
            // Main.getPrimaryStage().close();
            main.createGUI(LocaleManager.RU_LOCALE);
//                ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"), resourceBundle);
//                root = loader.load();
//
//
//                Scene scene = new Scene(root);
//                ((Stage) PassPain.getScene().getWindow()).setScene(scene);


        } else {
            labelPass.setVisible(true);
            passField.clear();
            //labelPass.setVisible(false);
            passField.requestFocus();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // Thread.currentThread().join();
                    //Thread.sleep(3000);
                }
            });


        }

    }


//    @Override
//    public void initialize(URL location, ResourceBundle resources) {
//        try {
//            try {
//                if ((ConnectDB.selectPassword()).equals("q")) {
//                    Parent root = null;
//                    try {
//                        root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
//                        Scene scene = new Scene(root);
//                        //((Stage) PassPain.getScene().getWindow()).setScene(scene);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
}
