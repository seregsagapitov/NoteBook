package com.seregsagapitov.controllers;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.interfaces.impls.CollectionNote;
import com.seregsagapitov.objects.Language;
import com.seregsagapitov.objects.Note;
import com.seregsagapitov.start.Main;
import com.seregsagapitov.utils.LocaleManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.zip.*;

public class Controller extends Observable implements Initializable {

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
    private ComboBox<Language> comboLocales;

    @FXML
    Button clearRecButton;

    public TableView<Note> getTableNote() {
        return tableNote;
    }

    TextArea textAreaFX;
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
    private TextField filterField;

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
    ResourceBundle resourceBundle;


    public static TreeMap<String, String> dataTable = new TreeMap<>();
    public static Integer count = 0;
    public static String currentTable = "";
    private static final String RU_CODE = "ru";
    private static final String EN_CODE = "en";


    static {
        dataTable.put("NOTES", "Мои заметки");
        dataTable.put("RECYCLED", "Корзина");
        currentTable = "NOTES";

    }

    @FXML
    public void initialize() throws SQLException, IOException {
        System.out.println(ConnectDB.selectPassword());
        fillLangCombobox();
        LocaleManager.setCurrentLanguage(comboLocales.getSelectionModel().getSelectedItem());
        listenCombo();


        if (currentTable == "NOTES" || currentTable == "RECYCLED") {
            menuButton_folder.getItems().get(1).setDisable(true);
        } else {
            menuButton_folder.getItems().get(1).setDisable(false);
        }

        if(currentTable != "RECYCLED"){
        clearRecButton.setVisible(false);}
        else {clearRecButton.setVisible(true);}


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



        ConnectDB.feelDataDB();


        // Реализация строки поиска filterField
        // FilteredList<Note> filteredList = new FilteredList<>(collectionNote.getNoteList(), b - > true);
        FilteredList<Note> filteredData = new FilteredList<>(collectionNote.getNoteList(), p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Note -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name field in your object with filter.
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(Note.getNoteText()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                    // Filter matches Notetext.
                } else if (String.valueOf(Note.getCurrentMoment()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches CurrentMoment.
                }
                return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<Note> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableNote.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        tableNote.setItems(sortedData);


        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                for (MenuItem item : menuButton.getItems()) {
                    item.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            if (item.getText().equals(resourceBundle.getString("SelectFolder"))) {
                                selectFolderMenu(event);
                            }

                            if (item.getText().equals(resourceBundle.getString("Recycled"))) {
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

                            if (item.getText().equals(resourceBundle.getString("ExportZipFile"))) {
                                exportToZipFile(event);
                            }

                            if (item.getText().equals(resourceBundle.getString("Password"))) {
                                if (setPasswordStage == null) {
                                    setPasswordStage = new Stage();
                                    setPasswordStage.setTitle(resourceBundle.getString("SetPassword"));
                                    setPasswordStage.setResizable(false);
                             ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.currentLanguage.getLocale());
//
                                    FXMLLoader fxmlLoader6 = new FXMLLoader(getClass().getResource("../fxml/setPassword.fxml"), resourceBundle);

                                    try {
                                        fxmlSetPass = fxmlLoader6.load();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    setPasswordStage.setScene(new Scene(fxmlSetPass, 300, 150));
                                    setPasswordStage.initModality(Modality.WINDOW_MODAL);
                                    setPasswordStage.showAndWait();

                                } else {
                                    setPasswordStage.show();
                                }
                            }


                            if (item.getText().equals(resourceBundle.getString("Exit"))) {
                                System.exit(0);
                                System.out.println("Кнопка выход и надо выйти!!!");
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
                            if (item.getText().equals(resourceBundle.getString("NewFolder"))) {
                                try {
                                    Stage stage = new Stage();
                                    ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.currentLanguage.getLocale());
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/newFolderNote.fxml"), resourceBundle);
                                    Parent root = loader.load();
                                    newFolderController = loader.getController();
                                    newFolderController.columnNotesNewFolder = columnNotes;
                                    //newFolderController.menuButton = menuButton;
                                    stage.setTitle(resourceBundle.getString("NewFolder"));
                                    stage.setScene(new Scene(root, 300, 150));
                                    stage.initModality(Modality.APPLICATION_MODAL);
                                    stage.showAndWait();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (item.getText().equals(resourceBundle.getString("DelFolder"))) {
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


//        Platform.runLater(new Runnable() {
//            @Override
//            public void run() {
//                // слушаем изменение языка
//                comboLocales.setOnAction(new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent event) {
//                        Language selectedLang = (Language) comboLocales.getSelectionModel().getSelectedItem();
//                        LocaleManager.setCurrentLanguage(selectedLang);
//
//                        // уведомить всех слушателей, что произошла смена языка
//                        setChanged();
//                        notifyObservers(selectedLang);
//                        System.out.println("Изменение языка");
//                        //fillLangCombobox();
//                    }
//                });
//
//            }
//        });
    }

    public void listenCombo() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // слушаем изменение языка
                comboLocales.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Language selectedLang = (Language) comboLocales.getSelectionModel().getSelectedItem();
                        LocaleManager.setCurrentLanguage(selectedLang);

                        // уведомить всех слушателей, что произошла смена языка
                        setChanged();
                        notifyObservers(selectedLang);
                        System.out.println("Изменение языка");
                    }
                });

            }
        });
    }

    // Открытие окна установки пароля на приложение
    @FXML
    void openSetPassWindow(ActionEvent event) {
        Window parentWindow = ((Node) event.getSource()).getScene().getWindow();

        if (setPasswordStage == null) {
            setPasswordStage = new Stage();
            setPasswordStage.setTitle(resourceBundle.getString("SetPassword"));
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
        ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.currentLanguage.getLocale());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/newNote.fxml"), resourceBundle);
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        ((Stage) addButton.getScene().getWindow()).setScene(scene);

    }

    @FXML
    void openEditWindow() {
        tableNote.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Parent root = null;

                if (tableNote.getSelectionModel().getSelectedItem() != null) {
                    try {

                        ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.currentLanguage.getLocale());
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/editNote.fxml"), resourceBundle);
                        root = loader.load();
                        Scene scene = new Scene(root);
                        ((Stage) addButton.getScene().getWindow()).setScene(scene);
                        editController = loader.getController();


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Note selectedItem = (Note) tableNote.getSelectionModel().getSelectedItem();
                    editController.setNote(selectedItem);
                    System.out.println(tableNote.getSelectionModel().getSelectedItem());
                } else {
                    Parent root1 = null;
                    try {
                        if (!currentTable.equals("RECYCLED")) {
                            ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.currentLanguage.getLocale());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/newNote.fxml"), resourceBundle);
                            root1 = loader.load();
                            newNoteController = loader.getController();
                            Scene scene = new Scene(root1);
                            ((Stage) MainAnchorPain.getScene().getWindow()).setScene(scene);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


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
        labelCount.setText(resourceBundle.getString("Count") + ": " + collectionNote.getNoteList().size());
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
            ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.currentLanguage.getLocale());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/selectFolder.fxml"), resourceBundle);
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
            stage.setTitle(resourceBundle.getString("SelectFolder"));
            stage.setScene(new Scene(root, 320, 300));
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
        // fillLangCombobox();
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
    private void exportToZipFile(ActionEvent event) {
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
        createAlertMessage("Создание Zip-архива", "Zip-архив " + Paths.get(zipFile).getFileName() + " успешно создан!", "Размер архива: " + new File(zipFile).length() + " байт");


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
    // Создание информационного окна

    void createAlertMessage(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    // Метод очистки корзины (безвозвратное удаление данных)
    @FXML
    void clearRecycled(ActionEvent event) {
        int sizeRecycled = collectionNote.getNoteList().size();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Корзина");
        alert.setHeaderText("Все файлы корзины будут удалены безвозвратно");
        alert.setContentText("Вы хотите очистить корзину?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            ConnectDB.deleteFromRecycled();
            try {
                ConnectDB.showData(collectionNote.getNoteList());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            createAlertMessage("Корзина очищена", "Корзина успешно очищена!", "Удалено " + sizeRecycled + " записей(сь)");
        } else {
            alert.close();
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

    public void fillLangCombobox() {
        Language langRU = new Language(RU_CODE, resourceBundle.getString("ru"), LocaleManager.RU_LOCALE, 0);
        Language langEN = new Language(EN_CODE, resourceBundle.getString("en"), LocaleManager.EN_LOCALE, 1);

        comboLocales.getItems().add(langRU);
        comboLocales.getItems().add(langEN);

        if (LocaleManager.getCurrentLanguage() == null) { // по умолчанию показывать русский язык
            comboLocales.getSelectionModel().select(0);
        } else {
            comboLocales.getSelectionModel().select(LocaleManager.getCurrentLanguage().getIndex());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.resourceBundle = resources;
            initialize();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

