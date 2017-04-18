package controllers;

import DBController.DatabaseController;
import pathFindingMenu.Pathfinder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;

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


        //edgeListToText(pathfinder.getPath());
        nodeListToText(pathfinder.getNodePath());
        try {
            Runtime.getRuntime().exec(new String[] { "pathfinder3D.exe"});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pathfinder.getPath();

    }


    public boolean edgeListToText(ArrayList<Edge> edges) {

        try {
            List<String> lines = new ArrayList();

            for(Edge edge : edges) {
                String line = "";
                line += edge.getStartNode().getPosX();
                line += ",";
                line += edge.getStartNode().getPosY();
                line += ",";
                line += edge.getStartNode().getFloor();

                lines.add(line);
            }

            String line = "";
            Edge finalEdge = edges.get(edges.size()-1);
            line += finalEdge.getEndNode().getPosX();
            line += ",";
            line += finalEdge.getEndNode().getPosY();
            line += ",";
            line += finalEdge.getEndNode().getFloor();

            lines.add(line);

            Path file = Paths.get("path.txt");
            Files.write(file, lines, Charset.forName("UTF-8"));
            //Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean nodeListToText(ArrayList<Node> nodes) {

        try {
            List<String> lines = new ArrayList();

            /*
            for(Node node : nodes) {
                String line = "";
                line += node.getPosX();
                line += ",";
                line += node.getPosY();
                line += ",";
                line += node.getFloor();

                lines.add(line);
            }
            */

            for(int i = nodes.size()-1; i >= 0; i--) {String line = "";
                line += nodes.get(i).getPosX();
                line += ",";
                line += nodes.get(i).getPosY();
                line += ",";
                line += nodes.get(i).getFloor();

                lines.add(line);
            }
            

            /*
            String line = "";
            Node finalNode = nodes.get(nodes.size()-1);
            line += finalNode.getPosX();
            line += ",";
            line += finalNode.getPosY();
            line += ",";
            line += finalNode.getFloor();

            lines.add(line);
             */

            Path file = Paths.get("path.txt");
            Files.write(file, lines, Charset.forName("UTF-8"));
            //Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    //used for multifloor pathfinding output
    //utilizes requestPath() to get pathfinding path from start to end
    //breaks up into individual path for each edge
    public ArrayList<Edge>[] requestFragmentedPath(ArrayList<Edge> fullList, int startingFloor, int endingFloor) {
        //fullList is given specifically from requestPath()
        //path is in reverse order!!!
        Collections.reverse(fullList);


        //initialize fragmented list
        ArrayList<Edge> [] fragmentedList = new ArrayList [10];
        //put null references to avoid index out of bounds errors
        for (int i = 0; i <= 8; i++) {
            fragmentedList[i] = null;
        }

        int currentFloor = startingFloor;
        ArrayList<Edge> currentlist = new ArrayList<>();

        for (Edge e: fullList) {
            System.out.println("Edge (floor) from " + e.getStartNode().getFloor()+ " to" + e.getEndNode().getFloor());


            //if change in floor, close off this list of edges
            if (e.getEndNode().getFloor() != e.getStartNode().getFloor()) {
                //add old list to the fragList - if it is not empty
                if (!currentlist.isEmpty()) {

                    System.out.println("created frag path on floor: " + Integer.toString(currentFloor));
                    fragmentedList[currentFloor] = currentlist;

                }

                //instantiate new version of currentlist
                currentlist = new ArrayList<>();

                //set new currentfloor
                if (e.getStartNode().getFloor() == currentFloor) {
                    currentFloor = e.getEndNode().getFloor();
                } else {
                    currentFloor = e.getStartNode().getFloor();
                }
                System.out.println("currentfloor updated to: " + Integer.toString(currentFloor));

                //don't add a transition edge unless you are on the currentfloor or the ending floor
                if (currentFloor != startingFloor && currentFloor != endingFloor) {
                    continue;
                }
            }

            //add the edge to the currentlist
            currentlist.add(e);
        }

        //add the final list to the fraglist
        System.out.println("finished loop created frag path on floor: " + Integer.toString(currentFloor));
        fragmentedList[currentFloor] = currentlist;

        return fragmentedList;
    }

    //returns true if the floors of the two nodes in the pathfinding are different
    public boolean areDifferentFloors() {
        return floorForNode1 != floorForNode2;
    }

    //returns the floor of the node at the start of the pathfinding
    public int returnOriginalFloor() {
        return floorForNode1;
    }

    public int returnDestFloor() {
        return floorForNode2;
    }

    //returns true if the multifloor pathfinding is going up
    //returns false if the pathfinding is going down floors
    public boolean goingUp() {
        return floorForNode2 > floorForNode1;
    }

    //in progress -> prints directions until I can figure out how to get it on the UI
    public String getTextDirections(ArrayList<Edge> path) {
        String destination;
        ArrayList<String> directions = new ArrayList<>();
        if(path.isEmpty())
            return null;
        for(int i = path.size()-1; i > 0; i--) {
            double angle = getAngle(path.get(i), path.get(i-1));
            if(path.get(i).getStartNode().getFloor() != path.get(i).getEndNode().getFloor() ||
                    path.get(i-1).getStartNode().getFloor() != path.get(i-1).getEndNode().getFloor()) {
                directions.add("Change Floors ");
                continue;
            }
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
        if(path.get(0).getStartNode().getFloor() != path.get(0).getEndNode().getFloor())
            directions.add("Change Floors ");
        directions.add("Reached Destination");
        directions = cleanDirections(directions);
        return concatenateDirections(directions);

    }

    private ArrayList<String> cleanDirections(ArrayList<String> direc) {
        ArrayList<String> directions = direc;
        for(int i = directions.size()-1; i > 0; i--) {
            if("Continue straight.".equals(directions.get(i)) && directions.get(i).equals(directions.get(i-1))) {
                directions.remove(directions.get(i));
            }
            if("Change Floors ".equals(directions.get(i)) && directions.get(i).equals(directions.get(i-1))) {
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