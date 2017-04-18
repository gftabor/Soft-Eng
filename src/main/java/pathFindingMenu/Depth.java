package pathFindingMenu;

import controllers.Node;

import java.util.LinkedList;

/**
 * Created by Griffin on 4/17/2017.
 */
public class Depth extends Search{

    private LinkedList<Node> frontier = new LinkedList<Node>();


    //append to end of list
    public void addNode(Node newNode){
        if(!frontier.contains(newNode) && newNode.getEnabled()) {
            frontier.addFirst(newNode);
        }
    }
    //get beginning of list
    public Node getNode() {
        if(frontier.size() > 0) {
            Node processing = frontier.pollFirst();
            return processing;
        }
        return null;
    }
    //clear list
    public void resetNodes(){
        frontier = new LinkedList<Node>();
    }

}