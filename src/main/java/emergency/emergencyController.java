package emergency;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URISyntaxException;

/**
 * Created by AugustoR on 3/31/17.
 */
public class emergencyController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label username_Label;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button call_authorities;

    @FXML
    private Label call_status;

    @FXML
    private Label start_Label;

    @FXML
    private Label exit_Label;

    //English by default
    int c_language = 0;

    //Return the the patient main menu
    public void mainMenuButton_Clicked() {
        System.out.println("The user clicked the main menu Button");
        //Switch screen
        //Change to patient menu
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if (c_language == 0) {
            controller.englishButtons_Labels();

            //set up spanish labels
        } else if (c_language == 1) {
            controller.spanishButtons_Labels();
        }

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

        //Change the labels
        start_Label.setText("You are here");
        exit_Label.setText("Exits");


    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        mainMenu_Button.setText("Menú Principal");
        call_authorities.setText("Llamar Autoridades");

        //change the Labels
        start_Label.setText("Tú estás aquí");
        exit_Label.setText("Salidas");


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

}
