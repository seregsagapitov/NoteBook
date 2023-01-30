package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Note;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.swing.text.TabableView;
import java.sql.SQLException;
import java.util.ArrayList;


public class selectFolderController {

    public TableColumn<Note, String> columnNotesSelectFolder;
    public MenuButton menuButton_folder_1;
    TableView<Note> tableNote;
    CollectionNote collectionNote = new CollectionNote();
    Button delButton;
    Button addButton;
    Button clearRecButton;


    @FXML
    ListView<String> listViewSelectFolder;

    public ListView<String> getListViewSelectFolder() {
        return listViewSelectFolder;
    }

    @FXML
    void clickMouseSelect() {
        listViewSelectFolder.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                listViewSelectItem();
                actionCloseMouse(event);
            }
        });
    }

    @FXML
    void buttonSelect(ActionEvent event) {
        listViewSelectItem();
        actionCloseButton(event);
    }

    void actionCloseMouse(MouseEvent event) {
        Node sourse = (Node) event.getSource();
        Stage stage = (Stage) sourse.getScene().getWindow();
        stage.close();
    }

    void actionCloseButton(ActionEvent event) {
        Node sourse = (Node) event.getSource();
        Stage stage = (Stage) sourse.getScene().getWindow();
        stage.close();
    }

    void listViewSelectItem() {
        ObservableList selectedIndices = listViewSelectFolder.getSelectionModel().getSelectedIndices();
        for (Object o : selectedIndices) {
            if (Controller.replaceOn == true) {

                ObservableList<Note> selectedNotes = tableNote.getSelectionModel().getSelectedItems();
                ArrayList<Note> rows = new ArrayList<>(selectedNotes);
                String newTable = (String) Controller.dataTable.keySet().toArray()[(int) o];
                System.out.println("Перемещаем в выбранную папку " + newTable);
                collectionNote.replace(rows, newTable);
                Controller.currentTable = newTable;
                Controller.replaceOn = false;
            }
            Controller.currentTable = (String) Controller.dataTable.keySet().toArray()[(int) o];

            try {
                ConnectDB.showData(CollectionNote.noteList);
                columnNotesSelectFolder.setText(Controller.dataTable.get(Controller.currentTable));

                if (Controller.currentTable != "RECYCLED") {
                    addButton.setDisable(false);
                    delButton.setDisable(false);
                    clearRecButton.setVisible(false);
                } else {
                    addButton.setDisable(true);
                    delButton.setDisable(true);
                    clearRecButton.setVisible(true);
                }

                if (Controller.currentTable != "NOTES" && Controller.currentTable != "RECYCLED") {
                    menuButton_folder_1.getItems().get(1).setDisable(false);

                } else {
                    menuButton_folder_1.getItems().get(1).setDisable(true);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    String listSelectToReplace() {
        String selectTable = "";
        ObservableList selectedIndices = listViewSelectFolder.getSelectionModel().getSelectedIndices();
        for (Object o : selectedIndices) {
            //System.out.println("o = " + o + " (" + o.getClass() + ")");
            selectTable = (String) Controller.dataTable.keySet().toArray()[(int) o];
        }
        return selectTable;
    }
}
