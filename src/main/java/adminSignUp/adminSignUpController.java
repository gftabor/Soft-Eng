package adminSignUp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import controllers.AbsController;

/**
 * Created by AugustoR on 4/17/17.
 */
public class adminSignUpController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label adminTitle_Label;

    @FXML
    private Label currentAdmin_Label;

    @FXML
    private Label invalidLogInputs;

    @FXML
    private TextField username_TextField;

    @FXML
    private PasswordField password_PasswordField;

    @FXML
    private Button addNAdmin_Button;

    @FXML
    private Button MainMenu_Button;

    int c_language = 0; //English by default


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

    }

    //Sets the english labels
    public void englishButtons_Labels(){
        //Buttons
        addNAdmin_Button.setText("Add Administrator");
        MainMenu_Button.setText("Main Menu");

        //Labels
        adminTitle_Label.setText("Admin Sign Up By:");


        //TextField
        username_TextField.setPromptText("username");
        password_PasswordField.setPromptText("password");

    }

    //sets the spanish labels
    public void spanishButtons_Labels(){
        //Buttons
        addNAdmin_Button.setText("Agregar Administrador");
        MainMenu_Button.setText("Menu Principal");

        //Labels
        adminTitle_Label.setText("Registrando Administrador");


        //TextField
        username_TextField.setPromptText("usuario");
        password_PasswordField.setPromptText("contrasena");

    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

}
