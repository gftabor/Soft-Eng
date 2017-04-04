package controllers;


import java.util.*;

/**
 * Created by dgian on 4/1/2017.
 */
//This is the internal representation of the map (grouping of node objects)
public class CollectionOfNodes {

    /*
    KEY: Xpos-Ypos
    Xpos 4 digits, Ypos 4 digits
    Example: 01500220 --> Xpos = 150, Ypos = 220
    */

    private HashMap<Integer, Node> floor1nodes;
    private HashMap<Integer, Node> floor2nodes;
    private HashMap<Integer, Node> floor3nodes;
    private HashMap<Integer, Node> floor4nodes;
    private HashMap<Integer, Node> floor5nodes;
    private HashMap<Integer, Node> floor6nodes;
    private HashMap<Integer, Node> floor7nodes;

    private ArrayList<HashMap<Integer, Node>> allNodes;

    public CollectionOfNodes() {
        floor1nodes = new HashMap<>();
        floor2nodes = new HashMap<>();
        floor3nodes = new HashMap<>();
        floor4nodes = new HashMap<>();
        floor5nodes = new HashMap<>();
        floor6nodes = new HashMap<>();
        floor7nodes = new HashMap<>();

        allNodes = new ArrayList<HashMap<Integer, Node>>();
        allNodes.add(floor1nodes);
        allNodes.add(floor2nodes);
        allNodes.add(floor3nodes);
        allNodes.add(floor4nodes);
        allNodes.add(floor5nodes);
        allNodes.add(floor6nodes);
        allNodes.add(floor7nodes);
    }

    //resets all nodes to initial state for pathfinding
    //applies to nodes in all hashmaps
    //does two things to each node:
    //  - sets cost to reach as infinite (MAX_INT)
    //  - sets the reference to parentEdge to null
    public void resetForPathfinding() {

        for(int i = 0; i < 7; i++) {
            for(Node n: allNodes.get(i).values()) {
                n.setCostToReach(Integer.MAX_VALUE);
                n.setParentEdge(null);
            }
        }

    }

    //add a node to the collection
    //  input: Node object you want to add to the HashTable collection
    public void addNode(Node node) {
        // Add node entry to the hashmap
        int floor = node.getFloor();
        allNodes.get(floor-1).put(node.getKey(), node);
        if(!allNodes.get(floor-1).containsKey(node.getKey())) {
            System.out.println("COLLECTIONOFNODES: addNode(): added node but then couldn't find in hashmap");
        }
        else {
            System.out.println("COLLECTIONOFNODES: addNode(): successfully added node to hashmap");
        }

    }

    //remove node from the collection
    //  input: Node object you want to remove
    public void removeNode(Node node) {
        // Remove node entry from hashmap
        int floor = node.getFloor();
        allNodes.get(floor-1).remove(node.getKey());
    }

    //returns string representation of ALL FLOORS of the database
    //  returns: concatenation of string representation of ALL 7 FLOORS
    public String toString() {

        String output = "";
        for (int i = 1; i < 8; i++) {
            output += "\n\n||Floor Number: " + i + " |";
            output += toStringFloor(i);
            output += "\n||";
        }
        return output;
    }

    //returns string representation of all nodes belonging to a particular floor
    //  returns: combination of toStrings for all nodes in a floor
    public String toStringFloor(int floor) {

        String output = "";
        Iterator it;
        it = allNodes.get(floor - 1).entrySet().iterator();

        while(it.hasNext()) {
            Map.Entry node = (Map.Entry) it.next();
            output += node.toString();
            output += "\n";
            //it.remove();
        }
        return output;
    }

    //get a node from the hashmap using key data for the node
    //  input: x, y, floor data of a node
    //  output: returns corresponding node object.
    //      -If unable to find, returns null - be sure to implement a check for it!
    public Node getNode(int x, int y, int floor) {
        Node node;
        
        int key = generateNodeKey(x, y);

        if(allNodes.get(floor-1).containsKey(key)) {
            node = allNodes.get(floor-1).get(key);
            return node;
        }
        else {
            //System.out.println("COLLECTIONOFNODES: getNode(): Current floor hashmap: " + mapController.getCollectionOfNodes().toString());
            System.out.println("COLLECTIONOFNODES: getNode(): could not find node with key: " + key);
            return null;
        }

    }

    //get the hash map corresponding to a particular floor
    //  input: floor number of the hash map you want to receive
    //  output: returns one of the 7 hash maps in the collection
    public HashMap<Integer, Node> getMap(int floor) {
        return allNodes.get(floor - 1);
    }

    //generate a key depending on the node information
    //use for creating unique keys in the hash maps
    //  input: x and y coords of a node
    //  returns: integer hash key for a node
    private int generateNodeKey(int x, int y) {
        // Key in format [XXXXYYYY]
        // where x's is the x cord, and y's are y cord
        int key = 0;

        // Add nodes x cord to key
        key +=  x * 10000;

        // Add nodes y cord to key
        key += y;

        return key;
    }

    //get method for all 7 hash maps
    //  returns: one array list (indexes 0 based - 0 to 6) containing all 7 hash maps of nodes
    public ArrayList<HashMap<Integer, Node>> getAllNodes() {
        return allNodes;
    }
}
