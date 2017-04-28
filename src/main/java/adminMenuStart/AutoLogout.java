package adminMenuStart;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


/**
 * Created by mylena on 4/27/17.
 */
public class AutoLogout {
    private adminMenuStartController c = new adminMenuStartController();

    public AutoLogout(){}

    public void autoLogout() {
        c.autoLogout();
    }

    public void checkActivity() throws InterruptedException {

        Caretaker c = new Caretaker();

        synchronized (c) {
            do {
                c.wait(10);
            } while(c.isM() == true);
            autoLogout();
        }

    }


}
