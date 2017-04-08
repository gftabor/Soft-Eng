package database;

import DBController.testDatabaseGenerator;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

/**
 * Created by jasonashton on 4/8/17.
 */
public class testTestDB {
    testDatabaseGenerator dbGen = new testDatabaseGenerator();

    @Before
    public void setUp(){
        assertTrue(dbGen.connectDB());
    }
    @Test
    public void populateDB(){
        assertTrue(dbGen.populateDB());
    }

    @Test
    public void clearDB(){
        assertTrue(dbGen.clearDB());
    }
}
