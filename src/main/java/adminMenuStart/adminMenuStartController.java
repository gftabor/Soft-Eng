package adminMenuStart;
import controllers.MapController;
import controllers.Node;
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

    @FXML
    private Label default_Search;

    @FXML
    private Label chooseLanguage_Label;

    @FXML
    private ChoiceBox<String> languages_ChoiceBox;

    @FXML
    private ChoiceBox<String> search_ChoiceBox;

    @FXML
    private Button addAdmin_Button;

    //Set to english by default
    int c_language = 0;

    int c_algorithm = 0;

    MapController mapController = MapController.getInstance();

    AutoLogout al = new AutoLogout();

    @FXML
    public void initialize() {
        search_ChoiceBox.getItems().addAll("A*", "Depth First", "Breadth First");
        search_ChoiceBox.getSelectionModel().select(0);
        search_ChoiceBox.setValue("" + mapController.getAlgorithm());
        search_ChoiceBox.getSelectionModel()
                .selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> changeSearch(newValue) );

    }


    public void changeSearch(String algorithm){
        if(algorithm.equals("A*")){
            mapController.setAlgorithm(0);
        }else if(algorithm.equals("Depth First")){
            mapController.setAlgorithm(2);
        }else if(algorithm.equals("Breadth First")){
            mapController.setAlgorithm(1);
        }else{
            mapController.setAlgorithm(0);
        }
    }


    public void signOutButton_Clicked(){
        System.out.println("The user has clicked the sign out button");

        //Change to patient menu
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/patientMainView.fxml");
        patientMain.patientMainController controller = loader.getController();
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

    //Changes the scene to the admin sign up view
    public void addAdmin(){
        //Change to patient menu
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminSignUpView.fxml");
        adminSignUp.adminSignUpController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //Gets the current admin
        controller.setUsername(username_Label.getText());
        //set up english labels

        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }

        controller.setUpTreeView();
        controller.setModeChoices();


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

        //sends the current language to the next screen
        controller.setC_language(c_language);
        MapController.getInstance().requestFloorMapCopy();
        MapController.getInstance().requestMapCopy();
        HashMap<Integer, Node> DBMap = MapController.getInstance().getCollectionOfNodes().getMap(4);
        //controller.setMapAndNodes(DBMap);

        controller.setUserString(username_Label.getText());


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

         //sets the current language
         controller.setC_language(c_language);

         controller.setModeChoices();
         controller.setRoomChoices();
         //controller.setDepartmentChoices();
         controller.setUpTreeView();
         controller.setUser(username_Label.getText());

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
        //Set the username label
        controller.setUserString(username_Label.getText());

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

    //Set the username coming from the main login
    public void setUsername_Admin(String user){
        username_Label.setText(user);
    }

    //Set the username coming from a scene
    public void setUsername(String user){
        username_Label.setText(user);
    }

    //set the choices for the user at the beginning of the scene
    public void setLanguageChoices(){
        //Makes sure you only set the choices once

        //sets the choices and sets the current language as the top choice
        languages_ChoiceBox.getItems().addAll("English", "Espanol");
        languages_ChoiceBox.getSelectionModel().select(c_language);
        //makes sure to not do this again

        //Checks if the user has decided to change languages
        languages_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //make sure you only execute the switching screen once
                            //System.out.println(newValue);
                            //Checks if the user wants english language
                            if (newValue.intValue() == 0) {
                                //System.out.println("English");
                                //Updates the screen and switches the labels and Buttons to english
                                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
                                adminMenuStart.adminMenuStartController controller = loader.getController();
                                controller.englishButtons_Labels();
                                controller.setUsername_Admin(username_Label.getText());
                                controller.setLanguageChoices();
                                //checks if the user wants spanish
                            } else if (newValue.intValue() == 1) {
                                //System.out.println("Spanish");
                                //Updates the screen and switches teh labels and Buttons to spanish
                                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
                                adminMenuStart.adminMenuStartController controller = loader.getController();
                                controller.spanishButtons_Labels();
                                controller.setUsername_Admin(username_Label.getText());
                                controller.setLanguageChoices();
                            }
                    }
                });
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
        addAdmin_Button.setText("Admin Management");

        //Change the labels
        title_Label.setText("Welcome to Faulkner Hospital");
        default_Search.setText("Default Algorithm");
        chooseLanguage_Label.setText("Choose your language");
    }

    //switches all the labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons

        pathFinding_Button.setText("Mapa");
        hospitalDirectory_Button.setText("Control de Directorio");
        mapManagement_Button.setText("Control de Mapa");
        SignOut_Button.setText("Salir");
        emergency_Button.setText("EMERGENCIA");
        addAdmin_Button.setText("Administración admin");
        //change the Labels
        title_Label.setText("Bienvenidos al Hospital de Faulkner");
        default_Search.setText("Algoritmo Predeterminado");
        chooseLanguage_Label.setText("Escoje tu lenguaje");
    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

    public void pathFinding_Button_Clicked(){
        //Change to patient menu
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/patientMainView.fxml");
        patientMain.patientMainController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        controller.setPermissionLevel(2);
        controller.setWelcome(username_Label.getText());
    }

    public void autoLogout() {
        System.out.println("The system has automatically logged off due to inactivity.");

        //Change to patient menu
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/patientMainView.fxml");
        patientMain.patientMainController controller = loader.getController();
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
}
