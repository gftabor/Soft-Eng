package main.java;

/**
 * Created by griffincecil on 4/1/2017.
 */
public class NodeGraph {
    private boolean isShowing;
    private int numNodes;
    private int numEdges;

    public boolean getIsShowing() {
        return isShowing;
    }

    public int getNumNodes() {
        return numNodes;
    }

    public int getNumEdges() {
        return numEdges;
    }

    public void setIsShowing(boolean state) {
        this.isShowing = state;
    }

    public void setNumNodes(int num) {
        this.numNodes = num;
    }

    public void setNumEdges(int num) {
        this.numEdges = num;
    }
}
