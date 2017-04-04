package database;

import DBController.DatabaseController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;

import static org.junit.Assert.*;

/**
 * Created by jasonashton on 4/3/17.
 */
public class testNode {
    DatabaseController databaseController = DatabaseController.getInstance();
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
        testNull();
    }

    @Test
    public void testAddDelete(){
        assertTrue(databaseController.newNode(x, y, floor, ishidden, enabled, type, name, roomnum));
        ResultSet resultSet = databaseController.getNode(x, y, floor);
        try{
            resultSet.next();
            assertEquals(resultSet.getInt("XPOS"), x);
            assertEquals(resultSet.getInt("YPOS"), y);
            assertEquals(resultSet.getInt("FLOOR"), floor);
            assertEquals(resultSet.getBoolean("ISHIDDEN"), ishidden);
            assertEquals(resultSet.getBoolean("ENABLED"), enabled);
            assertEquals(resultSet.getString("TYPE"), type);
            assertEquals(resultSet.getString("NAME"), name);
            assertEquals(resultSet.getString("ROOMNUM"), roomnum);
            databaseController.closeResultSet(resultSet);
        } catch (SQLException e){
            e.printStackTrace();
        }
        assertTrue(databaseController.deleteNode(x, y, floor));
    }

    public void testNull(){
        ResultSet resultSet = databaseController.getNode(x, y, floor);
        try{
            resultSet.next();
            assertEquals(resultSet.getInt("XPOS"), 0);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @After
    public void tearDown(){
        testNull();
    }
}
