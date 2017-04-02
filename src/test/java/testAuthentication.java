import org.junit.Test;
import static org.junit.Assert.assertEquals;
import adminLoginMain.AdminLoginManager;

/**
 * Created by jasonashton on 4/1/17.
 */
public class testAuthentication {

    @Test
    public void testAdminLogin(){
        AdminLoginManager loginManage = new AdminLoginManager();
        assertEquals(loginManage.verifyCredentials("Griffin", "1234"), 1);
    }

}
