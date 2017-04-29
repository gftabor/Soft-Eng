package NewMainMapManagement;

import adminMenuStart.adminMenuStartController;
import javafx.fxml.FXMLLoader;

/**
 * Created by mylena on 4/28/17.
 */
public class LogoutMemento {
    private adminMenuStart.adminMenuStartController c = new adminMenuStartController();
    private NewMainMapManagementController mmc = new NewMainMapManagementController();

    public LogoutMemento() {}

    public void autoLogout() {
        System.out.println("The system has automatically logged off due to inactivity.");

        /*
        //Change to patient menu
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        */
        mmc.autoLogout();
    }
}
