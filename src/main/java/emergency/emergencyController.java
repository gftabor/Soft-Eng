package emergency;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.net.URISyntaxException;

/**
 * Created by AugustoR on 3/31/17.
 */
public class emergencyController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label title_Label;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button call_authorities;

    @FXML
    private Label call_status;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane pane;

    @FXML
    private ImageView map_viewer;

    //English by default
    int c_language = 0;

    @FXML
    public void initialize() {
        map_viewer.setImage(new Image("/images/4emergency.png"));
        map_viewer.fitHeightProperty().bind(pane.heightProperty());
        map_viewer.fitWidthProperty().bind(pane.widthProperty());

    }

    //Return the the patient main menu
    public void mainMenuButton_Clicked() {
        //Switch screen
        //Change to patient menu
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        controller.setLanguage_ChoiceBox(c_language);
        //set up english labels
        if (c_language == 0) {
            controller.englishButtons_Labels();
            //set up spanish labels
        } else if (c_language == 1) {
            controller.spanishButtons_Labels();
        }

        controller.setPermissionLevel(0);
        

    }

    public void callAuthorities(){
        SmsSender mySMS = new SmsSender();
        try {
            if(mySMS.sendSMS().equals("queued")){
                call_status.setText("Authorities notified.");
            }else{
                call_status.setText("Failed to send. Call 911.");
            }
        } catch (URISyntaxException e){
            e.getReason();
        }
    }

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        //change the current language to english
        c_language = 0;
        //Change the Buttons
        mainMenu_Button.setText("Main Menu");
        call_authorities.setText("Call Authorities");




    }

    //switches all the labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        mainMenu_Button.setText("Menu Principal");
        call_authorities.setText("Llamar Autoridades");
        


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

}
