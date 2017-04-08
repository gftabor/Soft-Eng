package adminMenuStart;
import controllers.MapController;
import controllers.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;

/**
 * Created by AugustoR on 4/1/17.
 */
public class adminMenuStartController extends controllers.AbsController{
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
        switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");

    }

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");

    }

    public void pathFindingButton_Clicked(){
        System.out.println("The user has clicked the pathfinding button");

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/pathFindingMenuView.fxml");
        pathFindingMenu.pathFindingMenuController controller = loader.getController();
        MapController.getInstance().requestFloorMapCopy();
        MapController.getInstance().requestMapCopy();
        HashMap<Integer, Node> DBMap = MapController.getInstance().getCollectionOfNodes().getMap(4);
        //controller.setMapAndNodes(DBMap);
        controller.setUserString(username_Label.getText());

    }

    public void hospitalDirectoryButton_Clicked(){
         FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/mmNodeInformationView.fxml");
         mapManagementNodeInformation.mmNodeInformationController controller = loader.getController();
         // controller.createDirectoryTreeView();
         //controller.setTitleChoices();
         //controller.setModeChoices();
         //controller.setRoomChoices();
         //controller.setDepartmentChoices();
         //controller.setUser(username_Label.getText());
         //controller.setUser(username_Label.getText());


    }

    public void mapManagementButton_Clicked(){
        System.out.println("The user has clicked the map management button");
        switch_screen(backgroundAnchorPane, "/views/mmFloorAndModeView.fxml");

        //Get the scene loader
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/mmFloorAndModeView.fxml");
        //Get the controller of the the scene
        mapManagementFloorAndMode.mmFloorAndModeController controller = loader.getController();
        //Set the username label
    }

    //Set the username coming from the main login
    public void setUsername_Admin(String user){
        username_Label.setText("Admin: " + user);
    }

    //Set the username coming from a scene
    public void setUsername(String user){
        username_Label.setText(user);
    }


}
