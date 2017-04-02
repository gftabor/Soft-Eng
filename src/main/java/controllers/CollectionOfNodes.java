package controllers;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

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

    public void addNode(Integer key, Node node) {

        nodes.put(key, node);
    }

    public String toString() {
        return null;
    }
}
