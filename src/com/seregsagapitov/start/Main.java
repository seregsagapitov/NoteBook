package com.seregsagapitov.start;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.controllers.Controller;
import com.seregsagapitov.controllers.LoginController;
import com.seregsagapitov.objects.Language;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import sun.security.util.Password;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class Main extends Application implements Observer {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception {


        if (ConnectDB.selectPassword().equals("")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
          // loader.setResources(ResourceBundle.getBundle("../bundles/Locale_en.properties", new Locale("ru")));
            Parent root = loader.load();
           // controller.addObserver(this);
            //Parent root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
            //primaryStage.initStyle(StageStyle.TRANSPARENT);
            //primaryStage.setTitle(loader.getResources().getString("NoteBook"));
            primaryStage.setTitle("NoteBook");
            primaryStage.setResizable(false);
            Scene scene = new Scene(root, 300, 640);
            //scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Password.fxml"));
            Parent root = loader.load();

            primaryStage.setTitle("NoteMain");
            primaryStage.setResizable(false);
            Scene scene = new Scene(root, 300, 100);


            primaryStage.setScene(scene);
            primaryStage.show();

        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        Language language = (Language) arg;

    }
}
