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
public class testEdge {
    DatabaseController databaseController = DatabaseController.getInstance();
    int x1 = 0;
    int y1 = 0;
    int floor1 = 0;
    boolean ishidden1 = false;
    boolean enabled1 = true;
    String type1 = "TEST1";
    String name1 = "TEST1";
    String roomnum1 = "TEST1";
    int x2 = 1;
    int y2 = 1;
    int floor2 = 0;
    boolean ishidden2 = false;
    boolean enabled2 = true;
    String type2 = "TEST2";
    String name2 = "TEST2";
    String roomnum2 = "TEST2";

    @Before
    public void setUp() {
        databaseController.newNode(x1, y1, floor1, ishidden1, enabled1, type1, name1, roomnum1);
        databaseController.newNode(x2, y2, floor2, ishidden2, enabled2, type2, name2, roomnum2);
    }


    @Test
    public void addDeleteEdge(){
        databaseController.newEdge(x1, y1, floor1, x2, y2, floor2);
        databaseController.deleteEdge(x1, y1, floor1, x2, y2, floor2);
    }

    @After
    public void tearDown(){
        databaseController.deleteNode(x1, y1, floor1);
        databaseController.deleteNode(x2, y2, floor2);
    }

}
