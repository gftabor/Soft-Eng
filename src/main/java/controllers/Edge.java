package controllers;

/**
 * Created by mylena on 4/1/17.
 */
public class Edge {
    private String type;
    private float weight;
    public Node startNode;
    public Node endNode;

    public Edge(String type, Node startNode, Node endNode){
        this.type = type;
        this.startNode = startNode;
        this.endNode = endNode;
        float weight = calculateWeight();
    }

    public void addEdge() {
        System.out.println("Edge has been added");
    }

    public void deleteEdge() {
        System.out.println("Edge has been deleted");
    }

    public float calculateWeight() {
        return 0;
    }

}
