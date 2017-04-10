package database;

import DBController.DatabaseController;
import DBController.testDatabaseController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by jasonashton on 4/2/17.
 */
public class testDatabaseClose {
    testDatabaseController databaseController = testDatabaseController.getInstance();

    @Before
    public void checkSetup(){
        assertTrue(databaseController.startDB());
    }

    @Test
    public void checkClose() { assertTrue(databaseController.closeDB());}
}
