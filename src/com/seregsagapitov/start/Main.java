package com.seregsagapitov.start;

import com.seregsagapitov.DB.ConnectDB;
import com.seregsagapitov.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import sun.security.util.Password;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {


        if (ConnectDB.selectPassword().equals("")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/main.fxml"));
            //loader.setResources(ResourceBundle.getBundle("../bundless/Locale_en.properties", new Locale("en")));
            Parent root = loader.load();
            //Parent root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
            //primaryStage.initStyle(StageStyle.TRANSPARENT);
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
}
