package com.seregsagapitov.controllers;

import com.seregsagapitov.start.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

public class SettingsController {
    @FXML
    private AnchorPane MainField;

    @FXML
    private ComboBox<?> Themes;

    @FXML
    private Button CancelSettingsButton;

    @FXML
    private PasswordField settingPassword;

    @FXML
    private Button CloseSaveButton;

    @FXML
    private ComboBox<?> settingFont;

    @FXML
    void CloseSaveSettings(ActionEvent event) {

        if (Themes.getValue().equals("DarkTheme")){
        //    Main.getPrimaryStage().getScene().getStylesheets().add("../css/JMetroDarkTheme.css");
        }
//        MainField.setStyle("-fx-background-color: #" + choiseColour.getValue().toString().substring(2) );
//        System.out.println(choiseColour.getValue().toString().substring(2));
//        MainField.setStyle("-fx-font: " + settingFont.getValue());
//       //+ choiseColour.getValue().toString()
    }

    @FXML
    void CancelSettings(ActionEvent event) {

    }
}