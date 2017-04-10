package database;

import DBController.DatabaseController;
import DBController.testDatabaseController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jasonashton on 4/2/17.
 */
public class testDatabaseSetup {

    //DatabaseController dbControl = new DatabaseController();
    //DatabaseController databaseController = DatabaseController.getInstance();
    testDatabaseController databaseController = testDatabaseController.getInstance();


    @Test
    public void checkSetup(){
        assertTrue(databaseController.startDB());
    }
}