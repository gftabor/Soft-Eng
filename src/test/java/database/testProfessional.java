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
    int ID;
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

    @Before
    public void setUp(){
        //testNull();
        assertTrue(databaseController.newNode(x, y, floor, ishidden, enabled, type, name, roomnum));
    }

    @Test
    public void testAddDelete(){
        //add professional
        assertTrue(databaseController.newProfessional(firstName, lastName, type));

        //make sure it is there
        ResultSet resultSet = databaseController.getProfessional(firstName, lastName, type);
        try{
            resultSet.next();
            ID = resultSet.getInt("ID");
            assertEquals(resultSet.getString("FIRSTNAME"), firstName);
            assertEquals(resultSet.getString("LASTNAME"), lastName);
            assertEquals(resultSet.getString("TYPE"), type);
            databaseController.closeResultSet(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
        }

        //add a location
        assertTrue(databaseController.newProfessionalLocation(ID, x, y, floor));

        assertTrue(databaseController.deleteProfessionalLocation(ID, x, y, floor));

        assertTrue(databaseController.deleteProfessional(firstName, lastName, type));
    }

    @Test
    public void testNull(){
        ResultSet resultSet = databaseController.getProfessional(firstName, lastName, type);
        try{
            resultSet.next();
            assertEquals(resultSet.getString("ID"), 0);
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
