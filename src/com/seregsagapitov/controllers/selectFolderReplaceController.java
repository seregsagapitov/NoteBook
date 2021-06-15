package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Note;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.SQLException;


public class selectFolderReplaceController {

    public TableColumn<Note, String> columnNotesSelectFolder;
    public MenuButton menuButton_folder_1;
    public CollectionNote collectionNote;


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
                //listSelectToReplace();
                actionCloseMouse(event);
            }
        });
    }

    @FXML
    void buttonSelect(ActionEvent event) {
        listViewSelectItem();
        //listSelectToReplace();
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
            //System.out.println("o = " + o + " (" + o.getClass() + ")");
            Controller.currentTable = (String) Controller.dataTable.keySet().toArray()[(int) o];


            try {
                ConnectDB.showData(CollectionNote.noteList);
                columnNotesSelectFolder.setText(Controller.dataTable.get(Controller.currentTable));

                if (Controller.currentTable != "NOTES") {
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
