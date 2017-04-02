package emergency;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;  //NOT JAVA SWING
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
/**
 * Created by AugustoR on 3/31/17.
 */
public class emergencyController extends AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label username_Label;

    @FXML
    private Button mainMenu_Button;

    //Return the the patient main menu
    public void mainMenuButton_Clicked() {
      System.out.println("The user clicked the main menu Button");
      //Switch screen
      FXMLLoader loader = switch_screen(backgroundAnchorPane, "../main/resources/views/patientMenuStartView.fxml");

    }

}
