package NewMainMapManagement;


/**
 * Created by mylena on 4/27/17.
 */
public class AutoLogout {

    public AutoLogout(){}


    public void checkActivity() throws InterruptedException {

        Caretaker c = new Caretaker();
        LogoutMemento lm = new LogoutMemento();
        NewMainMapManagementController mmc = new NewMainMapManagementController();

        synchronized (c) {
            do{
                c.wait(10000);
            } while(c.isM());
            //mmc.autoLogout();
        }

    }


}
