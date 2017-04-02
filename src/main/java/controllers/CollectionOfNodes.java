package controllers;


import java.util.ArrayList;

/**
 * Created by dgian on 4/1/2017.
 */
public class CollectionOfNodes {

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

}
