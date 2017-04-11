package database;

import DBController.DatabaseController;
import org.junit.*;

/**
 * Created by jasonashton on 4/3/17.
 */
public class testEdge {
    //testDatabaseController databaseController = testDatabaseController.getInstance();
    static DatabaseController databaseController = DatabaseController.getInstance();
    static int x1 = 0;
    static int y1 = 0;
    static int floor1 = 0;
    static boolean ishidden1 = false;
    static boolean enabled1 = true;
    static String type1 = "TEST1";
    static String name1 = "TEST1";
    static String roomnum1 = "TEST1";
    static int x2 = 1;
    static int y2 = 1;
    static int floor2 = 0;
    static boolean ishidden2 = false;
    static boolean enabled2 = true;
    static String type2 = "TEST2";
    static String name2 = "TEST2";
    static String roomnum2 = "TEST2";


    @BeforeClass
    public static void setUp(){
        databaseController.setDbName("./TestDb");
        databaseController.startDB();
        databaseController.newNode(x1, y1, floor1, ishidden1, enabled1, type1, name1, roomnum1);
        databaseController.newNode(x2, y2, floor2, ishidden2, enabled2, type2, name2, roomnum2);
    }

    @AfterClass
    public static void tearDown(){
        databaseController.deleteNode(x1, y1, floor1);
        databaseController.deleteNode(x2, y2, floor2);
        databaseController.closeDB();
    }


    @Test
    public void addDeleteEdge(){
        databaseController.newEdge(x1, y1, floor1, x2, y2, floor2);
        databaseController.deleteEdge(x1, y1, floor1, x2, y2, floor2);
    }

}
