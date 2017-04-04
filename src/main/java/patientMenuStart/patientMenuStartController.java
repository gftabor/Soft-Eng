package patientMenuStart;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;  //NOT JAVA SWING
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;
import java.util.Hashtable;


/**
 * Created by AugustoR on 3/30/17.
 */
public class patientMenuStartController extends controllers.AbsController{
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

    @FXML
    private Label patient_Label;


    Boolean logInSuccess = true;

    //Handling when the logIn Button is clicked
    public void logInButton_Clicked(){
        System.out.println("The log in button was clicked by the user");
        FXMLLoader loader = switch_screen(backgroundAnchorPane,"/views/adminLoginMainView.fxml");


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
        //FXMLLoader loader = switch_screen(backgroundAnchorPane,"../main/resources/views/pathFindingMenuView.fxml");
        //pathFindingMenuController controller = loader.getController();
        //controller.setMode(username_TextField.getText());

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/pathFindingMenuView.fxml");
        pathFindingMenu.pathFindingMenuController controller = loader.getController();

        HashMap<Integer, controllers.Node> DBMap = new HashMap<>();
        DBMap.put(5,new controllers.Node(11,100,true,true,"tests",4));
        DBMap.put(6,new controllers.Node(200,100,true,true,"tests",4));
        DBMap.put(7,new controllers.Node(300,300,true,true,"tests",5));
        controller.setMapAndNodes(DBMap);
        controller.setUserString("");

    }


    //Handling when the hospitalDirectory Button is clicked
    public void hospitalDirectoryButton_Clicked(){
        System.out.println("The hospital Directory button was clicked by the user");

    }

    //Handling when the mapManagement Button is clicked
    public void mapManagementButton_Clicked(){
        switch_screen(backgroundAnchorPane,"/views/mmFloorAndModeView.fxml");

    }

    //Switch screen to emergency scene
    public void emergencyButton_Clicked() {
        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }



}
