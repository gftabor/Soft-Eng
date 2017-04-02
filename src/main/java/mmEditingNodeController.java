package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Created by AugustoR on 4/2/17.
 */
public class mmEditingNodeController {
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label currentMode_Label;

    @FXML
    private Label currentFloor_Label;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    public void mainMenuButton_Clicked(){
        System.out.println("The user clicked the main Menu Button");
    }

    public void emergencyButton_Clicked(){
        System.out.println("The user clicked the emergency Button");
    }

    public void cancelButton_Clicked(){
        System.out.println("The user clicked the cancel Button");
    }

    public void submitButton_Clicked(){
        System.out.println("The user clicked the submit Button");
    }

}
