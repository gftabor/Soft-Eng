package patientMain;

/**
 * Created by AugustoR on 4/15/17.
 */
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class patientMainController extends controllers.AbsController{

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private Button admin_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Label start_Label;

    @FXML
    private TextField start_textField;

    @FXML
    private Label end_Label;

    @FXML
    private TextField end_TextField;

    @FXML
    private Button clear_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private ChoiceBox<String> filter_ChoiceBox;

    @FXML
    private TreeTableView<?> allInformation_TableView;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    @FXML
    private Label floor_Label;

    @FXML
    private Label c_Floor_Label;

    @FXML
    private Label language_Label;

    @FXML
    private ChoiceBox<String> language_ChoiceBox;

    @FXML
    private ComboBox<String> filter_comboBox;

    int c_language = 0;

    int first_Time = 0;

    @FXML
    public void initialize(){
        //setLanguageChoices(c_language);
        setFloorChoices();
        setFilterChoices();
        setLanguage_ChoiceBox();


    }

    //Sets the choices for the language
    public void setLanguage_ChoiceBox(){
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        language_ChoiceBox.getItems().addAll("English", "Espanol");
        language_ChoiceBox.getSelectionModel().select(0);
        language_ChoiceBox.setTooltip(new Tooltip("Select the language"));

        //Checks if the user has decided to change languages
        language_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load English View
                            System.out.println("languages");
                            englishButtons_Labels();

                        } else if (newValue.intValue() == 1) {
                            //Load Spanish View
                            spanishButtons_Labels();

                        }
                    }

                });

    }

    public void setComboBox(){
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        if(c_language == 0) {
            filter_comboBox.getSelectionModel().clearSelection();
            filter_comboBox.getItems().clear();
            filter_comboBox.getItems().addAll("All", "Employees", "Services", "Frequently Searched", "Miscellaneous");
            filter_comboBox.getSelectionModel().select(0);
        }else if(c_language == 1){
            filter_comboBox.getSelectionModel().clearSelection();
            filter_comboBox.getItems().clear();
            filter_comboBox.getItems().addAll("Todo", "Empleados", "Servicios", "Buscados Frequentemente", "Varios");
            filter_comboBox.getSelectionModel().select(0);

        }



        //Checks if the user has decided to change languages
        filter_comboBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load everything

                        } else if (newValue.intValue() == 1) {
                            //Load only employees

                        } else if (newValue.intValue() == 2) {
                            //Load services

                        }else if(newValue.intValue() == 3){
                            //load frequently searched

                        }else if(newValue.intValue() == 4){
                            System.out.println("Hello World");
                            //Miscellaneous
                        }
                    }

                });


    }

    //Set the choices for Filter
    public void setFilterChoices(){
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        if(c_language == 0) {
            filter_ChoiceBox.getSelectionModel().clearSelection();
            filter_ChoiceBox.getItems().clear();
            filter_ChoiceBox.getItems().addAll("All", "Employees", "Services", "Frequently Searched", "Miscellaneous");
            filter_ChoiceBox.getSelectionModel().select(0);
        }else if(c_language == 1){
            filter_ChoiceBox.getSelectionModel().clearSelection();
            filter_ChoiceBox.getItems().clear();
            filter_ChoiceBox.getItems().addAll("Todo", "Empleados", "Servicios", "Buscados Frequentemente", "Varios");
            filter_ChoiceBox.getSelectionModel().select(0);

        }

        //Checks if the user has decided to change languages
        filter_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load everything

                        } else if (newValue.intValue() == 1) {
                            //Load only employees

                        } else if (newValue.intValue() == 2) {
                            //Load services

                        }else if(newValue.intValue() == 3){
                            //load frequently searched

                        }else if(newValue.intValue() == 4){
                            //Miscellaneous
                        }
                    }

                });
    }



    public void setFloorChoices() {
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        floor_ChoiceBox.getItems().addAll("1", "2", "3","4","5","6","7");
        floor_ChoiceBox.getSelectionModel().select(0);

        //Checks if the user has decided to change languages
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load floor 1

                        } else if (newValue.intValue() == 1) {
                            //Load floor 2

                        }else if (newValue.intValue() == 2) {
                            //Load floor 3

                        }else if (newValue.intValue() == 3) {
                            //Load floor 4

                        }else if (newValue.intValue() == 4) {
                            //Load floor 5

                        }else if (newValue.intValue() == 5) {
                            //Load floor 6

                        }else if (newValue.intValue() == 6) {
                            //Load floor 7

                        }
                    }

                });
    }

    //Handles the action when the submit button is clicked
    public void submitButton_Clicked(){

    }

    //Handling when the logIn Button is clicked
    public void logInButton_Clicked() {

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
    }

    //Checks if the emergency button was clicked
    public void emergencyButton_Clicked() {
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
    }

    //Handles the action when the clear button is clicked
    public void clearButton_Clicked(){
        start_textField.setText("");
        end_TextField.setText("");
    }

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        c_language = 0;
        //change the current language to english

        //Change the Buttons
        admin_Button.setText("Administrator");
        emergency_Button.setText("EMERGENCY");
        clear_Button.setText("Clear");
        submit_Button.setText("Submit");

        //Change the labels
        start_Label.setText("Start");
        end_Label.setText("End");
        language_Label.setText("Language");
        mainTitle_Label.setText("Welcome to Brigham and Women's Faulkner Hospital");
        floor_Label.setText("Floor");

        //Change the textFields
        start_textField.setPromptText("Starting position");
        end_TextField.setPromptText("Ending position");

        //Change choiceBox
        setFilterChoices();


    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        admin_Button.setText("Administrador");
        emergency_Button.setText("EMERGENCIA");
        clear_Button.setText("Borrar");
        submit_Button.setText("Listo");

        //change the Labels
        start_Label.setText("Inicio");
        end_Label.setText("Destino");
        language_Label.setText("Languaje");
        mainTitle_Label.setText("Bienvenidos al Hospital Faulkner Brigham and Women");
        floor_Label.setText("Piso");

        //Change the textFields
        start_textField.setPromptText("Nombre de inicio");
        end_TextField.setPromptText("Nombre del destino");

        //Change choiceBox
        setFilterChoices();


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }



}
