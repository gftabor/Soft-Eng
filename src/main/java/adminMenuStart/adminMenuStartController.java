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

    @FXML
    private Label title_Label;

    //Set to english by default
    int c_language = 0;


    public void signOutButton_Clicked(){
        System.out.println("The user has clicked the sign out button");

        //Change to patient menu
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
        patientMenuStart.patientMenuStartController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }

    }

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
        emergency.emergencyController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }


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

        //sends the current language to the next screen
        controller.setC_language(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }



    }

    public void hospitalDirectoryButton_Clicked(){
         FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/mmNodeInformationView.fxml");
         mapManagementNodeInformation.mmNodeInformationController controller = loader.getController();

         controller.setTitleChoices();
         controller.setModeChoices();
         controller.setRoomChoices();
         controller.setDepartmentChoices();
         controller.setUpTreeView();
         controller.setUser(username_Label.getText());

        //sets the current language
        controller.setC_language(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }

    }

    public void mapManagementButton_Clicked(){
        System.out.println("The user has clicked the map management button");
        switch_screen(backgroundAnchorPane, "/views/mmFloorAndModeView.fxml");

        //Get the scene loader
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/mmFloorAndModeView.fxml");
        //Get the controller of the the scene
        mapManagementFloorAndMode.mmFloorAndModeController controller = loader.getController();
        controller.setUserString(username_Label.getText());

    }

    //Set the username coming from the main login
    public void setUsername_Admin(String user){
        username_Label.setText("Admin: " + user);
    }

    //Set the username coming from a scene
    public void setUsername(String user){
        username_Label.setText(user);
    }


    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        //change the current language to english
        c_language = 0;

        //Change the Buttons
        pathFinding_Button.setText("Map");
        hospitalDirectory_Button.setText("Directory Management");
        mapManagement_Button.setText("Map Management");
        SignOut_Button.setText("Sign Out");
        emergency_Button.setText("EMERGENCY");

        //Change the labels
        title_Label.setText("Welcome to Faulkner Hospital");
    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        pathFinding_Button.setText("Mapa");
        hospitalDirectory_Button.setText("Control de Directorio");
        mapManagement_Button.setText("Control de Mapa");
        SignOut_Button.setText("Salir");
        emergency_Button.setText("EMERGENCIA");

        //change the Labels
        title_Label.setText("Bienvenidos al Hospital de Faulkner");


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }


}
