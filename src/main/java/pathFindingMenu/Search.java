package pathFindingMenu;

import controllers.Node;

/**
 * Created by Griffin on 4/17/2017.
 */
public abstract class Search {

    public abstract void addNode(Node newNode);
    public abstract Node getNode();
    public abstract void resetNodes();
}
