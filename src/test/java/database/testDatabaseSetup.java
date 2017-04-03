package database;

import DBController.DatabaseController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jasonashton on 4/2/17.
 */
public class testDatabaseSetup {

    DatabaseController dbControl = new DatabaseController();

    @Test
    public void checkSetup(){
        assertTrue(dbControl.startDB());
    }
}