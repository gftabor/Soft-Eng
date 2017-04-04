package controllers;


import java.util.*;

/**
 * Created by dgian on 4/1/2017.
 */
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

    public void removeNode(Node node) {
        // Remove node entry from hashmap
        int floor = node.getFloor();
        allNodes.get(floor-1).remove(node.getKey());
    }


    public String toString() {

        String output = "";
        for (int i = 1; i < 8; i++) {
            output += toStringFloor(i);
        }
        return output;
    }

    //do this
    public String toStringFloor(int floor) {

        String output = "";
        Iterator it;
        switch (floor) {
            case 1:
                it = floor1nodes.entrySet().iterator();
                break;
            case 2:
                it = floor2nodes.entrySet().iterator();
                break;
            case 3:
                it = floor3nodes.entrySet().iterator();
                break;
            case 4:
                it = floor4nodes.entrySet().iterator();
                break;
            case 5:
                it = floor5nodes.entrySet().iterator();
                break;
            case 6:
                it = floor6nodes.entrySet().iterator();
                break;
            case 7:
                it = floor7nodes.entrySet().iterator();
                break;
            default:
                System.out.println("Error: ToStringFloor");
                //by default set to first floor
                it = floor1nodes.entrySet().iterator();
                break;
        }
        while(it.hasNext()) {
            Map.Entry node = (Map.Entry) it.next();
            output += node.toString();
            output += "\n";
            it.remove();
        }
        return output;
    }

    public Node getNode(int x, int y, int floor) {
        Node node;
        int key = generateNodeKey(x, y);

        if(allNodes.get(floor-1).containsKey(key)) {
            node = allNodes.get(floor-1).get(key);
            return node;
        }
        else {
            System.out.println("COLLECTIONOFNODES: getNode(): could not find node with key: " + key);
            return null;
        }

    }

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
}
