package adminLoginMain;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.application.Platform;


import javafx.scene.control.*;

import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.VBox;


/**
 * Created by AugustoR on 4/1/17.
 */


public class adminLoginMainController extends controllers.AbsController{
    @FXML
    private TextField username_TextField;

    @FXML
    private PasswordField password_PasswordField;

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button logIn_Button;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Label invalidLogInputs;

    @FXML
    private Label adminTitle_Label;

    @FXML
    private Label languageTitle_Label;

    @FXML
    private VBox root;

    @FXML
    private ChoiceBox<String> languageChoices_ChoiceBox;

    int c_language;

    private AdminLoginManager loginManage;

    public void initialize() {
        facialRecognition.getInstance().start(root);
        facialRecognition.getInstance().scan(this);
    }
    public FXMLLoader switch_screen(AnchorPane BGCurrentanchor, String viewPath){
        facialRecognition.getInstance().stop();
        return super.switch_screen(BGCurrentanchor,viewPath);
    }
    private String faceUserName;
    public void alternateLogIn(String username){
        faceUserName = username;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                logIn(faceUserName);
            }
        });

    }
    public void logIn(String username) {
        loginManage = new AdminLoginManager();
        System.out.println("permissions  " +loginManage.getPermissions(username));
        if (loginManage.getPermissions(username) == 2) {
            System.out.println("Logging in " + username);
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewMainMapManagementView.fxml");
            NewMainMapManagement.NewMainMapManagementController controller = loader.getController();
            controller.setC_language(c_language);
            //Set the correct username for the next scene
            //set up english labels
            if (c_language == 0) {
                controller.englishButtons_Labels();

                //set up spanish labels
            } else if (c_language == 1) {
                controller.spanishButtons_Labels();
            }
            controller.setUserString(username);
            controller.setPermissionLevel(2);


            //LOG IN EMPLOYEE
            //*************************************************
        } else if (loginManage.getPermissions(username) == 1) {
            System.out.println("Logging in Employee");
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
            //patientMenuStart.patientMenuStartController controller = loader.getController();
            NewIntroUI.NewIntroUIController controller = loader.getController();
            //sets the current language
            controller.setCurrentLanguage(c_language);
            //set up english labels
            if (c_language == 0) {
                controller.englishButtons_Labels();
                //set up spanish labels
            } else if (c_language == 1) {
                controller.spanishButtons_Labels();
            }
            controller.setWelcome(username);
            controller.setPermissionLevel(1);
            controller.setLanguage_ChoiceBox(c_language);
            controller.loginOrOut(0, c_language);
        }
    }
        //logs the user in
    public void logInButton_Clicked(){
        loginManage = new AdminLoginManager();
        String username = username_TextField.getText();
        String password = password_PasswordField.getText();

        System.out.println("The user has clicked the log in Button");
        System.out.println(username);
        System.out.println(password);

        if(loginManage.verifyCredentials(username, password) == 1){
            System.out.println("Correct Password");

            //LOG IN ADMIN
            //*************************************************
            logIn(username);


            //Check is the username/passwords inputs are empty
        }else if(username.equals("")){
            invalidLogInputs.setText("Enter your username.");

        }else if(password.equals("")){
            invalidLogInputs.setText("Enter your password.");

        }else{//Incorrect inputs
            invalidLogInputs.setText("Incorrect credentials, try again.");

        }


    }

    //Switches screen to the patient menu
    public void mainMenuButton_Clicked(){
        System.out.println("The user has clicked the main menu Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        //patientMenuStart.patientMenuStartController controller = loader.getController();
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

        //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }

        controller.setLanguage_ChoiceBox(c_language);
        //Set the permissions
        controller.setPermissionLevel(0);

    }

    //Sets the choiceBox from the given language
    public void setLanguageChoiceBox(int l){
        //Check if english
          languageChoices_ChoiceBox.getItems().addAll("English", "Espanol");
          languageChoices_ChoiceBox.getSelectionModel().select(l);


        //Checks if the user has decided to change languages
        languageChoices_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load English View
                            englishButtons_Labels();

                        } else if (newValue.intValue() == 1) {
                            //Load Spanish View
                            spanishButtons_Labels();
                            System.out.println("");
                        }
                    }

                });


    }

    //sets the current language to the given language
    public void setC_language(int i){
        c_language = i;
    }

    //Changes the buttons and labels to english
    public void englishButtons_Labels(){
        setC_language(0);
        //Buttons
        logIn_Button.setText("Login");
        mainMenu_Button.setText("Main Menu");

        //Labels
        adminTitle_Label.setText("Admin Login");
        languageTitle_Label.setText("Choose your language");

        //text fields
        username_TextField.setPromptText("username");
        password_PasswordField.setPromptText("password");
    }

    //Changes the buttons and labels to spanish
    public void spanishButtons_Labels(){
        setC_language(1);
        //Buttons
        logIn_Button.setText("Iniciar Sesion");
        mainMenu_Button.setText("Menu Principal");

        //Labels
        adminTitle_Label.setText("Administrador");
        languageTitle_Label.setText("Escoge tu lenguaje");

        //text fields
        username_TextField.setPromptText("usuario");
        password_PasswordField.setPromptText("contrasena");
    }



}
