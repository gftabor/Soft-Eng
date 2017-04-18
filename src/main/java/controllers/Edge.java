package controllers;

import java.lang.Math;

/**
 * Created by mylena on 4/1/17.
 */
public class Edge {
    private double weight;
    private int floorStart;
    private int floorEnd;
    private Node startNode;
    private Node endNode;

    //standard constructor
    //  input: all fields at constructor
    public Edge(Node startNode, Node endNode, int floorStart, int floorEnd){
        this.startNode = startNode;
        this.endNode = endNode;
        this.weight = calculateWeight(startNode.getPosX(), startNode.getPosY(), endNode.getPosX(), endNode.getPosY());
        this.floorStart = floorStart;
        this.floorEnd = floorEnd;
    }

    //adds edge between two nodes - don't know if needed yet
    public void addEdge(Node startNode, Node endNode, int floorStart, int floorEnd) {
        /*
        Edge e = new Edge(startNode, endNode, floorStart, floorEnd);
        startNode.addEdge(e);
        endNode.addEdge(e);

        System.out.println("Edge has been added");
        */
    }

    //removes references to self from startNode and endNode
    public void deleteEdge() {
        this.startNode.removeEdge(this);
        this.endNode.removeEdge(this);
        System.out.println("Edge has been deleted");
    }

    //use distance formula to calculate weight of edge (weight is used in pathfinding)
    //NOTE: ONLY CALCULATES FOR SAME FLOOR. Floor to floor calc is not implemented yet
    //  input: x and y coordinates for both nodes - need to include floors in the future
    //  output: distance between the nodes - SLD
    public double calculateWeight(int x1, int y1, int x2, int y2) {
        //implementation of this formula:
        if (floorStart != floorEnd) {
            System.out.println("Multifloor edge calc executing...");
            //3 types:
            //  -stair
            //  -elevator
            //  -other (default catch-all)
            //Want to favor elevators over stairs, anything else just pick a default value
            if(startNode.getType() == "Elevator" && endNode.getType() == "Elevator") {
                return 10;
            } else if(startNode.getType() == "Stairwell" && endNode.getType() == "Stairwell") {
                return 20000 * (Math.abs(floorEnd - floorStart)); //get worse by floor
            } else {
                return 400; //idk just pick something, perhaps revisit later
            }

        }

        //differences between coords
        int xDiff;
        int yDiff;

        xDiff = x2 - x1;
        yDiff = y2 - y1;

        //square the differences
        double squareX;
        double squareY;
        squareX = Math.pow(xDiff, 2);
        squareY = Math.pow(yDiff, 2);

        //sum the squares
        double sumSquares;
        sumSquares = squareX + squareY;
        //square root for result
        double result;
        result = Math.sqrt(sumSquares);
        return result;

    }

    //getters and setters
    public double getWeight() {
        return weight; }

    public Node getStartNode() { return startNode; }

    public Node getEndNode() {
        return endNode;
    }

    //returns node at the other end of the edge
    //  input: a node
    //  output: node on other end of edge
    //      -if node is not part of this edge, returns null
    public Node getNeighbor(Node currentNode){
        if(currentNode.equals(startNode))
            return endNode;
        if(currentNode.equals((endNode)))
            return startNode;
        return null;
    }
}
