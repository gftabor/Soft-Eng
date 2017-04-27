package NewMainMapManagement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Created by AugustoR on 4/27/17.
 */
public class NewMainMapManagementController {
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private Button directoryManagement_Button;

    @FXML
    private Button adminManagement_Button;

    @FXML
    private Button signOut_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Label LogInPerson_Label;

    @FXML
    private StackPane mapStack;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane node_Plane;

    @FXML
    private ImageView map_viewer;

    @FXML
    private Button zoomOut_button;

    @FXML
    private ChoiceBox<?> floor_ChoiceBox;

    @FXML
    private Button zoomIn_button;

    @FXML
    private Button previous_Button;

    @FXML
    private Button continueNew_Button;

    @FXML
    private Button clear_Button;

    @FXML
    private Button save_Button;

    int c_language=0;

    //Signs the user out
    public void signOutButton_Clicked(){
        /*FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
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
        //set permissions back
        controller.setPermissionLevel(0);
        //set label to empty
        controller.setWelcome("");*/
    }

    //Manage when the directory button is clicked
    public void DirectoryManButton_Clicked(){

    }

    //Manages when the admin management button is clicked
    public void AdminManButton_Clicked(){

    }

    //Manages when the emergency button is clicked
    public void emergencyButton_Clicked(){

    }

    //Manages when the previous button is clicked
    public void previousButton_Clicked(){

    }

    //Manages when the continue button is clicked
    public void continueNewButton_Clicked(){

    }

    //Manages when the user clicks the save button
    public void saveButton_Clicked(){

    }

    //Manages when the user clicks the clear button
    public void clearButton_Clicked(){

    }

    //Zooms the map in
    public void zoomInButton_Clicked() {

    }

    //Zooms out the map
    public void zoomOutButton_Clicked() {

    }

    //Labels for english
    public void englishButtons_Labels(){

    }
    //Labels for spanish
    public void spanishButtons_Labels(){

    }

    public void setUser(String user){
        LogInPerson_Label.setText(user);

    }


}
