package adminLoginMain;

import DBController.DatabaseController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import adminLoginMain.AdminLoginManager;

/**
 * Created by jasonashton on 4/1/17.
 */
public class testAuthentication {
    DatabaseController databaseController = DatabaseController.getInstance();

    @Before
    public void setUp(){
        databaseController.startDB();
    }

    @Test
    public void testAdminLoginCorrect(){
        AdminLoginManager loginManage = new AdminLoginManager();
        assertEquals(loginManage.verifyCredentials("Griffin", "1234"), 1);
    }

    @Test
    public void testAdminLoginWrongUsername(){
        AdminLoginManager loginManage = new AdminLoginManager();
        assertEquals(loginManage.verifyCredentials("XXX", "1234"), 0);
    }

    @Test
    public void testAdminLoginWrongPassword(){
        AdminLoginManager loginManage = new AdminLoginManager();
        assertEquals(loginManage.verifyCredentials("Griffin", "0000"), 0);
    }

    @After
    public void tearDown(){
        databaseController.closeDB();
    }
}
