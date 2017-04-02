package controllers;

import java.lang.Math;

/**
 * Created by mylena on 4/1/17.
 */
public class Edge {
    private double weight;
    private int floor;
    private Node startNode;
    private Node endNode;

    //standard constructor
    //  -input all fields at constructor
    public Edge(Node startNode, Node endNode, int floor){
        this.startNode = startNode;
        this.endNode = endNode;
        float weight = calculateWeight(startNode.getPosX(), startNode.getPosY(), endNode.getPosX(), endNode.getPosY());
        this.floor = floor;
    }

    public void addEdge() {
        System.out.println("Edge has been added");
    }

    public void deleteEdge() {
        System.out.println("Edge has been deleted");
    }

    //use distance formula to calculate weight of edge (weight is used in pathfinding)
    public double calculateWeight(int x1, int y1, int x2, int y2) {
        //implementation of this formula:

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

}
