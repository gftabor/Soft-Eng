package pathFindingMenu;

import controllers.Node;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Griffin on 4/17/2017.
 */
public class AStar extends Search {

    private ArrayList<Node> frontier = new ArrayList<Node>();
    public String toString(){

        return "A *";
    }
    public void addNode(Node newNode){
        if(!frontier.contains(newNode) && newNode.getEnabled()) {
            frontier.add(newNode);
        }
    }
    public Node getNode() {
        if(frontier.size() > 0) {
            Collections.sort(frontier);

            Node processing = frontier.get(0);
            frontier.remove(0);

            return processing;
        }
        return null;
    }

    public void resetNodes(){
        frontier = new ArrayList<Node>();
    }

}
