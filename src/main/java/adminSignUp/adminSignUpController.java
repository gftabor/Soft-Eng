package adminSignUp;
import DBController.DatabaseController;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

/**
 * Created by AugustoR on 4/17/17.
 */
public class adminSignUpController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label currentAdmin_Label;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private Button MainMenu_Button;

    @FXML
    private TableView<?> Table_TableView;

    @FXML
    private TableColumn<Table, String> ID_TableColumn;

    @FXML
    private TableColumn<?, ?> username_TableColumn;

    @FXML
    private TableColumn<?, ?> firstName_TableColumn;

    @FXML
    private TableColumn<?, ?> lastName_TableColumn;

    @FXML
    private TableColumn<?, ?> password_TableColumn;

    @FXML
    private Label queryStatus;

    @FXML
    private Label subTitle_Label;

    @FXML
    private TextField search_textField;

    @FXML
    private Label error_LabelText;

    @FXML
    private Label Mode_Label;

    @FXML
    private ChoiceBox<?> mode_ChoiceBox;

    @FXML
    private HBox ID_Label;

    @FXML
    private Label username_Label;

    @FXML
    private TextField userName_TextField;

    @FXML
    private HBox firstName_Label;

    @FXML
    private TextField firstName_TextField;

    @FXML
    private Label lastName_Label;

    @FXML
    private TextField lastName_TextField;

    @FXML
    private Label password_Label;

    @FXML
    private TextField newPassword_TextField;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    int c_language = 0; //English by default

    DatabaseController databaseController = DatabaseController.getInstance();


    //Sends the admin back to the main menu
    public void mainMenuButton_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(currentAdmin_Label.getText());
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

    }

    //Set the username coming from a scene
    public void setUsername(String user){
        currentAdmin_Label.setText(user);
    }

    //adds the admin into the database
    public void addAdminButton_Clicked(){
        try {
            if (databaseController.newAdmin(firstName_TextField.getText(), lastName_TextField.getText(),
                    userName_TextField.getText(), newPassword_TextField.getText())) {
                queryStatus.setText("Admin Added");
            } else {
                queryStatus.setText("Error Adding Admin");
            }
        }
        catch(Exception e){
            queryStatus.setText("ERROR: Exception");
            e.printStackTrace();
        }
    }

    //edits admin
    //FIXME: GET THE ID FROM THE CHART SELECTION


    public void editAdminButton_Clicked(){
        try {
            if (databaseController.editAdmin(999, firstName_TextField.getText(), lastName_TextField.getText(),
                    userName_TextField.getText(), newPassword_TextField.getText())) {
                queryStatus.setText("Admin Edited");
            } else {
                queryStatus.setText("Error Edit Admin");
            }
        }
        catch(Exception e){
            queryStatus.setText("ERROR: Exception");
            e.printStackTrace();
        }
    }

    //deletes admin from database
    //FIXME: GET THE ID FROM THE CHART SELECTION
    public void deleteAdminButton_Clicked(){
        try {
            if (databaseController.deleteAdmin(999)){
                queryStatus.setText("Admin Deleted");
            } else {
                queryStatus.setText("Error Deleting Admin");
            }
        }
        catch(Exception e){
            queryStatus.setText("ERROR: Exception");
            e.printStackTrace();

         }
    }


    /*
    //Sets the english labels
    public void englishButtons_Labels(){
        //Buttons
        addNAdmin_Button.setText("Add Administrator");
        MainMenu_Button.setText("Main Menu");


        //TextField
        username_TextField.setPromptText("username");
        password_PasswordField.setPromptText("password");

    }

    //sets the spanish labels
    public void spanishButtons_Labels(){
        //Buttons
        addNAdmin_Button.setText("Agregar Administrador");
        MainMenu_Button.setText("Menu Principal");


        //TextField
        username_TextField.setPromptText("usuario");
        password_PasswordField.setPromptText("contrasena");

    }
    */

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

}
