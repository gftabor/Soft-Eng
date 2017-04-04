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

    public CollectionOfNodes() {
        floor1nodes = new HashMap<>();
        floor2nodes = new HashMap<>();
        floor3nodes = new HashMap<>();
        floor4nodes = new HashMap<>();
        floor5nodes = new HashMap<>();
        floor6nodes = new HashMap<>();
        floor7nodes = new HashMap<>();
    }

    public void addNode(Node node) {
        // Add node entry to the hashmap
        int floor = node.getFloor();

        switch(floor) {
            case 1:
                floor1nodes.put(node.getKey(), node);
                break;

            case 2:
                floor2nodes.put(node.getKey(), node);
                break;

            case 3:
                floor3nodes.put(node.getKey(), node);
                break;

            case 4:
                floor4nodes.put(node.getKey(), node);
                break;

            case 5:
                floor5nodes.put(node.getKey(), node);
                break;

            case 6:
                floor6nodes.put(node.getKey(), node);
                break;

            case 7:
                floor7nodes.put(node.getKey(), node);
                break;
            default:
                System.out.println("CollectionOfNodes.addNode: Invalid floor");
                break;
        }

    }

    public void removeNode(Node node) {
        // Remove node entry from hashmap
        int floor = node.getFloor();

        switch(floor) {
            case 1:
                floor1nodes.remove(node.getKey());
                break;

            case 2:
                floor2nodes.remove(node.getKey());
                break;

            case 3:
                floor3nodes.remove(node.getKey());
                break;

            case 4:
                floor4nodes.remove(node.getKey());
                break;

            case 5:
                floor5nodes.remove(node.getKey());
                break;

            case 6:
                floor6nodes.remove(node.getKey());
                break;

            case 7:
                floor7nodes.remove(node.getKey());
                break;
            default:
                System.out.println("CollectionOfNodes.addNode: Invalid floor");
                break;
        }
    }

/*
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
*/
    public Node getNode(int x, int y, int floor) {
        /*
        Node node;
        int key = generateNodeKey(x, y, floor);

        if(nodes.containsKey(key)) {
            node = nodes.get(key);
            return node;
        } else { return null; }
        */
        Node node;
        int key = generateNodeKey(x, y);

        switch(floor) {
            case 1:
                if(floor1nodes.containsKey(key)) {
                    node = floor1nodes.get(key);
                    return node;
                } else { return null; }

            case 2:
                if(floor2nodes.containsKey(key)) {
                    node = floor2nodes.get(key);
                    return node;
                } else { return null; }

            case 3:
                if(floor3nodes.containsKey(key)) {
                    node = floor3nodes.get(key);
                    return node;
                } else { return null; }

            case 4:
                if(floor4nodes.containsKey(key)) {
                    node = floor4nodes.get(key);
                    return node;
                } else { return null; }

            case 5:
                if(floor5nodes.containsKey(key)) {
                    node = floor5nodes.get(key);
                    return node;
                } else { return null; }

            case 6:
                if(floor6nodes.containsKey(key)) {
                    node = floor6nodes.get(key);
                    return node;
                } else { return null; }

            case 7:
                if(floor7nodes.containsKey(key)) {
                    node = floor7nodes.get(key);
                    return node;
                } else { return null; }

            default:
                System.out.println("CollectionOfNodes.getNode: Invalid location");
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
