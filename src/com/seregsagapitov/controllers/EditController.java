package com.seregsagapitov.controllers;

import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Note;
import com.seregsagapitov.start.Main;
import com.seregsagapitov.utils.LocaleManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class EditController {


    ResourceBundle resourceBundle;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM. yyyy г HH:mm:ss");
    @FXML
    private Button btnClose;
    CollectionNote collectionNote = new CollectionNote();
    @FXML
    private Button btnSave;

    @FXML
    private AnchorPane MainAnchorPain;

    @FXML
    private TextArea textAreaFXsave;
    private Note note;


    public void setNote(Note note) {
        this.note = note;
        if (note.getNoteText() == null) {
            textAreaFXsave.setText("");
        } else
            textAreaFXsave.setText(note.getNoteText());
    }


    void actionClose(ActionEvent event) throws IOException {

        Parent root = null;

        Main main = new Main();
        resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
        main.createGUI(LocaleManager.currentLanguage.getLocale());


//        Parent root = null;
//            ResourceBundle resources = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
//            root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"), resources);
//            Scene scene = new Scene(root);
//            ((Stage) MainAnchorPain.getScene().getWindow()).setScene(scene);
    }

    @FXML
    void cancelButton(ActionEvent event) throws IOException {
        actionClose(event);
    }


    @FXML
    void actionSave(ActionEvent event) throws IOException {

        note.setNoteText(textAreaFXsave.getText());
        note.setCurrentMoment(simpleDateFormat.format(new Date()).toString());
        if (textAreaFXsave.getText().length() > 12) {
            note.setTitle(textAreaFXsave.getText().substring(0, 12).trim() + "\n" + "                                        "
                    + simpleDateFormat.format(new Date()).toString());
        } else {
            note.setTitle(textAreaFXsave.getText().trim() + "\n" + "                                        "
                    + simpleDateFormat.format(new Date()).toString());
        }
        collectionNote.update(note);
        actionClose(event);


    }

}
