package controllers;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Griffin on 4/5/2017.
 */
public class MapOverlay {
    public MapOverlay(Pane scenePane , AbsController controller){
        this.currentPane = scenePane;
        System.out.println("map   " + currentPane );
        this.sceneController = controller;
    }

    private Pane currentPane;
    private HashMap<Integer, controllers.Node> currentNodeMap;
    private Line lne;
    private Circle location;
    private static final double lableRadius = 8.5;
    AbsController sceneController;

    private ArrayList<Circle> ButtonList = new ArrayList<Circle>();

    private ArrayList<Line> lineList = new ArrayList<Line>();

    //takes in a Hashtable when scene is switched and calls setNodes
    public void setMapAndNodes(HashMap<Integer, Node> nodeMap, boolean showHidden) {
        currentNodeMap = nodeMap;
        // Clear circles from the scene
        while (ButtonList.size() < 0) {
            currentPane.getChildren().remove(ButtonList.get(0));
            ButtonList.remove(0);
        }
        // Add all the nodes onto the scene as buttons
        for(controllers.Node current: nodeMap.values()){

            //criteria for node to display:
            //  - node must be enabled
            if (current.getEnabled() == true) {
                if(current.getIsHidden() == false)
                    create_Button(current.getPosX(), current.getPosY(),false);
                if(current.getIsHidden() == true && showHidden)
                    create_Button(current.getPosX(), current.getPosY(),true);

            }
            //else skip displaying the node
        }
        wipeEdgeLines();
    }

    public void create_Button(double nodeX, double nodeY, boolean hidden){
        //System.out.println("checking button");
        //System.out.println("make button");
        location = new Circle(lableRadius);
        location.setOnMouseClicked(e -> {

            Object o = e.getSource();
            Circle c = (Circle) o;
            sceneController.sceneEvent((int)((nodeX)), (int)((nodeY)), c);
            //set color --
        });

        // this code sets node's x and y pos to be on the plane holding the graph
        currentPane.getChildren().add(location);
        location.setLayoutX(nodeX);
        location.setLayoutY(nodeY);
        location.toFront();
        if(hidden)
            location.setFill(Color.GRAY);

        ButtonList.add(location);
    }


    public void wipeEdgeLines(){
        for(Line currentLine : lineList) {
            currentPane.getChildren().remove(currentLine);
        }

    }
    //creates visual representations of the edges of nodes on the pane
    //  input: any arraylist of Edge objects
    //NOTE: caller is responsible for not sending duplicate edges
    public void createEdgeLines(ArrayList<controllers.Edge> edgeList) {
        //for-each loop through arraylist
        wipeEdgeLines();
        for(controllers.Edge thisEdge: edgeList) {
            lne = new Line();
            //add to pane
            currentPane.getChildren().add(lne);
            lne.setStartX(thisEdge.getStartNode().getPosX());
            lne.setStartY(thisEdge.getStartNode().getPosY());
            lne.setEndX(thisEdge.getEndNode().getPosX());
            lne.setEndY(thisEdge.getEndNode().getPosY());
            //show
            lne.toFront();
            //add to list
            lineList.add(lne);
        }
    }
}
