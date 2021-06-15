package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SetPassword {

    @FXML
    private Button setPassButton;

    @FXML
    private Button CancelPassButton;

    @FXML
    private PasswordField setPass;

    @FXML
    void closeWindowPassword(ActionEvent event) {
        Node sourse = (Node) event.getSource();
        Stage stage = (Stage) sourse.getScene().getWindow();
        stage.close();
    }

    @FXML
    void createNewPassword(ActionEvent event) {
        try {
            ConnectDB.setPassword(setPass);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeWindowPassword(event);
    }
}
