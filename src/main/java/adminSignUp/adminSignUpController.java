package adminSignUp;
import javafx.fxml.FXML;
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



    public void englishButtons_Labels(){

    }

    public void spanishButtons_Labels(){

    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

}
