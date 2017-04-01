package controllers;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
/**
 * Created by AugustoR on 4/1/17.
 */
public class adminMenuStartController extends AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label username_Label;

    @FXML
    private Button SignOut_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Button pathFinding_Button;

    @FXML
    private Button hospitalDirectory_Button;

    @FXML
    private Button mapManagement_Button;


    public void signOutButton_Clicked(){
        System.out.println("The user has clicked the sign out button");

        //Change to patient menu
        switch_screen(backgroundAnchorPane, "../main/resources/views/patientMenuStartView.fxml");

    }

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency button");

    }

    public void pathFindingButton_Clicked(){
        System.out.println("The user has clicked the pathfinding button");
        switch_screen(backgroundAnchorPane, "../main/resources/views/pathFindingMenuView.fxml");

    }

    public void hospitalDirectoryButton_Clicked(){
        System.out.println("The user has clicked the hospital directory button");

    }

    public void mapManagementButton_Clicked(){
        System.out.println("The user has clicked the map management button");
        switch_screen(backgroundAnchorPane, "../main/resources/views/mmFloorAndModeView.fxml");


    }

}
