package controllers;

import DBController.DatabaseController;
import pathFindingMenu.Pathfinder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.lang.Math;

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

    private int floorForNode1;
    private int floorForNode2;

    private MapController() {
        requestMapCopy();
    }

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

    public ArrayList<Edge> getEdgeCollection() {
        return edgeCollection;
    }

    public CollectionOfNodes getCollectionOfNodes() {
        return collectionOfNodes;
    }

    //request full map info from the database (full reset of CollectionOfNodes)
    //  1. DB query
    //  2. instantiate objects of nodes & fill collectionOfNodes (internal representation)
    //  3. instantiate edges
    //  4. add edges to corresponding nodes in collectionOfNodes
    public void requestMapCopy() {
        ResultSet nodeRset = databaseController.getTableSet("NODE");
        ResultSet edgeRset = databaseController.getTableSet("EDGE");

        //wipes old verson of collection of nodess
        collectionOfNodes = new CollectionOfNodes();
        edgeCollection = new ArrayList<Edge>();
        System.out.println("new");
        System.out.println("new map copy loading...");
        try {
            //instantiate all node objects and add to collection
            int x, y, floor;
            boolean hidden;
            boolean enabled;

            String name, type, roomnum;
            Node node;
            while (nodeRset.next()) {
                x = nodeRset.getInt("XPOS");
                y = nodeRset.getInt("YPOS");
                hidden = nodeRset.getBoolean("ISHIDDEN");
                enabled = nodeRset.getBoolean("ENABLED");
                name = nodeRset.getString("NAME");
                floor = nodeRset.getInt("FLOOR");
                type = nodeRset.getString("TYPE");
                roomnum = nodeRset.getString("ROOMNUM");
                node = new Node(x, y, floor, hidden, enabled, type, name, roomnum);
                collectionOfNodes.addNode(node);
                //System.out.println("MAPCONTROLLER: requestMapCopy(): Added a node from the rset to collection of nodes");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            //instantiate edges and add to corresponding nodes
            int x1, y1, x2, y2, floor1, floor2;
            Edge myEdge;
            while (edgeRset.next()) {
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

                //continue if can't find corresponding nodes
                if (node1 == null || node2 == null) {
                    System.out.println("Node lookup unsuccessful");
                    continue;
                }

                //add to arraylist to go through later
                edgeCollection.add(new Edge(node1, node2, floor1, floor2));
            }
                //go through collection of edges and add them to their corresponding nodes
                for(Edge thisEdge:edgeCollection) {
                    thisEdge.getStartNode().addEdge(thisEdge);
                    thisEdge.getEndNode().addEdge(thisEdge);
                }


        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //close the ResultSets
        databaseController.closeResultSet(nodeRset);
        databaseController.closeResultSet(edgeRset);
    }

    //Helper function to set nodes as start or end
    public int markNode(int x, int y, int type, int floor) {
        if(type == 1) {
            startNodeX = x;
            startNodeY = y;
            floorForNode1 = floor;
            return 0;
        }
        else if(type == 2) {
            endNodeX = x;
            endNodeY = y;
            floorForNode2 = floor;
            return 0;
        }
        else {
            return 1;
        }
    }

    //used for pathfinding
    //creates a pathfinder and runs pathfinding on the startnode and the end node.
    //  returns: 0 if success, 1 if error
    public ArrayList<Edge> requestPath() {
        Node startNode, endNode;

        //instantiates the collection if nothing is there yet
        if(collectionOfNodes == null) {
            requestMapCopy();
        }
        //System.out.println("MAPCONTROLLER: requestPath: collectionOfNOdes size: " + collectionOfNodes.toString());

        startNode = collectionOfNodes.getNode(startNodeX, startNodeY, floorForNode1);
        if(startNode == null) {
            System.out.println("MAPCONTROLLER: getNode(startNode) returns null!");
            //System.out.println("MAPCONTROLLER: requestPath: collectionOfNOdes size: " + collectionOfNodes.toString());
            return null;
        }

        endNode = collectionOfNodes.getNode(endNodeX, endNodeY, floorForNode2);
        if(endNode == null) {
            System.out.println("MAPCONTROLLER: getNode(endNode) returns null!");
            return null;
        }

        //creates and runs a pathfinder
        Pathfinder pathfinder = new Pathfinder();
        pathfinder.generatePath(startNode, endNode);
        return pathfinder.getPath();

    }

    public boolean areDifferentFloors() {
        return floorForNode1 != floorForNode2;
    }

    public int returnOriginalFloor() {
        return floorForNode1;
    }

    //in progress -> prints directions until I can figure out how to get it on the UI
    public String getTextDirections(ArrayList<Edge> path) {
        String destination;
        ArrayList<String> directions = new ArrayList<>();

        for(int i = path.size()-1; i > 0; i--) {
            double angle = getAngle(path.get(i), path.get(i-1));
            if(angle > -135.0 && angle <= -45.0) {
                destination = path.get(i).getEndNode().getRoomNum();
                directions.add("Turn left at " + destination);
            }
            else if(angle >= 45.0 && angle < 135.0) {
                destination = path.get(i).getEndNode().getRoomNum();
                directions.add("Turn right at " + destination);
            }
            else if(angle > 10.0 && angle < 45.0) {
                destination = path.get(i).getEndNode().getRoomNum();
                directions.add("Make a slight right at " + destination);
            }
            else if(angle >= -10.0 && angle <= 10.0){
                directions.add("Continue straight.");
            }
            else if(angle > -45.0 && angle < -10.0) {
                destination = path.get(i).getEndNode().getRoomNum();
                directions.add("Make a slight left at " + destination);
            }
            else if(angle > 135.0 && angle < 180.0) {
                destination = path.get(i).getEndNode().getRoomNum();
                directions.add("Make a hard right at " + destination);
            }
            else if(angle > -180.0 && angle < -135.0) {
                destination = path.get(i).getEndNode().getRoomNum();
                directions.add("Make a hard left at " + destination);
            }else{
                directions.add("nothing");
            }

        }
        directions = cleanDirections(directions);
        return concatenateDirections(directions);

    }

    private ArrayList<String> cleanDirections(ArrayList<String> direc) {
        ArrayList<String> directions = direc;
        for(int i = directions.size()-1; i > 0; i--) {
            if("Continue straight.".equals(directions.get(i)) && directions.get(i).equals(directions.get(i-1))) {
                directions.remove(directions.get(i));
            }
        }
        return directions;
    }

    private String concatenateDirections(ArrayList<String> directions) {
        String text = "";
        for(String s: directions) {
            text = text + s + "\n";
        }
        return text;
    }

    private double getAngle(Edge e1, Edge e2) {
        Node middle;
        double e1X = 0.0;
        double e1Y = 0.0;
        double e2X = 0.0;
        double e2Y = 0.0;
        if(e1.getEndNode() == e2.getStartNode()) {
            middle = e1.getEndNode();

        } else if(e1.getEndNode() == e2.getEndNode()) {
            middle = e1.getEndNode();

        } else if(e1.getStartNode() == e2.getEndNode()) {
            middle = e1.getStartNode();

        } else {
            middle = e1.getStartNode();
        }
        e1X = middle.getPosX() - e1.getNeighbor(middle).getPosX();
        e1Y = middle.getPosY() - e1.getNeighbor(middle).getPosY();

        e2X =  e2.getNeighbor(middle).getPosX() - middle.getPosX();
        e2Y =  e2.getNeighbor(middle).getPosY() - middle.getPosY();

        double angle = Math.toDegrees(Math.atan2(e1X*e2Y - e1Y*e2X, e1X*e2X + e1Y*e2Y));
        System.out.println(angle);
        return angle;
    }

}