package contollers;

import DBController.DatabaseController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.*;
import static org.junit.Assert.*;
import controllers.Node;

/**
 * Created by griffincecil on 4/6/2017.
 */

public class testNode {

    private Node testNode = new Node(10, 10, 4, false, true, "office", "wentworth", "401");

    @Test
    public void testToString() {

        assertEquals(testNode.toString(), "|Node wentworth: Pos X: 10 Pos y: 10  Hidden: false  Enabled: true|");

    }

    @Test
    public void testGetKey() {
        assertEquals(testNode.getKey(), 100010);
    }

}

