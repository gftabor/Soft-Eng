package adminLoginMain;

/**
 * Created by Griffin on 4/1/2017.
 */
public class AdminLoginManager {
    private int loginAttempts;

    public int verifyCredentials(String username, String password) {
        if((username.equals("Griffin")) && (password.equals("1234"))){
            return 1;
        }
        return 0;
    }

    private int updateInstructions() {
        return 0;
    }

    public int checkAdminPowers() {
        return 0;
    }
}
