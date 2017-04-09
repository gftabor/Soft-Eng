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

    int c_language;

    public void logInButton_Clicked(){
        AdminLoginManager loginManage = new AdminLoginManager();
        String username = username_TextField.getText();
        String password = password_PasswordField.getText();

        System.out.println("The user has clicked the log in Button");
        System.out.println(username);
        System.out.println(password);

        if(loginManage.verifyCredentials(username, password) == 1){
            System.out.println("correct Password");

            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
            adminMenuStart.adminMenuStartController controller = loader.getController();
            //Set the correct username for the next scene
            controller.setUsername_Admin(username_TextField.getText());

            //Check is the username/passwords inputs are empty
        }else if(username.equals("")){
            invalidLogInputs.setText("Enter your username.");

        }else if(password.equals("")){
            invalidLogInputs.setText("Enter your password.");

        }else{//Incorrect inputs
            invalidLogInputs.setText("Incorrect username or password, try again.");

        }


    }

    public void mainMenuButton_Clicked(){
        System.out.println("The user has clicked the main menu Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
        patientMenuStart.patientMenuStartController controller = loader.getController();
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




}
