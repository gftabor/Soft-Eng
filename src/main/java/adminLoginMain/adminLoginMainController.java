package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * Created by AugustoR on 4/1/17.
 */
public class adminLoginMainController extends AbsController{
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

    public void logInButton_Clicked(){

        System.out.println("The user has clicked the log in Button");
        System.out.println(username_TextField.getText());
        System.out.println(password_PasswordField.getText());
        if(password_PasswordField.getText().equals("1234") &&
                username_TextField.getText().equals("Griffin")) {
            System.out.println("correct Password");
        }



    }

    public void mainMenuButton_Clicked(){

        System.out.println("The user has clicked the main menu Button");
        switch_screen(backgroundAnchorPane, "../main/resources/views/patientMenuStartView.fxml");

    }




}
