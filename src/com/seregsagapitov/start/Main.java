package com.seregsagapitov.start;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.controllers.Controller;
import com.seregsagapitov.objects.Language;
import com.seregsagapitov.utils.LocaleManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Main extends Application implements Observer {


    public static final String FXML_MAIN = "../fxml/main.fxml";
    public static final String BUNDLES_FOLDER = "com.seregsagapitov.bundles.Locale";
    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private Parent fxmlMain;
    private Controller controller;
    private FXMLLoader fxmlLoader;
    private AnchorPane currentRoot;


    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;

        if (ConnectDB.selectPassword().equals("")) {

            createGUI(LocaleManager.RU_LOCALE);
        } else {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(Main.BUNDLES_FOLDER, LocaleManager.EN_LOCALE);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Password.fxml"), resourceBundle);
            Parent root = loader.load();

            primaryStage.setTitle("NoteMain");
            primaryStage.setResizable(false);
            Scene scene = new Scene(root, 300, 100);
            primaryStage.setScene(scene);
            //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            primaryStage.show();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        Language language = (Language) arg;
        AnchorPane newNode = loadFXML(language.getLocale()); // получить новое дерево компонентов  с нужной локалью
        currentRoot.getChildren().setAll(newNode.getChildren()); // заменить старые дочерние компоненты на новые
    }

    // Загружаем дерево компонентов и возвращаем в виде AnchorPane (корневой элемент в FXML)
    private AnchorPane loadFXML(Locale locale) {
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(FXML_MAIN));
        fxmlLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, locale));

        AnchorPane node = null;

        try {
            node = (AnchorPane) fxmlLoader.load();
            controller = fxmlLoader.getController();
            controller.addObserver(this);
            primaryStage.setTitle(fxmlLoader.getResources().getString("NoteBook"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    public void createGUI(Locale locale) {
        currentRoot = loadFXML(locale);
        Scene scene = new Scene(currentRoot, 310, 640);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
