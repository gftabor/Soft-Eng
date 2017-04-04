package database;

import DBController.DatabaseController;
import org.junit.*;

import java.sql.*;
import static org.junit.Assert.*;

/**
 * Created by jasonashton on 4/3/17.
 * This test is definitely broken
 */
public class testProfessional {
    DatabaseController databaseController = DatabaseController.getInstance();
    String ID = "TEST";
    String firstName = "TEST";
    String lastName = "TEST";
    int x = 0;
    int y = 0;
    int floor = 0;
    boolean ishidden = false;
    boolean enabled = true;
    String type = "TEST";
    String name = "TEST";
    String roomnum = "TEST";

    public void setUp(){

        assertTrue(databaseController.newNode(x, y, floor, ishidden, enabled, type, name, roomnum));
    }

    @Test
    public void testAdd(){
        assertTrue(databaseController.newProfessional(ID, x, y, floor, firstName, lastName, type));
    }

    @Test
    public void wasAdded(){
        ResultSet resultSet = databaseController.getProfessional(ID);
        try{
            resultSet.next();
            assertEquals(resultSet.getString("ID"), ID);
            assertEquals(resultSet.getInt("XPOS"), x);
            assertEquals(resultSet.getInt("YPOS"), y);
            assertEquals(resultSet.getInt("FLOOR"), floor);
            assertEquals(resultSet.getString("FIRSTNAME"), firstName);
            assertEquals(resultSet.getString("LASTNAME"), lastName);
            assertEquals(resultSet.getString("TYPE"), type);
            databaseController.closeResultSet(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete(){
        assertTrue(databaseController.deleteProfessional(ID));
    }

    public void tearDown(){
        assertTrue(databaseController.deleteNode(x, y, floor));
    }
}
