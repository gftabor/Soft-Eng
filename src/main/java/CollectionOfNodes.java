package main.java;

import java.util.ArrayList;

/**
 * Created by dgian on 4/1/2017.
 */
public class CollectionOfNodes {

    private ArrayList nodes;

    CollectionOfNodes () {
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }



}
