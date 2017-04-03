package controllers;


import java.util.*;

/**
 * Created by dgian on 4/1/2017.
 */
public class CollectionOfNodes {

    /*
    private ArrayList<Node> nodes;

    public CollectionOfNodes () {
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public String toString() {
        System.out.println("---------- Printing all nodes in CollectionOfNodes ----------");
        for (Node node : nodes) {
            node.toString();
            System.out.println();
        }
        System.out.println("-------------------------------------------------------------");

        return null;
    }
*/
    /*
    KEY: Floor-Xpos-Ypos
    Floor one digit, Xpos 4 digits, Ypos 4 digits
    Example: 401500220 --> 4th floor, Xpos = 150, Ypos = 220
    */

    private HashMap<Integer, Node> nodes;

    public CollectionOfNodes() {
        nodes = new HashMap<>();
    }

    public void addNode(Node node) {
        // Add node entry to the hashmap
        nodes.put(node.getKey(), node);
    }

    public String toString() {
        System.out.println("---------- Printing all nodes in CollectionOfNodes ----------");

        Iterator it = nodes.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry node = (Map.Entry)it.next();
            node.toString();
            it.remove();
        }

        System.out.println("-------------------------------------------------------------");
        return null;
    }

    public Node getNode(int x, int y, int floor) {
        Node node;
        int key = generateNodeKey(x, y, floor);

        node = nodes.get(key);

        if(node == null) {
            System.out.println("Failed to find node with key: " + key);
            return null;
        } else {
            return node;
        }
    }

    private int generateNodeKey(int x, int y, int floor) {
        // Key in format [FXXXXYYYY]
        // where F is floor, x's is the x cord, and y's are y cord
        int key = 0;

        // Add nodes floor to key
        key +=  floor * 100000000;

        // Add nodes x cord to key
        key +=  x * 10000;

        // Add nodes y cord to key
        key += y;

        return key;
    }
}
