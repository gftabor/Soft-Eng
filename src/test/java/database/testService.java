package database;

import DBController.DatabaseController;
import org.junit.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by MZ on 4/6/17.
 */
public class testService {

    static DatabaseController databaseController = DatabaseController.getInstance();
    static int ID;
    static String firstName = "TEST";
    static String lastName = "TEST";
    static int x = 0;
    static int y = 0;
    static int floor = 0;
    static boolean ishidden = false;
    static boolean enabled = true;
    static String type = "TEST";
    static String name = "TEST";
    static String roomnum = "TEST2";


    @BeforeClass
    public static void setUp(){
        databaseController.setDbName("./TestDb");
        databaseController.startDB();
        //
    }

    @AfterClass
    public static void tearDown(){
        assertTrue(databaseController.deleteNode(x, y, floor));
        databaseController.closeDB();
    }


    @Test
    public void testAddDelete(){
        assertTrue(databaseController.newNode(134, 2345, 7, ishidden, enabled, type, name, "TEST555"));
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

}
