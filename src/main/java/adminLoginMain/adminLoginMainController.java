package adminLoginMain;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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

    int c_language;

    //logs the user in
    public void logInButton_Clicked(){
        AdminLoginManager loginManage = new AdminLoginManager();
        String username = username_TextField.getText();
        String password = password_PasswordField.getText();

        System.out.println("The user has clicked the log in Button");
        System.out.println(username);
        System.out.println(password);

        if(loginManage.verifyCredentials(username, password) == 1){
            System.out.println("Correct Password");

            //LOG IN ADMIN
            //*************************************************
            if(loginManage.getPermissions(username) == 2){
                System.out.println("Logging in Admin");
                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
                adminMenuStart.adminMenuStartController controller = loader.getController();
                //Set the correct username for the next scene
                controller.setUsername_Admin("Admin: "+ username_TextField.getText());
                //sets the current language
                controller.setCurrentLanguage(c_language);
                //set up english labels
                if(c_language == 0){
                    controller.englishButtons_Labels();

                    //set up spanish labels
                }else if(c_language == 1){
                    controller.spanishButtons_Labels();
                }
                controller.setLanguageChoices();

                //LOG IN EMPLOYEE
                //*************************************************
            }else if(loginManage.getPermissions(username) == 1){
                System.out.println("Logging in Employee");
                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMainView.fxml");
                //patientMenuStart.patientMenuStartController controller = loader.getController();
                patientMain.patientMainController controller = loader.getController();
                //sets the current language
                controller.setCurrentLanguage(c_language);
                //set up english labels
                if(c_language == 0){
                    controller.englishButtons_Labels();
                    //set up spanish labels
                }else if(c_language == 1){
                    controller.spanishButtons_Labels();
                }
                controller.setPermissionLevel(1);
                controller.setWelcome("Employee: " + username);
            }else{
                System.out.println("Logging in Regular User. What??");
            }




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
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMainView.fxml");
        //patientMenuStart.patientMenuStartController controller = loader.getController();
        patientMain.patientMainController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

        //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }

    }

    //sets the current language to the given language
    public void setC_language(int i){
        c_language = i;
    }

    //Changes the buttons and labels to english
    public void englishButtons_Labels(){
        //Buttons
        logIn_Button.setText("Login");

        //Labels
        mainMenu_Button.setText("Main Menu");
        adminTitle_Label.setText("Admin Login");

        //text fields
        username_TextField.setPromptText("username");
        password_PasswordField.setPromptText("password");

    }

    //Changes the buttons and labels to spanish
    public void spanishButtons_Labels(){

        //Buttons
        logIn_Button.setText("Iniciar Sesión");

        //Labels
        mainMenu_Button.setText("Menu Principal");
        adminTitle_Label.setText("Administrador");

        //text fields
        username_TextField.setPromptText("usuario");
        password_PasswordField.setPromptText("contraseña");

    }



}
