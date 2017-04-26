package database;

import DBController.DatabaseController;
import org.junit.*;

import java.sql.*;
import static org.junit.Assert.*;

/**
 * Created by jasonashton on 4/3/17.
 */
public class testProfessional {
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
    static String roomnum = "TEST";
    static String department = "TEST";
    static int permissionLevel = 0;


    @BeforeClass
    public static void setUp(){
        databaseController.setDbName("./TestDb");
        databaseController.startDB();
        assertTrue(databaseController.newNode(x, y, floor, ishidden, enabled, type, name, roomnum, permissionLevel));
    }

    @AfterClass
    public static void tearDown(){
        databaseController.closeDB();
    }

    /*
    @Test
    public void testAddDelete(){
        //add professional
        assertTrue(databaseController.newProfessional(firstName, lastName, type, department));

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
    */

    @Test
    public void testNull() {
        ResultSet resultSet = databaseController.getProfessional(firstName, lastName, type);
        try {
            resultSet.next();
            assertEquals(resultSet.getString("ID"), 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
