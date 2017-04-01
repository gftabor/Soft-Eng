package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;  //NOT JAVA SWING
import javafx.scene.layout.AnchorPane;

/**
 * Created by AugustoR on 3/30/17.
 */
public class patientMenuStartController extends AbsController{
    //To wrap the scenes IMPORTANT
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button logIn_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Button pathFinding_Button;

    @FXML
    private Button hospitalDirectory_Button;

    @FXML
    private Button mapManagement_Button;


    Boolean logInSuccess = true;

    //Handling when the logIn Button is clicked
    public void logInButton_Clicked(){
        System.out.println("The log in button was clicked by the user");



        //Display the Hospital Directory button
        /*if(logInSuccess == true) {
            hospitalDirectory_Button.setDisable(false);
            mapManagement_Button.setDisable(false);
        }else{
            System.out.println("Could not log in");
        }*/


    }

    //Handling when the pathFinding Button is clicked
    //IMPORTANT
    public void pathFindingButton_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane,"../main/resources/views/pathFindingMenuView.fxml");
        //pathFindingMenuController controller = loader.getController();
        //controller.setMode(username_TextField.getText());

    }


    //Handling when the hospitalDirectory Button is clicked
    public void hospitalDirectoryButton_Clicked(){
        System.out.println("The hospital Directory button was clicked by the user");

    }

    //Handling when the mapManagement Button is clicked
    public void mapManagementButton_Clicked(){
        switch_screen(backgroundAnchorPane,"../main/resources/views/mmFloorAndModeView.fxml");

    }

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");

    }



}
