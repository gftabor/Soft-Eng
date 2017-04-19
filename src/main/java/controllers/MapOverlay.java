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
    public MapOverlay(Pane scenePane , mapScene controller){
        this.currentPane = scenePane;
        System.out.println("map   " + currentPane );
        this.sceneController = controller;
    }

    private Pane currentPane;
    private HashMap<Integer, controllers.Node> currentNodeMap;
    private Line lne;
    private Circle location;
    private static final double labelRadius = 5.5;
    mapScene sceneController;

    private ArrayList<Circle> ButtonList = new ArrayList<Circle>();

    private ArrayList<Line> lineList = new ArrayList<Line>();

    //takes in a Hashtable when scene is switched and calls setNodes

    public void setMapAndNodes(HashMap<Integer, Node> nodeMap, boolean devMode) {

        if (devMode) {
            System.out.println("DEVMODE active");
        }

        currentNodeMap = nodeMap;
        // Clear circles from the scene
        while (ButtonList.size() > 0) {
            currentPane.getChildren().remove(ButtonList.get(0));
            ButtonList.remove(0);
        }
        // Add all the nodes onto the scene as buttons
        for (controllers.Node current : nodeMap.values()) {

                //  - node can be disabled and show in dev mode
                //devs can see everything and interact with everything
                if (devMode == true) {
                    create_Button(current.getPosX(), current.getPosY(), current.getIsHidden(), current.getEnabled());
                } else {
                    //if not dev mode:
                    //show only if enabled and not hidden
                    if (current.getIsHidden() == false && current.getEnabled() == true) {
                        create_Button(current.getPosX(), current.getPosY(), false, true);
                    }
                }
                //else skip displaying the node

            }
            wipeEdgeLines();
        }



    public void create_Button(double nodeX, double nodeY, boolean hidden, boolean enabled){
        //System.out.println("checking button");
        //System.out.println("make button");
        location = new Circle(labelRadius);
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

        if (!enabled) {
            location.setFill(Color.RED);
        } else if(hidden) {
            location.setFill(Color.GRAY);
        }


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
    public void createEdgeLines(ArrayList<controllers.Edge> edgeList, boolean highlighted) {
        //for-each loop through arraylist
        wipeEdgeLines();
        for(controllers.Edge thisEdge: edgeList) {
            lne = new Line();

            //don't draw lines if on different floor
            if (thisEdge.getStartNode().getFloor() != thisEdge.getEndNode().getFloor()) {
                continue;
                //maybe use colors???
            }

            //config to display properly
            if(highlighted) {
                lne.setFill(Color.RED);
                lne.setStroke(Color.RED);
                lne.setStrokeWidth(4.5);
            } else {
                lne.setStroke(Color.BLACK);
                lne.setStrokeWidth(0.7);
            }

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

    //get method for labelRadius
    public double getLabelRadius() {
        return labelRadius;
    }

    //get method for the circle list (buttonList)
    public ArrayList<Circle> getButtonList() {
        return ButtonList;
    }

    //draw each floor's edge lines
    public void drawFloorEdges(int floor) {
        ArrayList<Edge> currentFloorEdges = new ArrayList<>();

        //get all edges for this floor in that one.
        for (Edge e: MapController.getInstance().getEdgeCollection()) {
            if (e.getStartNode().getFloor() == floor && e.getEndNode().getFloor() == floor) {
                currentFloorEdges.add(e);
            }
        }

        createEdgeLines(currentFloorEdges, false);
    }
}
