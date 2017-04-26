package controllers;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private static final double labelRadius = 6.8;
    private final double sizeUpRatio = 1.9;

    private final double lineHighlightedStrokeW = 4.5;
    private final double lineNonHighlightedStrokeW = 1.7;
    private final double lineSizeUpRatio  = 1.5;
    mapScene sceneController;

    private ArrayList<Circle> ButtonList = new ArrayList<Circle>();

    private ArrayList<Line> lineList = new ArrayList<Line>();

    private static double zoom = 1;

    private static int pathfinding = 0;

    //takes in a Hashtable when scene is switched and calls setNodes

    public void setMapAndNodes(HashMap<Integer, Node> nodeMap, boolean devMode, int floor) {

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
                    create_Button(current, floor);
                } else {
                    //if not dev mode:
                    //show only if enabled and not hidden
                    if (current.getIsHidden() == false && current.getEnabled() == true) {
                        create_Button(current, floor);
                    }
                }
                //else skip displaying the node

            }
            wipeEdgeLines();
        }


    public void create_Button(Node button, int floor){
        int nodeX = button.getPosX();
        int nodeY = button.getPosY();
        boolean hidden = button.getIsHidden();
        boolean enabled = button.getEnabled();

        Node current = MapController.getInstance().getCollectionOfNodes().getNode(nodeX, nodeY, floor);
        final String infoString;
        infoString = "x: " + nodeX + " y: " + nodeY + " Floor: " + floor + "\n" +
                "Name: " + current.getName() + "\n" +
                "Room: " + current.getRoomNum() + "\n" +
                "Type: " + current.getType();

        location = new Circle(labelRadius);
        location.setOnMouseClicked(e -> {
            Object o = e.getSource();
            Circle c = (Circle) o;

            //only work for left click
            if (e.getButton() == MouseButton.PRIMARY) {
                sceneController.sceneEvent((int)((nodeX)), (int)((nodeY)), c);
            }

        });
        location.setOnMouseEntered(e -> {
            Object o = e.getSource();
            Circle c = (Circle) o;
            c.setRadius(labelRadius * sizeUpRatio);
            Tooltip.install(
                    c,
                    new Tooltip(infoString)
            );
        });
        location.setOnMouseExited(e -> {
            Object o = e.getSource();
            Circle c = (Circle) o;
            c.setRadius(labelRadius);
        });

        // this code sets node's x and y pos to be on the plane holding the graph
        currentPane.getChildren().add(location);
        location.setLayoutX(nodeX * zoom);
        location.setLayoutY(nodeY * zoom);
        location.toFront();

        if (!enabled) {
            location.setFill(Color.RED);
        } else if(hidden) {
            location.setFill(Color.GRAY);
        }else if (button.getName().equals("Kiosk")){
            System.out.println("Found Kiosk");
            location.setFill(Color.ORANGE);
        }

        location.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY){
//                    // Create ContextMenu
//                    ContextMenu contextMenu = new ContextMenu();
//
//                    MenuItem item1 = new MenuItem("Remove");
//                    MenuItem item2 = new MenuItem("Edit");
//                    // Add MenuItem to ContextMenu
//                    contextMenu.getItems().addAll(item1, item2);
//                    contextMenu.show(location, event.getScreenX(), event.getScreenY());
                    Object o = event.getSource();
                    Circle c = (Circle) o;
                    sceneController.rightClickEvent((int)((nodeX)), (int)((nodeY)), c);
                }
            }
        });

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
    public void createEdgeLines(ArrayList<controllers.Edge> edgeList, boolean highlighted, boolean devmode) {
        //for-each loop through arraylist
        wipeEdgeLines();
        for(controllers.Edge thisEdge: edgeList) {
            lne = new Line();

            //don't draw lines if on different floor
            if (thisEdge.getStartNode().getFloor() != thisEdge.getEndNode().getFloor()) {
                continue;
                //maybe use colors???
            }

            if (devmode) {
                lne.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        Object o = e.getSource();
                        Line lne = (Line) o;
                        //sceneController.EdgeEvent(lne.getStartX(), lne.getStartY());
                        sceneController.edgeClickRemove((int)lne.getStartX(), (int)lne.getStartY(), (int)lne.getEndX(), (int)lne.getEndY());
                    }
                });

                lne.setOnMouseEntered(e -> {
                    Object o = e.getSource();
                    Line lne = (Line) o;
                    if (highlighted) {
                        lne.setStrokeWidth(lineHighlightedStrokeW * lineSizeUpRatio);
                    } else {
                       lne.setStrokeWidth(lineNonHighlightedStrokeW * lineSizeUpRatio);
                    }
                });

                lne.setOnMouseExited(e -> {
                    Object o = e.getSource();
                    Line lne = (Line) o;
                    if (highlighted) {
                        lne.setStrokeWidth(lineHighlightedStrokeW );
                    } else {
                        lne.setStrokeWidth(lineNonHighlightedStrokeW);
                    }
                });
            }

            //config to display properly
            if(highlighted) {
                lne.setFill(Color.RED);
                lne.setStroke(Color.RED);
                lne.setStrokeWidth(lineHighlightedStrokeW);
            } else {
                lne.setStroke(Color.BLACK);
                lne.setStrokeWidth(lineNonHighlightedStrokeW);
            }

            //add to pane
            currentPane.getChildren().add(lne);
            lne.setStartX(thisEdge.getStartNode().getPosX() * zoom);
            lne.setStartY(thisEdge.getStartNode().getPosY() * zoom);
            lne.setEndX(thisEdge.getEndNode().getPosX() * zoom);
            lne.setEndY(thisEdge.getEndNode().getPosY() * zoom);
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

        createEdgeLines(currentFloorEdges, false, false);
    }

    public static double getZoom() {
        return zoom;
    }

    public static void setZoom(double zoom1) {
        zoom = zoom1;
    }

    public static int getPathfinding() {
        return pathfinding;
    }

    public static void setPathfinding(int newpath) {
        pathfinding = newpath;
    }
}
