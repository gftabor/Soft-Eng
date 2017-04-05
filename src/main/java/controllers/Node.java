package controllers;

import java.util.ArrayList;

/**
 * Created by mylena on 4/1/17.
 */
public class Node implements Comparable<Node>{
    private int posX;
    private int posY;
    private boolean isHidden;
    private boolean isEnabled; //if isEnabled is true, node is valid for pathfinding. If false, it's looked over in pathfinding
    private String name; //room number, general location, etc. ex: "Room 123" or "Cafeteria"
    private int floor;
    private String type; //type indicates if node belongs to doctor's office, food service, restroom, etc.
    private String roomNum;

    //fields used for pathfinding:
    private double costToReach;
    private double totalCost;
    private Edge parentEdge;

    private ArrayList<Edge> edges = new ArrayList<Edge>();

    //Basic Constructor
    //  - must fill in all fields manually
    public Node(int posX, int posY, int floor, boolean hidden, boolean enabled, String type, String name, String roomNum) {
        this.posX = posX;
        this.posY = posY;
        this.isHidden = hidden;
        this.isEnabled = enabled;
        this.name = name;
        this.floor = floor;
        this.roomNum = roomNum;
        this.type = type;
        this.costToReach = Integer.MAX_VALUE;
        this.totalCost = Integer.MAX_VALUE; //costToReach + hueristic
        this.parentEdge = null; //instantiate reference to null
    }

    //Quick Constructor
    //  -assume enabled by default
    public Node(int posX, int posY, boolean hidden, boolean isEnabled, String name, int floor) {
        this.posX = posX;
        this.posY = posY;
        this.isHidden = hidden;
        this.isEnabled = true;
        this.name = name;
        this.floor = floor;
        this.costToReach = 1000000;
        this.totalCost = 1000000; //costToReach + hueristic
    }

    public String toString() {
        String output;
        output = "|Node " + name + ":" + " Pos X: " + posX + " Pos y: " + posY + "  Hidden: " + isHidden + "  Enabled: " + isEnabled + "|";

        return output;
    }
    

    //add edge to arrayList of nearby edges
    public void addEdge(Edge myEdge) {
        edges.add(myEdge);
    }

    public ArrayList<Edge> getEdgeList(){
        return edges;
    }

    //remove edge to arrayList of nearby edges (uses edge object)
    public void removeEdge(Edge myEdge) {
        edges.remove(myEdge);
    }

    //generates a key for the CollectionOfNodes hashmap
    public int getKey() {
        // Key in format [XXXXYYYY]
        // x's is the x cord, and y's are y cord
        int key = 0;

        // Add nodes x cord to key
        key +=  posX * 10000;

        // Add nodes y cord to key
        key += posY;

        return key;
    }

    //some getters and setters
    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getFloor() {
        return floor;
    }

    public double getCostToReach() {
        return costToReach;
    }

    public void setCostToReach(double costToReach) {
        this.costToReach = costToReach;
    }

    public Edge getParentEdge() {
        return parentEdge;
    }

    public void setParentEdge(Edge parentEdge) {
        this.parentEdge = parentEdge;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double newCost) {
        this.totalCost = newCost;
    }

    //Allows sorting list based on totalCost
    @Override
    public int compareTo(Node o) {
        return Double.compare(this.getTotalCost(),o.getTotalCost());
    }
}
