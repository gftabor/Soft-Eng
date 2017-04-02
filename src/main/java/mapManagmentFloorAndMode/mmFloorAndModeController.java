package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.CheckBox;

/**
 * Created by AugustoR on 3/31/17.
 */
public class mmFloorAndModeController extends AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button emergency_Button;

    @FXML
    private Spinner<Integer> floorChoices_Spinner;

    @FXML
    private ChoiceBox<String> mode_ChoiceBox;

    @FXML
    private ChoiceBox<String> title_ChoiceBox;

    @FXML
    private CheckBox hidden_CheckBox;

    @FXML
    private Button submit_Button;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Label username_Label;



    //Flag to set choices once
    boolean setModeChoices = false;
    boolean setTitleChoices = false;


    //Sets the choices for the mode to the user to see
    public void modeChoiceBox_Clicked(){
        //System.out.println("Hello World");
        if(!setModeChoices) {
            System.out.println("Setting Choices");
            mode_ChoiceBox.getItems().addAll("Add", "Remove", "Edit");
            //mode_ChoiceBox.setValue("Add");
            setModeChoices = true;
        }
    }


    //set the choices for the title to the user to see
    public void titleChoiceBox_Clicked(){
        //
        if(!setTitleChoices){
            System.out.println("Setting Choices");
            title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom");
            setTitleChoices = true;
        }
    }

    //Make a function to set the value of the mode_ChoiceBox to "Add" with the same idea of the button
    public void emergencyButton_Clicked(){

        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "../main/resources/views/emergencyView.fxml");
    }

    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit Button");

        System.out.println(mode_ChoiceBox.getValue());


    }

    public void mainMenuButton_Clicked(){
        //Get the scene loader
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "../main/resources/views/adminMenuStartView.fxml");
        //Get the controller adminMenuStartController scene
        controllers.adminMenuStartController controller = loader.getController();
        //Set the username Label
        controller.setUsername(username_Label.getText());


    }

    //Set the username label to the current admin/patient
    public void setUserString(String user){
        username_Label.setText(user);
    }


}
