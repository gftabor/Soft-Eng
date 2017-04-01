package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../main/resources/views/patientMenuStartView.fxml"));
        primaryStage.setTitle("Iteration 1 Minimal Application Correct");

        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root,1274 ,710));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
