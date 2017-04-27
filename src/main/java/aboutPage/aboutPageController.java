package aboutPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class aboutPageController extends controllers.AbsController{

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button main_Menu;

    @FXML
    void mainMenu_Clicked() {
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        patientMain.patientMainController controller = loader.getController();

    }

}
