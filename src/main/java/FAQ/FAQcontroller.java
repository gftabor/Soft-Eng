package FAQ;
import controllers.AbsController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;


/**
 * Created by Jackson on 4/29/2017.
 */
public class FAQcontroller extends controllers.AbsController{


    @FXML
    private Button main_menu_button;

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button emergency_button;

    @FXML
    private Label title_label;

    int c_language = 0;
    String loggedIn;
    private int permissionLevel;

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

    //set the logging admin
    public void setAdmin(String user){
        loggedIn = user;
    }

    //Gets the permissions
    public int getPermissionLevel() {
        return permissionLevel;
    }

    //Sets the permissions
    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
        System.out.println("Setting permission level to: " + permissionLevel);

    }

    // when the main menu button is clicked
    public void main_menu_clicked (){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        //patientMenuStart.patientMenuStartController controller = loader.getController();
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            controller.setWelcome(loggedIn);
            //set up spanish labels
        }
        controller.setPermissionLevel(permissionLevel);
        controller.loginOrOut(1,c_language);

    }

    // when the emergency button is clicked
    public void emergency_button_clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewEmergencyView.fxml");
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


}
