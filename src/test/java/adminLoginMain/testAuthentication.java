package adminLoginMain;

import DBController.DatabaseController;
import org.junit.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by jasonashton on 4/1/17.
 */
public class testAuthentication {
    static DatabaseController databaseController = DatabaseController.getInstance();

    @BeforeClass
    public static void setUp(){
        databaseController.setDbName("TestDB");
        databaseController.startDB();
    }

    @AfterClass
    public static void tearDown(){
        databaseController.closeDB();
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
}
