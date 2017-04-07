package database;

import DBController.DatabaseController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by MZ on 4/6/17.
 */
public class testService {

    DatabaseController databaseController = DatabaseController.getInstance();
    int ID;
    String firstName = "TEST";
    String lastName = "TEST";
    int x = 20;
    int y = 20;
    int floor = 0;
    boolean ishidden = false;
    boolean enabled = true;
    String type = "TEST";
    String name = "TEST";
    String roomnum = "TEST";

    @Before
    public void setUp(){
        assertTrue(databaseController.newNode(x, y, floor, ishidden, enabled, type, name, roomnum));
    }

    @Test
    public void testAddDelete(){
        //add professional
        assertTrue(databaseController.newService(name, type, x, y, floor));

        //make sure it is there
        ResultSet resultSet = databaseController.getService(name, type, x, y, floor);
        try{
            resultSet.next();
            ID = resultSet.getInt("ID");
            assertEquals(resultSet.getString("NAME"), name);
            assertEquals(resultSet.getString("TYPE"), type);
            assertEquals(resultSet.getInt("XPOS"), x);
            assertEquals(resultSet.getInt("YPOS"), y);
            assertEquals(resultSet.getInt("FLOOR"), floor);
            databaseController.closeResultSet(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
        }

        assertTrue(databaseController.deleteService(name, type, x, y, floor));
    }

    @Test
    public void testNull(){
        ResultSet resultSet = databaseController.getService(name, type, x, y, floor);
        try{
            resultSet.next();
            assertEquals(resultSet.getInt("ID"), 0);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        assertTrue(databaseController.deleteNode(x, y, floor));
        //testNull();
    }

}
