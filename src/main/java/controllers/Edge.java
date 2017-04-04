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
    //  -input all fields at constructor
    public Edge(Node startNode, Node endNode, int floorStart, int floorEnd){
        this.startNode = startNode;
        this.endNode = endNode;
        double weight = calculateWeight(startNode.getPosX(), startNode.getPosY(), endNode.getPosX(), endNode.getPosY());
        this.floorStart = floorStart;
        this.floorEnd = floorEnd;
    }

    public void addEdge() {
        System.out.println("Edge has been added");
    }

    public void deleteEdge() {
        System.out.println("Edge has been deleted");
    }

    //use distance formula to calculate weight of edge (weight is used in pathfinding)
    //NOTE: ONLY CALCULATES FOR SAME FLOOR. Floor to floor calc is not implemented yet
    public double calculateWeight(int x1, int y1, int x2, int y2) {
        //implementation of this formula:
        if (floorStart != floorEnd) {
            System.out.println("CROSS FLOOR NOT IMPLEMENTED YET!!");
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

    public double getWeight() { return weight; }

    public Node getStartNode() { return startNode; }

    public Node getEndNode() {
        return endNode;
    }

    public Node getNeighbor(Node currentNode){
        if(currentNode.equals(startNode))
            return endNode;
        if(currentNode.equals((endNode)))
            return startNode;
        return null;
    }
}
