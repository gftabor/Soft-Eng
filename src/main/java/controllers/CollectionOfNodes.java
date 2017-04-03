package controllers;


import java.util.*;

/**
 * Created by dgian on 4/1/2017.
 */
public class CollectionOfNodes {

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

    public void removeNode(Node node) {
        // Remove node entry from hashmap
        nodes.remove(node.getKey());
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

        if(nodes.containsKey(key)) {
            node = nodes.get(key);
            return node;
        } else { return null; }
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
