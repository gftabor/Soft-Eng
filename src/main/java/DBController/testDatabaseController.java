package DBController;


/**
 * Created by jasonashton on 4/10/17.
 */
public class testDatabaseController extends DatabaseController{

    private static testDatabaseController testDBController = new testDatabaseController();
    public static testDatabaseController getInstance() { return testDBController; }

    public testDatabaseController() {
        this.dbName = "testDB";
    }
}
