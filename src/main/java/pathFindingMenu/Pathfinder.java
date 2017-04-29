package pathFindingMenu;

import controllers.Edge;
import controllers.Node;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by griffincecil on 4/1/2017.
 */
public class Pathfinder {
    private HashSet<Node> alreadyProcessed = new HashSet<Node>();;

    private ArrayList<Edge> path = new ArrayList<Edge>();
    public Search algorithm = new AStar();

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
       return Math.sqrt(squareX + squareY) + 1000* Math.abs(currentNode.getFloor() - goalNode.getFloor());

    }
    public void algorithmSwitch(int choice){
        switch(choice){
            case 0: algorithm = new AStar(); break;
            case 1: algorithm = new Breadth(); break;
            case 2: algorithm = new Depth(); break;
            default: algorithm = new AStar(); break;
        }
    }

    //if process node is end node return true, otherwise process and return false
    private boolean processNode(Node currentNode, Node goalNode, int currentPermissionLevel, boolean useStairs){
        if(currentNode.equals(goalNode))
            return true;

        for(Edge currentEdge:currentNode.getEdgeList()){
            Node neighbor = currentEdge.getNeighbor(currentNode);

            //--node must be enabled
            if (!neighbor.getEnabled()) {
                continue;
            }
//
//            System.out.println("---");
//            System.out.println("current perms: " + currentPermissionLevel);
//            System.out.println("neighbor perms: " + neighbor.getPermissionLevel());
            //must be of correct permission level for the node to be used
            if (neighbor.getPermissionLevel() > currentPermissionLevel) {
                continue;
            }

            if (!useStairs) {
                if (neighbor.getType().equalsIgnoreCase("Stair")) {
                    continue;
                }
            }

            //System.out.println("weight" + currentEdge.getWeight());
            double neighbourNewCost = currentNode.getCostToReach() + currentEdge.getWeight();
            //System.out.println(neighbor.getCostToReach() + "   " + neighbourNewCost);
            if(neighbor.getCostToReach() > neighbourNewCost) { //if better path found
                neighbor.setParentEdge(currentEdge);
                neighbor.setCostToReach(neighbourNewCost);
                neighbor.setTotalCost(neighbourNewCost + getHueristic(neighbor, goalNode));
                //if the object is in frontier only edit the object

            }
            algorithm.addNode(neighbor);


        }
        return false;
    }

    //generates a path on the map given a start node and the end node
    //runs A* pathfinding algorithm
    //  input: 2 nodes - a start and an end
    //  output: the total cost of the shortest path (type=double)
    public double generatePath(Node startNode, Node endNode, int permissionLevel, boolean useStairs) {
        System.out.println("PATHFINDER: generating path from node at (" + startNode.getPosX() + ", " +
                startNode.getPosY() + ", floor: " + startNode.getFloor() + ") to node at (" + endNode.getPosX() + ", " +
                endNode.getPosY() + ", floor: " + endNode.getFloor() + ")");
        for(Node oldNode : alreadyProcessed){
            oldNode.setTotalCost(Integer.MAX_VALUE);
            oldNode.setCostToReach(Integer.MAX_VALUE);
        }
        alreadyProcessed.clear();
        path.clear();
        algorithm.resetNodes();
        if ((!(startNode.getEnabled() && endNode.getEnabled()))
                && permissionLevel >= startNode.getPermissionLevel() && permissionLevel >= endNode.getPermissionLevel()){
            System.out.println("selected node not enabled");
            return -2;
        }

        startNode.setTotalCost(getHueristic(startNode, endNode));
        startNode.setCostToReach(0);
        algorithm.addNode(startNode);
        boolean finished = false;
        int tries =0;
        while (!finished) {
            Node processing = algorithm.getNode();
            if(processing == null)
                break;
            finished = processing.equals(endNode);
            if (!alreadyProcessed.contains(processing)) {
                processNode(processing, endNode, permissionLevel, useStairs);
                alreadyProcessed.add(processing);
            }
            tries ++;
        }
        Node viewingNode = endNode;
        while(!viewingNode.equals(startNode) && finished){
            path.add(viewingNode.getParentEdge());
            viewingNode = viewingNode.getParentEdge().getNeighbor(viewingNode);
        }
        System.out.println(algorithm + " had to search  " + tries +  " and path contains " + path.size() + " edges");

        if (finished)
            return endNode.getTotalCost();
        System.out.println("not possible");
        return -1;
    }

}
