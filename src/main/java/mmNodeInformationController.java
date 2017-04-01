package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;  //NOT JAVA SWING
import javafx.scene.layout.AnchorPane;
import java.io.IOException;



/**
 * Created by AugustoR on 3/31/17.
 */
public class mmNodeInformationController {

    @FXML
    private Label currentMode_Label;

    @FXML
    private Label currentAdmin_Label;

    @FXML
    private Button signOut_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private TextField title_TextField;

    @FXML
    private TextField name_TextField;

    @FXML
    private TextField department_TextField;

    @FXML
    private TextField location_TextField;

    @FXML
    private TextField availability_TextField;

    @FXML
    private TextField contactInfo_TextField;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    public void cancelButton_Clicked(){
        System.out.println("The user has clicked the cancel Button");

    }

    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit Button");

    }

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");

    }

    public void signOutButton_Clicked(){
        System.out.println("The user has clicked the sign out Button");

    }


}
