package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Note;
import com.seregsagapitov.start.Main;
import com.seregsagapitov.utils.LocaleManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ResourceBundle;

public class NewFolderController {

    ResourceBundle resourceBundle;
    private static Connection connection;
    private static Statement stmt;
    public TableColumn<Note, String> columnNotesNewFolder;

    @FXML
    private TextField labelNewFolder;

    void actionClose(ActionEvent event) {

        Node sourse = (Node) event.getSource();
        Stage stage = (Stage) sourse.getScene().getWindow();
        stage.close();


        Main main = new Main();
        resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER);
                main.createGUI(LocaleManager.currentLanguage.getLocale());
    }


    @FXML
    void closeWindowNewFolder(ActionEvent event) {
        actionClose(event);

    }

    @FXML
    void createNewFolderAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        ConnectDB.connect();
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:NOTETEXT_DB.db");
        stmt = connection.createStatement();
        Controller.currentTable = "base_" + ++Controller.count;

        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + Controller.currentTable + "(\n" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                "     UNIQUE\n" +
                "    NOT NULL,\n" +
                "    NOTETEXT VARCHAR (8192) NOT NULL,\n" +
                "    DATE     VARCHAR (256)  NOT NULL\n" +
                ");";
        stmt.execute(sqlQuery);

        PreparedStatement ps;
        ps = connection.prepareStatement("INSERT INTO NAMES ( NAME_BASE, TITLE_BASE) VALUES (?,?);");
        ps.setString(1, Controller.currentTable);
        ps.setString(2, labelNewFolder.getText());
        Controller.dataTable.put(Controller.currentTable, labelNewFolder.getText());
        ConnectDB.showData(CollectionNote.noteList);
        columnNotesNewFolder.setText(Controller.dataTable.get(Controller.currentTable));

        System.out.println(Controller.dataTable.get(Controller.currentTable) + " новая папка");
        labelNewFolder.clear();
        ps.execute();
        ps.close();
        ConnectDB.disconnect();
        actionClose(event);


        System.out.println(Controller.count);
    }

}
