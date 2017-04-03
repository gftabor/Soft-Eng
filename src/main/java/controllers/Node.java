package controllers;

import java.util.ArrayList;

/**
 * Created by mylena on 4/1/17.
 */
public class Node {
    private int posX;
    private int posY;
    private boolean isHidden;
    private boolean isEnabled;
    private String name;
    private int floor;

    private ArrayList<Edge> edges = new ArrayList<Edge>();

    //Basic Constructor
    //  - must fill in all fields manually
    public Node(int posX, int posY, boolean hidden, boolean enabled, String name, int floor) {
        this.posX = posX;
        this.posY = posY;
        this.isHidden = hidden;
        this.isEnabled = enabled;
        this.name = name;
        this.floor = floor;
    }

    //Quick Constructor
    //  -assume enabled by default
    public Node(int posX, int posY, boolean hidden, String name, int floor) {
        this.posX = posX;
        this.posY = posY;
        this.isHidden = hidden;
        this.isEnabled = true;
        this.name = name;
        this.floor = floor;
    }

    public String toString() {
        System.out.println("Node " + name + ":");
        System.out.println("    Pos X: " + posX);
        System.out.println("    Pos y: " + posY);
        System.out.println("    Hidden: " + isHidden);
        System.out.println("    Enabled: " + isEnabled);
        //System.out.println("    Name: " + name);

        return null;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getFloor() {
        return floor;
    }

    //add edge to arrayList of nearby edges
    public void addEdge(Edge myEdge) {
        edges.add(myEdge);
    }

    //remove edge to arrayList of nearby edges (uses edge object)
    public void removeEdge(Edge myEdge) {
        edges.remove(myEdge);
    }

    public int getKey() {
        // Key in format [FXXXXYYYY]
        // where F is floor, x's is the x cord, and y's are y cord
        int key = 0;

        // Add nodes floor to key
        key +=  floor * 100000000;

        // Add nodes x cord to key
        key +=  posX * 10000;

        // Add nodes y cord to key
        key += posY;

        return key;
    }

}
