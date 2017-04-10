package database;

import DBController.DatabaseController;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by jasonashton on 4/2/17.
 */
public class testDatabaseClose {
    static DatabaseController databaseController = DatabaseController.getInstance();

    @BeforeClass
    public static void setUp(){
        databaseController.setDbName("TestDB");
        databaseController.startDB();
    }

    @Test
    public void checkClose() { assertTrue(databaseController.closeDB());}
}
