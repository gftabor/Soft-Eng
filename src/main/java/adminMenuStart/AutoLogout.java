package adminMenuStart;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


/**
 * Created by mylena on 4/27/17.
 */
public class AutoLogout {

    public AutoLogout(){}

    public void autoLogout() {
        adminMenuStartController c = new adminMenuStartController();
        c.autoLogout();
    }

    public void checkActivity() throws InterruptedException {

        Caretaker c = new Caretaker();

        synchronized (c) {
            c.wait(10);
            if(c.isM() == false) {
                autoLogout();
            }
        }

    }
}
