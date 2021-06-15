package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Note;
import com.seregsagapitov.start.Main;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Controller {

    CollectionNote collectionNote = new CollectionNote();
    private ListView<String> listViewSelectFolder;
    @FXML
    private Label labelPass;
    @FXML
    private AnchorPane PassPain;
    @FXML
    private PasswordField passField;
    @FXML
    private Button EnterMain;
    Stage stage;
    @FXML
    private VBox VboxMain;
    @FXML
    private MenuButton menuButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button newFolderButton;

    public TableView<Note> getTableNote() {
        return tableNote;
    }

    @FXML
    private TableView<Note> tableNote;
    @FXML
    private TableColumn<Note, String> columnNotes;
    @FXML
    private Button delButton;
    @FXML
    private Button addButton;
    @FXML
    private Button replaceButton;
    @FXML
    private MenuButton menuButton_folder;
    @FXML
    private AnchorPane MainAnchorPain;
    @FXML
    private PasswordField setPass;
    @FXML
    private Label labelCount;
    private Parent fxmlEdit;
    private Parent fxmlNewFolder;
    private Parent fxmlAdd;
    private Parent fxmlColour;
    private Parent fxmlSetting;
    private Parent fxmlSetPass;
    private FXMLLoader fxmlLoader1 = new FXMLLoader();
    private FXMLLoader fxmlLoader2 = new FXMLLoader();
    private FXMLLoader fxmlLoader3 = new FXMLLoader();
    private FXMLLoader fxmlLoader4 = new FXMLLoader();
    private FXMLLoader fxmlLoader5 = new FXMLLoader();
    private FXMLLoader fxmlLoader6 = new FXMLLoader();
    private Stage setPasswordStage;
    private Stage choiceColourDialogStage;
    private Stage settingsDialogStage;
    private EditController editController;
    private NewFolderController newFolderController;
    private NewNoteController newNoteController;
    public selectFolderController selectFolderController;
    public LoginController loginController;
    selectFolderReplaceController selectFolderReplaceController;
    public static boolean replaceOn;


    public static TreeMap<String, String> dataTable = new TreeMap<>();
    public static Integer count = 0;
    public static String currentTable = "";


    static {
        dataTable.put("NOTES", "Мои заметки");
        dataTable.put("RECYCLED", "Корзина");
        currentTable = "NOTES";
        //dataTable.put(currentTable, "пум-пурум");
    }

    @FXML
    private void initialize() throws SQLException, IOException {
        System.out.println(ConnectDB.selectPassword());

        if (currentTable == "NOTES" && currentTable == "RECYCLED") {
            menuButton_folder.getItems().get(1).setDisable(true);
        } else {
            menuButton_folder.getItems().get(1).setDisable(false);
        }


        tableNote.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        columnNotes.setCellValueFactory(new PropertyValueFactory<Note, String>("title"));
        columnNotes.setText(dataTable.get(currentTable));

        collectionNote.getNoteList().addListener(new ListChangeListener<Note>() {
            @Override
            public void onChanged(Change<? extends Note> c) {
                updateCountLabel();
            }
        });

        collectionNote.fillTestData();
        tableNote.setItems(collectionNote.getNoteList());
        updateCountLabel();

        fxmlLoader4.setLocation(getClass().getResource("../fxml/choiceColour.fxml"));
        fxmlLoader5.setLocation(getClass().getResource("../fxml/setts.fxml"));
        fxmlLoader6.setLocation(getClass().getResource("../fxml/setPassword.fxml"));


        //      try {
//            fxmlEdit = fxmlLoader1.load();
//            fxmlAdd = fxmlLoader2.load();
        //      fxmlNewFolder = fxmlLoader3.load();
//            fxmlColour = fxmlLoader4.load();
//            fxmlSetting = fxmlLoader5.load();
        fxmlSetPass = fxmlLoader6.load();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        ConnectDB.feelDataDB();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                for (MenuItem item : menuButton.getItems()) {
                    item.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (item.getText().equals("Выбор папки")) {
                                selectFolderMenu(event);
                            }
                            if (item.getText().equals("Корзина")) {
                                currentTable = "RECYCLED";
                                try {
                                    ConnectDB.showData(CollectionNote.noteList);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                columnNotes.setText(Controller.dataTable.get(Controller.currentTable));
                                menuButton_folder.getItems().get(1).setDisable(true);

                            }
                            if (item.getText().equals("Выбор цвета")) {
                                try {
                                    openWindowCoiceColour(event);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (item.getText().equals("Настройки")) {
                                try {
                                    openWindowCoSetting(event);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (item.getText().equals("Выход")) {
                                System.exit(0);
                                System.out.println("Кнопка выход и надо выйти!!!");
                            }

                            if (item.getText().equals("пароль")) {
                                if (setPasswordStage == null) {
                                    setPasswordStage = new Stage();
                                    setPasswordStage.setTitle("Установка пароля ");
                                    setPasswordStage.setResizable(false);
                                    setPasswordStage.setScene(new Scene(fxmlSetPass, 300, 150));
                                    setPasswordStage.initModality(Modality.WINDOW_MODAL);
                                    setPasswordStage.showAndWait();

                                } else {
                                    setPasswordStage.show();
                                }
                            } else {
                                System.out.println("Не существует такой таблицы!!!");
                            }
                        }
                    });
                }
            }
        });


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                for (MenuItem item : menuButton_folder.getItems()) {
                    item.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (item.getText().equals("Новая папка")) {
                                try {
                                    Stage stage = new Stage();
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/newFolderNote.fxml"));
                                    Parent root = loader.load();
                                    newFolderController = loader.getController();
                                    newFolderController.columnNotesNewFolder = columnNotes;
                                    //newFolderController.menuButton = menuButton;
                                    stage.setTitle("Новая папка");
                                    stage.setScene(new Scene(root, 300, 150));
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.showAndWait();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (item.getText().equals("Удалить папку")) {
                                String ss = Controller.dataTable.get(Controller.currentTable);  // Видимый заголовок таблицы
                                String s1 = currentTable;
                                try {
                                    ConnectDB.deleteItems(s1);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }

                                dataTable.remove(s1);
                                System.out.println(dataTable);

                                currentTable = "NOTES";
                                if (currentTable == "NOTES") {
                                    menuButton_folder.getItems().get(1).setDisable(true);
                                }
                                try {
                                    //ConnectDB.feelDataDB();
                                    ConnectDB.showData(CollectionNote.noteList);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                columnNotes.setText(Controller.dataTable.get(Controller.currentTable));
                            }
                        }
                    });
                }
            }
        });
    }


    // Открытие окна установки пароля на приложение
    @FXML
    void openSetPassWindow(ActionEvent event) {
        Window parentWindow = ((Node) event.getSource()).getScene().getWindow();

        if (setPasswordStage == null) {
            setPasswordStage = new Stage();
            setPasswordStage.setTitle("Установка пароля ");
            setPasswordStage.setResizable(false);
            setPasswordStage.setScene(new Scene(fxmlSetPass, 300, 150));
            setPasswordStage.initModality(Modality.WINDOW_MODAL);
            setPasswordStage.getScene().getStylesheets().add((Main.class.getResource("../css/example.css")).toExternalForm());
            setPasswordStage.initOwner(parentWindow);

            setPasswordStage.showAndWait();
        } else {
            setPasswordStage.show();
        }
    }


    // NOTES Мои заметки
    @FXML
    void addNote(ActionEvent event) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/newNote.fxml"));
            root = loader.load();
            newNoteController = loader.getController();
            Scene scene = new Scene(root);
            ((Stage) MainAnchorPain.getScene().getWindow()).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openEditWindow() {
        tableNote.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Parent root = null;

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/editNote.fxml"));
                    root = loader.load();
                    Scene scene = new Scene(root);
                    ((Stage) MainAnchorPain.getScene().getWindow()).setScene(scene);
                    editController = loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Note selectedItem = (Note) tableNote.getSelectionModel().getSelectedItem();
                editController.setNote(selectedItem);
                System.out.println(tableNote.getSelectionModel().getSelectedItem());
            }
        });
    }

    @FXML
    void delNote(ActionEvent event) {
        ObservableList<Note> selectedNotes = tableNote.getSelectionModel().getSelectedItems();
        ArrayList<Note> rows = new ArrayList<>(selectedNotes);
        for (Note row : rows) {
            collectionNote.delete(row);

        }
    }


    public void updateCountLabel() {
        labelCount.setText("Количество записей: " + collectionNote.getNoteList().size());
    }

    ////////////////////////////////////////////////////////////////////////////////////
    @FXML
    void replaceNote(ActionEvent event) {

//        ObservableList<Note> selectedNotes = tableNote.getSelectionModel().getSelectedItems();
//        ArrayList<Note> rows = new ArrayList<>(selectedNotes);
        replaceOn = true;
        selectFolderMenu(event);


///////////////////////////////////////////////////////////////////////////////

    }

    // Выбор активной папки
    public void selectFolderMenu(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/selectFolder.fxml"));
            Parent root = loader.load();
            selectFolderController = loader.getController();
            selectFolderController.columnNotesSelectFolder = columnNotes;
            selectFolderController.menuButton_folder_1 = menuButton_folder;
            selectFolderController.tableNote = tableNote;

            for (Map.Entry<String, String> entry : dataTable.entrySet()) {
                String value = String.valueOf(entry.getValue());
                selectFolderController.getListViewSelectFolder().getItems().add(value);
                //selectFolderController.getListViewSelectFolder().getItems().add();
            }
            stage.setTitle("Выбор папки");
            stage.setScene(new Scene(root, 300, 300));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Заолнение таблицы данными
    public void fillSelectList() {
        selectFolderController = new selectFolderController();
        for (Map.Entry<String, String> entry : dataTable.entrySet()) {
            String value = String.valueOf(entry.getValue());
            selectFolderController.getListViewSelectFolder().getItems().add(value);
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////

//    void passwordWindow() {
//        try {
//            Stage stage = new Stage();
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Password.fxml"));
//            Parent root = loader.load();
//
//            stage.setTitle("NoteMain/Password");
//            stage.setScene(new Scene(root, 400, 200));
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.showAndWait();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    @FXML
    void OnActionMenuButton_folder(ActionEvent event) {
    }


    @FXML
    void choiseFolder(ActionEvent event) {

    }

    @FXML
    void choiseColour(ActionEvent event) {
    }

    @FXML
    void openSettingWindow(ActionEvent event) {

    }

    void openWindowCoiceColour(ActionEvent event) throws IOException {
        if (choiceColourDialogStage == null) {
            choiceColourDialogStage = new Stage();
            choiceColourDialogStage.setTitle("Выбор цвета");
            choiceColourDialogStage.setResizable(false);
            choiceColourDialogStage.setScene(new Scene(fxmlColour, 300, 150));
            choiceColourDialogStage.initModality(Modality.WINDOW_MODAL);
            choiceColourDialogStage.showAndWait();
        } else {
            choiceColourDialogStage.show();
        }
    }

    void openWindowCoSetting(ActionEvent event) throws IOException {

        if (settingsDialogStage == null) {
            settingsDialogStage = new Stage();
            settingsDialogStage.setTitle("Настройки");
            settingsDialogStage.setResizable(false);
            settingsDialogStage.setScene(new Scene(fxmlSetting, 300, 250));
            settingsDialogStage.initModality(Modality.WINDOW_MODAL);
            settingsDialogStage.showAndWait();
        } else {
            settingsDialogStage.show();
        }
    }


    @FXML
    void OnActionMenuButton(ActionEvent event) {
    }

    void actionClose(ActionEvent event) {
        Node sourse = (Node) event.getSource();
        Stage stage = (Stage) sourse.getScene().getWindow();
        stage.close();
    }
}

