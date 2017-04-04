package pathFindingMenu;

import controllers.Edge;
import controllers.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Created by griffincecil on 4/1/2017.
 */
public class Pathfinder {
    private ArrayList<Node> frontier;
    private HashSet<Node> alreadyProcessed;

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
            double neighbourNewCost = currentNode.getCostToReach() + currentEdge.getWeight();

            if(neighbor.getCostToReach() > neighbourNewCost) { //if better path found
                neighbor.setParentEdge(currentEdge);
                neighbor.setCostToReach(neighbourNewCost);
                neighbor.setTotalCost(neighbourNewCost + getHueristic(neighbor, goalNode));
                frontier.add(neighbor);
            }

        }
        return false;
    }

    public double generatePath(Node startNode, Node endNode) {
        System.out.println("PATHFINDER: generating path from node at (" + startNode.getPosX() + ", " +
                            startNode.getPosY() + ") to node at (" + endNode.getPosX() + ", " +
                            endNode.getPosY() + ")");
        startNode.setTotalCost(0);
        startNode.setCostToReach(0);
        frontier.add(startNode);
        boolean finished = false;
        while(!finished){
            Collections.sort(frontier);
            Node processing = frontier.get(0);//might be biggest cost currently
            finished = processing.equals(endNode);//

            if(!alreadyProcessed.contains(processing)) {
                processNode(processing, endNode);
                alreadyProcessed.add(processing);
            }
            //frontier.remove(processing);
            frontier.remove(0);//maybe cheaper
        }
        return endNode.getTotalCost();
    }

}
