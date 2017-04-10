package database;

import DBController.testDatabaseController;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by jasonashton on 4/8/17.
 */
public class testTestDB {

    testDatabaseController testDBController = testDatabaseController.getInstance();

    @Before
    public void startDB(){
        assertTrue(testDBController.startDB());
    }

    @Test
    public void populateThenClearDB(){
        assertTrue(testDBController.populateDB());
        assertTrue(testDBController.clearDB());
    }
}
