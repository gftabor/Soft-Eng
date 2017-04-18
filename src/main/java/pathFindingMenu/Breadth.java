package pathFindingMenu;

import controllers.Node;

import java.util.ArrayList;

/**
 * Created by Griffin on 4/17/2017.
 */
public class Breadth extends Search{

    private ArrayList<Node> frontier = new ArrayList<Node>();


    //append to end of list
    public void addNode(Node newNode){
        if(!frontier.contains(newNode) && newNode.getEnabled()) {
            frontier.add(newNode);
        }
    }
    public String toString(){

        return "Breadth";
    }
    //get beginning of list
    public Node getNode() {
        if(frontier.size() > 0) {
            Node processing = frontier.get(0);
            frontier.remove(0);
            return processing;
        }
        return null;
    }
    //clear list
    public void resetNodes(){
        frontier = new ArrayList<Node>();
    }

}
