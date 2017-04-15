package database;

import DBController.DatabaseController;
import org.junit.*;

import java.sql.*;
import controllers.Node;

import static org.junit.Assert.*;

/**
 * Created by jasonashton on 4/3/17.
 */
public class testNode {
    static DatabaseController databaseController = DatabaseController.getInstance();
    int x = 0;
    int y = 0;
    int floor = 0;
    boolean ishidden = false;
    boolean enabled = true;
    String type = "TEST";
    String name = "TEST";
    String roomnum = "TEST";


    @BeforeClass
    public static void setUp(){
        databaseController.setDbName("./TestDb");
        databaseController.startDB();
    }

    @AfterClass
    public static void tearDown(){
        databaseController.closeDB();
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

    @Test
    public void testNull(){
        ResultSet resultSet = databaseController.getNode(x, y, floor);
        try{
            resultSet.next();
            assertEquals(resultSet.getInt("XPOS"), 0);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testKey(){
        Node exampleNode = new Node(150, 220, 4, false, true, "Restroom", "Bathroom 1", "417");
        int correctKey = 1500220;
        int keyGotten = exampleNode.getKey();
        System.out.println(keyGotten);
        assertEquals(correctKey, keyGotten);
    }
}
