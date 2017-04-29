package adminMenuStart;

import NewMainMapManagement.LogoutMemento;
import NewMainMapManagement.NewMainMapManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


/**
 * Created by mylena on 4/27/17.
 */
public class AutoLogout {

    public AutoLogout(){}

    //public void autoLogout() { m.autoLogout();}

    public void checkActivity() throws InterruptedException {

        Caretaker c = new Caretaker();
        LogoutMemento lm = new LogoutMemento();

        synchronized (c) {
            do {
                    c.wait(10);
            } while(c.isM());

            lm.autoLogout();
        }
    }


}
