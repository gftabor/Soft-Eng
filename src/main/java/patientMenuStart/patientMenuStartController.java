package patientMenuStart;

import controllers.MapController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.HashMap;


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
    private Button directory_Button;

    @FXML
    private ChoiceBox<String> languages_ChoiceBox;

    //0 is english 1 is spanish
    int c_language = 0;

    //flag to set the choices only once
    boolean choicesSet = false;

    //flag for changing scenes once
    boolean loop_once = false;


    //Handling when the logIn Button is clicked
    public void logInButton_Clicked() {
        System.out.println("The log in button was clicked by the user");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminLoginMainView.fxml");
        adminLoginMain.adminLoginMainController controller = loader.getController();
        //sends the current language the user is in
        controller.setC_language(c_language);

    }

    //Handling when the pathFinding Button is clicked
    //IMPORTANT
    public void pathFindingButton_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/pathFindingMenuView.fxml");
        pathFindingMenu.pathFindingMenuController controller = loader.getController();
        MapController.getInstance().requestFloorMapCopy();
        MapController.getInstance().requestMapCopy();
        HashMap<Integer, controllers.Node> DBMap = MapController.getInstance().getCollectionOfNodes().getMap(4);
        controller.setUserString("");
    }


    //Switch screen to emergency scene
    public void emergencyButton_Clicked() {
        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    //switch screen to the directory searching
    public void directoryButton_Clicked(){
        System.out.println("The user has clicked the directory button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/hospitalDirectorySearchView.fxml");
        hospitalDirectorySearch.hospitalDirectorySearchController controller = loader.getController();
        controller.setUpTreeView();
    }

    //set the choices for the user at the beginning of the scene
    public void setLanguageChoices() {
        //Makes sure you only set the choices once
        if (!choicesSet) {
            //sets the choices and sets the current language as the top choice
            languages_ChoiceBox.getItems().addAll("English", "Spanish");
            languages_ChoiceBox.getSelectionModel().select(c_language);
            //makes sure to not do this again
            choicesSet = true;
        }

        //Checks if the user has decided to change languages
        languages_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //make sure you only execute the switching screen once
                        if (!loop_once) {
                            loop_once = true;
                            //System.out.println(newValue);
                            //Checks if the user wants english language
                            if (newValue.intValue() == 0) {
                                //System.out.println("English");
                                //Updates the screen and switches the labels to english
                                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
                                patientMenuStart.patientMenuStartController controller = loader.getController();
                                controller.englishLabels();

                            //checks if the user wants spanish
                            } else if (newValue.intValue() == 1) {
                                //System.out.println("Spanish");
                                //Updates the screen and switches teh labels to spanish
                                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
                                patientMenuStart.patientMenuStartController controller = loader.getController();
                                controller.spanishLabels();
                            }
                        }
                    }
                });
    }

    //switches all the labels to english
    public void englishLabels(){
        //change the current language to english
        c_language = 0;
        //Change the labels
        directory_Button.setText("Directory");
        pathFinding_Button.setText("Map");
        emergency_Button.setText("IN CASE OF EMERGENCY");
    }

    //switches all teh labels to spanish
    public void spanishLabels() {
        //change the current language to spanish
        c_language = 1;
        //change the labels
        directory_Button.setText("Directorio");
        pathFinding_Button.setText("Mapa");
        emergency_Button.setText("EN CASO DE EMERGENCIA");


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }




}
