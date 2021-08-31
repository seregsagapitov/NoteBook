package com.seregsagapitov.controllers;

import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Language;
import com.seregsagapitov.objects.Note;
import com.seregsagapitov.start.Main;
import com.seregsagapitov.utils.LocaleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class NewNoteController {


    ResourceBundle resourceBundle;
    @FXML
    private AnchorPane MainAnchorPain;

    Controller controller;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy г HH:mm:ss");
    CollectionNote collectionNote = new CollectionNote();

    @FXML
    private TextArea textAreaFX;


    void actionClose(ActionEvent event) {
        Parent root = null;

        try {

            Main main = new Main();
            resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
           // Main.getPrimaryStage().close();
            main.createGUI(LocaleManager.currentLanguage.getLocale());

//            ResourceBundle resources = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
//            root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"), resources);
//            Scene scene = new Scene(root);
//            ((Stage) MainAnchorPain.getScene().getWindow()).setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @FXML
    void cancelButton(ActionEvent event) throws IOException {
        actionClose(event);
    }

    @FXML
    void actionAdd(ActionEvent event) throws IOException {
        collectionNote.addNote(new Note(textAreaFX.getText(), (simpleDateFormat.format(new Date())).toString(), 1));
        textAreaFX.setText("");
        actionClose(event);
    }
}
