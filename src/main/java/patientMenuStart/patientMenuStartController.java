package patientMenuStart;

import emergency.SmsSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URISyntaxException;


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

    @FXML
    private Label chooseLanguage_Label;

    @FXML
    private Label title_Label;



    //0 is english 1 is spanish
    int c_language = 0;

    //flag to set the choices only once
    boolean choicesSet = false;

    //flag for changing scenes once
    boolean loop_once = false;
    @FXML
    public void initialize(){
        setLanguageChoices(c_language);

    }

    public void setLanguageChoices(int i) {
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        languages_ChoiceBox.getItems().addAll("English", "Spanish");
        languages_ChoiceBox.getSelectionModel().select(i);

        //Checks if the user has decided to change languages
        languages_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            englishButtons_Labels();

                            //checks if the user wants spanish
                        } else if (newValue.intValue() == 1) {
                            spanishButtons_Labels();
                        }
                    }

                });
    }


    //Handling when the logIn Button is clicked
    /*public void logInButton_Clicked() {
        System.out.println("The log in button was clicked by the user");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminLoginMainView.fxml");
        adminLoginMain.adminLoginMainController controller = loader.getController();
        //sends the current language the next screen
        controller.setC_language(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

        //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }



    } */

    //Handling when the pathFinding Button is clicked
    //IMPORTANT
    public void pathFindingButton_Clicked(){
        System.out.println("CLICKING");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/pathFindingMenuView.fxml");
        pathFindingMenu.pathFindingMenuController controller = loader.getController();
        controller.setUserString("");
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


    //Switch screen to emergency scene
    /*public void emergencyButton_Clicked() {
        System.out.println("The user has clicked the emergency Button");
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

    }*/

    //switch screen to the directory searching
    public void directoryButton_Clicked(){
        System.out.println("The user has clicked the directory button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/hospitalDirectorySearchView.fxml");
        hospitalDirectorySearch.hospitalDirectorySearchController controller = loader.getController();

        controller.setUpTreeView();
        controller.setUserString("");

        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        controller.setUpTreeView();
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
    }

    //set the choices for the user at the beginning of the scene
    /*public void setLanguageChoices() {
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
                                //Updates the screen and switches the labels and Buttons to english
                                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
                                patientMenuStart.patientMenuStartController controller = loader.getController();
                                controller.englishButtons_Labels();

                            //checks if the user wants spanish
                            } else if (newValue.intValue() == 1) {
                                //System.out.println("Spanish");
                                //Updates the screen and switches teh labels and Buttons to spanish
                                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
                                patientMenuStart.patientMenuStartController controller = loader.getController();
                                controller.spanishButtons_Labels();
                            }
                        }
                    }
                });
    }*/

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        //change the current language to english
        c_language = 0;
        //Change the Buttons
        directory_Button.setText("Directory");
        pathFinding_Button.setText("Map");
        emergency_Button.setText("IN CASE OF EMERGENCY");
        logIn_Button.setText("Administrator");

        //Change the labels
        chooseLanguage_Label.setText("Choose your language");
        title_Label.setText("Welcome to Brigham and Women's Faulkner Hospital");
    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        directory_Button.setText("Directorio");
        pathFinding_Button.setText("Mapa");
        emergency_Button.setText("EN CASO DE EMERGENCIA");
        logIn_Button.setText("Administrador");

        //change the Labels
        chooseLanguage_Label.setText("Escoge tu lenguaje");
        title_Label.setText("Bienvenidos al Hospital Faulkner Brigham and Women ");

    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }







}
