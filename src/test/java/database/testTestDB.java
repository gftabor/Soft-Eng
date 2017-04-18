package database;

import DBController.DatabaseController;
import org.junit.Test;

/**
 * Created by jasonashton on 4/8/17.
 */
public class testTestDB {

    DatabaseController dbController = DatabaseController.getInstance();

    @Test
    public void populateThenClearDB(){
        dbController.setDbName("./TestDb");
        dbController.startDB();
        //assertTrue(dbController.clearDB());
        //assertTrue(dbController.populateDB());
        dbController.closeDB();
    }
}
