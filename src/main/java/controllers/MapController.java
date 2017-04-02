package controllers;

import java.sql.*;
import java.sql.SQLException;

//import main.java.controllers.CollectionOfNodes;

/**
 * Created by griffincecil on 4/1/2017.
 */
public class MapController {

    private CollectionOfNodes collectionOfNodes;

    MapController () {
        collectionOfNodes = new CollectionOfNodes();
    }


    private void requestMapCopy(ResultSet nodeRset) {
        collectionOfNodes = new CollectionOfNodes();

        try {
            int x, y, floor;
            boolean hidden;
            String name;
            Node node;
            CollectionOfNodes collectionOfNodes = new CollectionOfNodes();
            while (nodeRset.next()) {
                x = nodeRset.getInt("XPOS");
                y = nodeRset.getInt("YPOS");
                hidden = nodeRset.getBoolean("HIDDEN?");
                name = nodeRset.getString("NAME");
                floor = nodeRset.getInt("FLOOR");
                node = new Node(x, y, hidden, true, name, floor);
                collectionOfNodes.addNode(node);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private int generateMap() {
        return 0;
    }

    private int drawMap() {
        return 0;
    }

    private int markNode() {
        return 0;
    }

    private int requestPath() {
        return 0;
    }

    private int updateInstructions() {
        return 0;
    }
}