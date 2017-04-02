package mapManagementFloorAndMode;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

/**
 * Created by AugustoR on 3/31/17.
 */
public class mmFloorAndModeController extends controllers.AbsController{
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
    private Label username_Label;

    @FXML
    private Button submit_Button;

    @FXML
    private Button mainMenu_Button;



    //Flag to only set choices once
    int setChoices = 0;
    int setTitleChoices = 0;

    public void modeChoiceBox_Clicked(){
        //System.out.println("Hello World");
        if(setChoices == 0) {
            System.out.println("Setting Choices");
            mode_ChoiceBox.getItems().addAll("Add", "Remove", "Edit");
            //mode_ChoiceBox.setValue("Add");
            setChoices = 1;
        }
    }
    //Make a function to set the value of the mode_ChoiceBox to "Add" with the same idea of the button
    public void titleChoiceBox_Clicked(){
        if(setTitleChoices == 0){
            System.out.println("Setting choices");
            title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom");
            setTitleChoices = 1;
        }
    }

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");
    }

    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit Button");
    }

    public void mainMenuButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
    }

    public void setUserString(String user){
        username_Label.setText(user);
    }

}
