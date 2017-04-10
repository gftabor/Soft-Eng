package pathFindingMenu;

import controllers.Edge;
import controllers.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import java.lang.Math;

/**
 * Created by griffincecil on 4/1/2017.
 */
public class Pathfinder {
    private ArrayList<Node> frontier = new ArrayList<Node>();
    private HashSet<Node> alreadyProcessed = new HashSet<Node>();;

    private ArrayList<Edge> path = new ArrayList<Edge>();


    public ArrayList<Edge> getPath() {
        return path;
    }

    //double heuristic function for A* pathfinding
    //uses a straight-line-distance (SLD) heuristic
    //  input: 2 nodes
    //      - current node pathfinding is looking at
    //      - goal node pathfinding is trying to reach
    //  output: SLD (double) between input nodes as heuristic val
    private double getHueristic(Node currentNode, Node goalNode){
       double squareX = Math.pow((currentNode.getPosX()-goalNode.getPosX()),2);
       double squareY = Math.pow((currentNode.getPosY()-goalNode.getPosY()),2);
       return Math.sqrt(squareX + squareY);

    }

    //if process node is end node return true, otherwise process and return false
    private boolean processNode(Node currentNode, Node goalNode){
        if(currentNode.equals(goalNode))
            return true;

        for(Edge currentEdge:currentNode.getEdgeList()){
            Node neighbor = currentEdge.getNeighbor(currentNode);

            //--node must be enabled
            if (!neighbor.getEnabled()) {
                continue;
            }

            //System.out.println("weight" + currentEdge.getWeight());
            double neighbourNewCost = currentNode.getCostToReach() + currentEdge.getWeight();
            //System.out.println(neighbor.getCostToReach() + "   " + neighbourNewCost);
            if(neighbor.getCostToReach() > neighbourNewCost) { //if better path found
                neighbor.setParentEdge(currentEdge);
                neighbor.setCostToReach(neighbourNewCost);
                neighbor.setTotalCost(neighbourNewCost + getHueristic(neighbor, goalNode));

                //if the object is in frontier only edit the object
                if(!frontier.contains(neighbor) && neighbor.getEnabled()) {
                    frontier.add(neighbor);
                    //System.out.println("new frontier");
                }
            }

        }
        return false;
    }

    //generates a path on the map given a start node and the end node
    //runs A* pathfinding algorithm
    //  input: 2 nodes - a start and an end
    //  output: the total cost of the shortest path (type=double)
    public double generatePath(Node startNode, Node endNode) {
        System.out.println("PATHFINDER: generating path from node at (" + startNode.getPosX() + ", " +
                startNode.getPosY() + ") to node at (" + endNode.getPosX() + ", " +
                endNode.getPosY() + ")");
        alreadyProcessed.clear();
        frontier.clear();
        path.clear();
        if(!(startNode.getEnabled() && endNode.getEnabled())){
            System.out.println("selected node not enabled");
            return -2;
        }
        startNode.setTotalCost(getHueristic(startNode, endNode));
        startNode.setCostToReach(0);
        frontier.add(startNode);
        boolean finished = false;
        while (!finished && !frontier.isEmpty()) {
            Collections.sort(frontier);
            Node processing = frontier.get(0);
            finished = processing.equals(endNode);//
            if (!alreadyProcessed.contains(processing)) {
                processNode(processing, endNode);
                alreadyProcessed.add(processing);
            }
            frontier.remove(0);
        }
        Node viewingNode = endNode;
        while(!viewingNode.equals(startNode) && finished){
            path.add(viewingNode.getParentEdge());
            viewingNode = viewingNode.getParentEdge().getNeighbor(viewingNode);
        }

        if (finished)
            return endNode.getTotalCost();
        System.out.println("not possible");
        return -1;
    }

    //in progress -> prints directions until I can figure out how to get it on the UI
    public void getTextDirections(ArrayList<Edge> path) {
        path = this.path;

        for(int i = 0; i < path.size(); i++) {
            if(getAngle(path.get(i), path.get(i+1)) >= 170.0 &&
                    getAngle(path.get(i), path.get(i+1)) <= 110.0) {
                if(getAngle(path.get(i-1), path.get(i)) >= 170.0 &&
                        getAngle(path.get(i-1), path.get(i)) <= 110.0){
                  System.out.println("Continue");
                } else { System.out.println("Continue straight."); }
            }
            else if(getAngle(path.get(i), path.get(i+1)) >= 70.0 &&
                    getAngle(path.get(i), path.get(i+1)) <= 110.0) {
                System.out.println("Turn right.");
            }
            else if(getAngle(path.get(i), path.get(i+1)) >= 250.0 &&
                    getAngle(path.get(i), path.get(i+1)) <= 290.0) {
                System.out.println("Turn left.");
            }
            else if(getAngle(path.get(i), path.get(i+1)) > 110.0 &&
                    getAngle(path.get(i), path.get(i+1)) < 170.0) {
                System.out.println("Make a slight right.");
            }
            else if(getAngle(path.get(i), path.get(i+1)) > 190.0 &&
                    getAngle(path.get(i), path.get(i+1)) < 250.0) {
                System.out.println("Make a slight left.");
            }
            else if(getAngle(path.get(i), path.get(i+1)) >= 10.0 &&
                    getAngle(path.get(i), path.get(i+1)) < 70.0) {
                System.out.println("Make a hard right.");
            }
            else if(getAngle(path.get(i), path.get(i+1)) > 290.0 &&
                    getAngle(path.get(i), path.get(i+1)) <= 350.0) {
                System.out.println("Make a hard left.");
            }
        }
    }


    //finds angle between two edges by finding the unit vectors for each
    //taking the dot product between the unit vectors
    //and taking the arccos() of the dot product
    private double getAngle(Edge e1, Edge e2) {

        //find unit vectors
        ArrayList<Double> e1comp = getUnitVector(e1);
        ArrayList<Double> e2comp = getUnitVector(e2);

        //find dot product
        double dot = e1comp.get(0)*e2comp.get(0) + e1comp.get(1)*e2comp.get(1);

        double angle = Math.acos(dot);

        return angle;
    }

    private ArrayList<Double> getUnitVector(Edge e1) {
        double xcomp = e1.getEndNode().getPosX() - e1.getStartNode().getPosX();
        double ycomp = e1.getEndNode().getPosY() - e1.getStartNode().getPosY();

        double magnitude = Math.sqrt(Math.pow(xcomp, 2.0) + Math.pow(ycomp, 2.0));

        //list of components of unit vector
        ArrayList<Double> unitComponents = new ArrayList<>();
        unitComponents.add(xcomp/magnitude);
        unitComponents.add(ycomp/magnitude);

        return unitComponents;


    }

}
