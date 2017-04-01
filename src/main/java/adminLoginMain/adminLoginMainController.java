package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Created by AugustoR on 4/1/17.
 */
public class adminLoginMainController {
    @FXML
    private TextField username_TextField;

    @FXML
    private PasswordField password_PasswordField;

    @FXML
    private Button logIn_Button;

    @FXML
    private Button mainMenu_Button;

    public void logInButton_Clicked(){
        System.out.println("The user has clicked the log in Button");
    }

    public void mainMenuButton_Clicked(){

        System.out.println("The user has clicked the main menu Button");
    }




}
