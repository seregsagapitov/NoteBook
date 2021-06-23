package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Note;
import com.seregsagapitov.start.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.*;

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

    @FXML
    Button clearRecButton;

    public TableView<Note> getTableNote() {
        return tableNote;
    }

    @FXML
    private TableView<Note> tableNote;
    @FXML
    private TableColumn<Note, String> columnNotes;
    @FXML
     Button delButton;
    @FXML
    Button addButton;
    @FXML
    private Button replaceButton;
    @FXML
    private MenuButton menuButton_folder;
    @FXML
    private AnchorPane MainAnchorPain;

    @FXML
    private MenuItem exportZipItem;


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

        if (currentTable == "NOTES" || currentTable == "RECYCLED") {
            menuButton_folder.getItems().get(1).setDisable(true);
        } else {
            menuButton_folder.getItems().get(1).setDisable(false);
        }
        clearRecButton.setVisible(false);


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

                                delButton.setDisable(true);
                                addButton.setDisable(true);
                                clearRecButton.setVisible(true);


                            }

                            if (item.getText().equals("Отправить в Zip архив...")) {
                                exportToZipFile(event);
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


                }catch (IOException e) {
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

        collectionNote.delete(rows);
//        for (Note row : rows) {
//            collectionNote.delete(row);
//
//        }
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
            selectFolderController.addButton = addButton;
            selectFolderController.delButton = delButton;
            selectFolderController.clearRecButton = clearRecButton;

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


    // Сохранение данных приложения в Zip-файл
    @FXML
    void exportToZipFile(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Выбор папки для Zip-архива");

        File selectedDir = directoryChooser.showDialog(tableNote.getScene().getWindow());


        Path pathRoot = Paths.get("Notebook");
        try {
            Files.createDirectory(pathRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String notetext = "";
        String currentMoment = "";
        ObservableList<Note> noteList = FXCollections.observableArrayList();

        ConnectDB.connect();
        for (Map.Entry<String, String> entry : dataTable.entrySet()) {
            Path path = Paths.get(pathRoot + "/" + dataTable.get(entry.getKey()));
//            try {
//                //Files.createDirectory(Paths.get(pathRoot + "/" + path));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
            try {
                Statement stmt = ConnectDB.connection.createStatement();
                String query = "SELECT * FROM " + entry.getKey() + " ;";
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {

                    noteList.add(new Note(rs.getString("NOTETEXT"), rs.getString("DATE"), rs.getInt("ID")));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            String fileName1 = "";


            try {
                for (int i = 0; i < noteList.size(); i++) {

                    try {
                        Files.createDirectories(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (noteList.get(i).getNoteText().length() > 7) {
                        fileName1 = noteList.get(i).getNoteText().substring(0, 7) + ".txt";
                    } else {
                        fileName1 = noteList.get(i).getNoteText() + ".txt";
                    }
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path + "/" + fileName1), 8192);
                    bufferedWriter.write(noteList.get(i).getCurrentMoment() + "\n" + noteList.get(i).getNoteText());
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            noteList.clear();
        }
        ConnectDB.disconnect();
        String zipFile = selectedDir + "/" + pathRoot + ".zip";
        try {
            Zip(pathRoot.toString(), zipFile);

            // Удаление созданного каталога
            Files.walkFileTree(pathRoot, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                        throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Zip(String source_dir, String zip_file) throws Exception {
        // Cоздание объекта ZipOutputStream из FileOutputStream
        FileOutputStream fout = new FileOutputStream(zip_file);
        ZipOutputStream zout = new ZipOutputStream(fout);
        zout.setLevel(Deflater.BEST_COMPRESSION);


        // Создание объекта File object архивируемой директории
        File fileSource = new File(source_dir);
        addDirectory(zout, fileSource);
        // Закрываем ZipOutputStream
        zout.closeEntry();
        zout.close();
        System.out.println("Zip файл создан!");
    }

    private void addDirectory(ZipOutputStream zout, File fileSource)
            throws Exception {
        File[] files = fileSource.listFiles();
        System.out.println("Добавление директории <" + fileSource.getName() + ">");
        for (int i = 0; i < files.length; i++) {
            // Если file является директорией, то рекурсивно вызываем
            // метод addDirectory
            if (files[i].isDirectory()) {
                addDirectory(zout, files[i]);
                continue;
            }
            System.out.println("Добавление файла <" + files[i].getName() + ">");

            FileInputStream fis = new FileInputStream(files[i]);

            zout.putNextEntry(new ZipEntry(files[i].getPath()));

            byte[] buffer = new byte[4048];
            int length;
            while ((length = fis.read(buffer)) > 0)
                zout.write(buffer, 0, length);
            // Закрываем ZipOutputStream и InputStream
            zout.closeEntry();
            fis.close();
        }
    }

    // Метод очистки корзины (безвозвратное удаление данных)
    @FXML
    void clearRecycled(ActionEvent event) {
        ConnectDB.deleteFromRecycled();
        try {
            ConnectDB.showData(collectionNote.getNoteList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

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

