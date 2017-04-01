package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;  //NOT JAVA SWING
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
/**
 * Created by AugustoR on 3/31/17.
 */
public class emergencyController {

    @FXML
    private Button mainMenu_Button;

    public void mainMenuButton_Clicked() {
      System.out.println("The user clicked the main menu Button");
    }

}
