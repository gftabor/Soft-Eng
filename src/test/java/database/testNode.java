package database;

import DBController.DatabaseController;
import org.junit.Test;
import java.sql.*;

import static org.junit.Assert.*;

/**
 * Created by jasonashton on 4/3/17.
 */
public class testNode {
    DatabaseController databaseController = DatabaseController.getInstance();
    int x, y, floor = 0;
    boolean ishidden = false;
    boolean enabled = true;
    String type, name, roomnum = "None";

    @Test
    public void testAdd(){
        assertTrue(databaseController.newNode(x, y, floor, ishidden, enabled, type, name, roomnum));
    }

    @Test
    public void wasAdded(){
        ResultSet resultSet = databaseController.getNode(x, y, floor);
        try{
            resultSet.first();
            assertEquals(resultSet.getInt("XPOS"), x);
            assertEquals(resultSet.getInt("YPOS"), y);
            assertEquals(resultSet.getInt("FLOOR"), floor);
            assertEquals(resultSet.getBoolean("ISHIDDEN"), ishidden);
            assertEquals(resultSet.getBoolean("ENABLED"), enabled);
            assertEquals(resultSet.getString("TYPE"), type);
            assertEquals(resultSet.getString("NAME"), name);
            assertEquals(resultSet.getString("ROOMNUM"), roomnum);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete(){
        assertTrue(databaseController.deleteNode(x, y, floor));
    }

}
