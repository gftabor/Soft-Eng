package controllers;

import DBController.DatabaseController;
import org.apache.derby.iapi.types.Resetable;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;

import pathFindingMenu.Pathfinder;

//import main.java.controllers.CollectionOfNodes;

/**
 * Created by griffincecil on 4/1/2017.
 */
public class MapController {

    private static MapController mapController = new MapController();

    private int startNodeX;
    private int startNodeY;
    private int endNodeX;
    private int endNodeY;

    private MapController() {}

    public static MapController getInstance() {
        return mapController;
    }

    //internal representation of our node map
    private CollectionOfNodes collectionOfNodes;

    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    // Testing as public, change to private in final
    public void requestFloorMapCopy() {
        ResultSet nodeRset = databaseController.getTableSet("NODE");
    }

    //internal listing of edges (used only in startup full query of DB)
    private ArrayList<Edge> edgeCollection;

    //request full map info from the database (full reset of CollectionOfNodes)
    //  1. DB query
    //  2. instantiate objects of nodes & fill collectionOfNodes (internal representation)
    //  3. instantiate edges
    //  4. add edges to corresponding nodes in collectionOfNodes
    private void requestMapCopy() {
        ResultSet nodeRset = databaseController.getTableSet("NODE");
        ResultSet edgeRset = databaseController.getTableSet("EDGE");

        //wipes old verson of collection of nodess
        collectionOfNodes = new CollectionOfNodes();
        edgeCollection = new ArrayList<Edge>();

        try {
            int x, y, floor;
            boolean hidden;
            String name;
            Node node;
            while (nodeRset.next()) {
                x = nodeRset.getInt("XPOS");
                y = nodeRset.getInt("YPOS");
                hidden = nodeRset.getBoolean("HIDDEN?");
                name = nodeRset.getString("NAME");
                floor = nodeRset.getInt("FLOOR");
                node = new Node(x, y, hidden, true, name, floor);
                collectionOfNodes.addNode(node);
                System.out.println("MAPCONTROLLER: requestMapCopy(): Added a node from the rset to collection of nodes");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            int x1, y1, x2, y2, floor1, floor2;
            Edge myEdge;
            while (nodeRset.next()) {
                //get info from each query tuple
                x1 = edgeRset.getInt("XPOS1");
                y1 = edgeRset.getInt("YPOS1");
                x2 = edgeRset.getInt("XPOS2");
                y2 = edgeRset.getInt("YPOS2");
                floor1 = edgeRset.getInt("FLOOR1");
                floor2 = edgeRset.getInt("FLOOR2");

                //lookup node object pointer
                Node node1, node2;
                node1 = collectionOfNodes.getNode(x1, y1, floor1);
                node2 = collectionOfNodes.getNode(x2, y2, floor2);
                if (node1 == null || node2 == null) {
                    System.out.println("Node lookup unsuccessful");
                    continue;
                }

                myEdge = new Edge(node1, node2, floor1, floor2);
                edgeCollection.add(myEdge);

                //go through collection of edges and add them to their corresponding nodes
                for(Edge thisEdge:edgeCollection) {
                    thisEdge.getStartNode().addEdge(thisEdge);
                    thisEdge.getEndNode().addEdge(thisEdge);
                }

            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        

        databaseController.closeResultSet(nodeRset);
        databaseController.closeResultSet(edgeRset);
    }

    private int generateMap() {
        return 0;
    }

    private int drawMap() {
        return 0;
    }

    public int markNode(int x, int y, int type) {
        if(type == 1) {
            startNodeX = x;
            startNodeY = y;
            return 0;
        }
        else if(type == 2) {
            endNodeX = x;
            endNodeY = y;
            return 0;
        }
        else {
            return 1;
        }
    }

    public int requestPath() {
        Node startNode, endNode;

        if(collectionOfNodes == null) {
            requestMapCopy();
        }
        System.out.println("MAPCONTROLLER: requestPath: collectionOfNOdes size: " + collectionOfNodes.toString());

        startNode = collectionOfNodes.getNode(startNodeX, startNodeY, 4);
        if(startNode == null) {
            System.out.println("MAPCONTROLLER: getNode(startNode) returns null!");
            return 1;
        }

        endNode = collectionOfNodes.getNode(startNodeX, startNodeY, 4);
        if(endNode == null) {
            System.out.println("MAPCONTROLLER: getNode(endNode) returns null!");
            return 1;
        }

        Pathfinder pathfinder = new Pathfinder();
        pathfinder.generatePath(startNode, endNode);
        return 0;
    }

    private int updateInstructions() {
        return 0;
    }
}