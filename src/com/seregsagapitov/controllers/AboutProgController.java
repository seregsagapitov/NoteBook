package com.seregsagapitov.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AboutProgController {

    @FXML
    public TextArea TextAreaAboutProgramm;
    @FXML
    private Text TextInfo;

    @FXML
    private Text AboutProgText;

    @FXML
    private Button AboutProgButtonClose;

    @FXML
    void ActionCloseButton(ActionEvent event) {
        Node sourse = (Node) event.getSource();
        Stage stage = (Stage) sourse.getScene().getWindow();
        stage.close();
    }
}
