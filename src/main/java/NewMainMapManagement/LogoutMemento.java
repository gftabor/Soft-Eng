package NewMainMapManagement;

import adminMenuStart.adminMenuStartController;
import javafx.fxml.FXMLLoader;

/**
 * Created by mylena on 4/28/17.
 */
public class LogoutMemento {
    private NewMainMapManagementController mmc = new NewMainMapManagementController();

    public LogoutMemento() {}

    public void autoLogout() {
        System.out.println("The system has automatically logged off due to inactivity.");

        //mmc.autoLogout();
    }
}
