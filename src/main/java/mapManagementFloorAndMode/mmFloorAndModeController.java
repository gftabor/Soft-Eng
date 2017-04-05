package mapManagementFloorAndMode;

import controllers.Edge;
import controllers.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AugustoR on 3/31/17.
 */
public class mmFloorAndModeController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button emergency_Button;

    @FXML
    private ChoiceBox<String> mode_ChoiceBox;

    @FXML
    private ChoiceBox<String> title_ChoiceBox;

    @FXML
    private CheckBox hidden_CheckBox;

    @FXML
    private Label username_Label;

    @FXML
    private Button submit_Button;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button clear_Button;

    //Add the name and Room
    @FXML
    private TextField name_TextField;

    @FXML
    private TextField room_TextField;

    @FXML
    private Pane admin_FloorPane;

    HashMap<Integer, Node> DBMap;

    private ArrayList<Circle> nodeList = new ArrayList();

    private ArrayList<Line> lineList = new ArrayList();

    private Line lne;

    private int selectNodeX;
    private int selectNodeY;

    private int nodeEdgeX1;
    private int nodeEdgeY1;
    private int nodeEdgeX2;
    private int nodeEdgeY2;

    private int edgesSelected = 0;

    private static final double lableRadius = 8.5;

    private Node firstNode;

    private Circle btK;
    public void emergencyButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    public void clearButton_Clicked() {
        //if(mode_ChoiceBox.getValue().equals("Add Node")) {
        if("Add Node".equals(mode_ChoiceBox.getValue())) {
            admin_FloorPane.getChildren().remove(btK);
        }

        edgesSelected = 0;
    }

    private void nodeChosen(double x, double y, int floor){
        edgesSelected++;
        if (edgesSelected == 1){
            //display edges already associated with selected node
            nodeEdgeX1 = (int) x;
            nodeEdgeY1 = (int) y;
            System.out.println(nodeEdgeX1 + "     "+ nodeEdgeY1);
            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                    .getNode(nodeEdgeX1, nodeEdgeY1, floor);
            createEdgeLines(firstNode.getEdgeList());
        }
        if (edgesSelected == 2) {
            //create edge between the two nodes
            nodeEdgeX2 = (int) x;
            nodeEdgeY2 = (int) y;
        }
    }

    //submit button is clicked
    //To Do - use this to send the information of your changes to the DB to get updated
    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit Button");
        System.out.println(mode_ChoiceBox.getValue());
        final String tempName = name_TextField.getText();
        final String tempRoom = room_TextField.getText();
        final int floor = 4;
        final String name = "Mark?";


        switch(mode_ChoiceBox.getValue()) {
            case "---":
                System.out.println("Mode = default");

                break;
            case "Add Node":

                if(!(title_ChoiceBox.getValue().equals("")) &&
                        !(name_TextField.getText().equals("")) &&
                        !(room_TextField.getText().equals(""))) {

                    System.out.println("Mode = add");
                    Node newNode = new Node((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                            hidden_CheckBox.isSelected(), true, name, floor);
                    DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                            floor, hidden_CheckBox.isSelected(), true, "Doctor", tempName, tempRoom);

                    Circle newButton = new Circle(lableRadius);
                    newButton.setLayoutX(newNode.getPosX());
                    newButton.setLayoutY(newNode.getPosY());

                    nodeList.add(newButton);
                    newButton.setOnMouseClicked(e -> {
                        if (mode_ChoiceBox.getValue().equals( "Add Edge")) {
                            nodeChosen(newButton.getLayoutX(),newButton.getLayoutY(),4);
                        }
                        if (mode_ChoiceBox.getValue().equals( "Remove Node")) {
                            nodeChosen(newButton.getLayoutX(),newButton.getLayoutY(),4);
                        }

                    });

                    admin_FloorPane.getChildren().add(newButton);
                    newButton.toFront();

                    mode_ChoiceBox.getSelectionModel().select("---");
                    break;
                }

            case "Edit Node":
                System.out.println("Mode = edit node");
                break;
            case "Remove Node":
                System.out.println("Mode = remove node");
                for(controllers.Edge thisEdge : firstNode.getEdgeList()) {
                    thisEdge.getNeighbor(firstNode).getEdgeList().remove(thisEdge);
                }
                DBController.DatabaseController.getInstance().deleteNode(firstNode.getPosX(),firstNode.getPosY(),4);
                break;
            case "Add Edge":
                if (edgesSelected == 2) {
                    System.out.println("Mode = add edge");
                    DBController.DatabaseController.getInstance().newEdge(nodeEdgeX1,
                            nodeEdgeY1, 4, nodeEdgeX2, nodeEdgeY2, 4);
                    System.out.println("added edge");
                }
                break;
            case "Remove Edge":
                System.out.println("Mode = remove edge");
                break;
            default:
                System.out.println("Nothing selected for mode");
                break;
        }
        controllers.MapController.getInstance().requestMapCopy();
        setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(4));

        edgesSelected = 0;

    }

    //Change to main Menu
    public void mainMenuButton_Clicked(){

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(username_Label.getText());
    }

    public void setUserString(String user){username_Label.setText(user); }

    public void setModeChoices() {
        mode_ChoiceBox.getItems().addAll("---", "Add Node", "Remove Node", "Edit Node", "Add Edge", "Remove Edge");
        mode_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        // Do validation
                        clearButton_Clicked();
                        System.out.println(newValue);
                        if(newValue.intValue()==1){
                            create_Button();
                        }
                        
                    }
                });
    }

    public void setTitleChoices(){
        title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom");
    }

    public void create_Button(){
        System.out.println("checking button");
            System.out.println("make button");
            btK = new Circle(lableRadius);//new Button();
            // this code drags the button
            final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());

            //This code is for placing nodes
            btK.setOnMouseDragged(e -> {
                if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                        && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                    btK.setLayoutX((e.getSceneX() - paneBounds.getMinX()));
                    btK.setLayoutY((e.getSceneY() - paneBounds.getMinY()));
                }
            });

            //Clicking on Nodes added to the Map
            btK.setOnMouseClicked(e -> {
                        if (mode_ChoiceBox.getValue().equals("Add Edge")) {
                            System.out.println("attempting to add edge");
                            edgesSelected++;

                            if (edgesSelected == 1) {
                                //display edges already associated with selected node
                                nodeEdgeX1 = (int) btK.getLayoutX();
                                nodeEdgeY1 = (int) btK.getLayoutY();

                                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                                        .getNode(nodeEdgeX1, nodeEdgeY1, 4);

                                createEdgeLines(firstNode.getEdgeList());
                            }
                        }
                    });



            admin_FloorPane.getChildren().add(btK);
            btK.toFront();

    }
    //creates visual representations of the edges of nodes on the pane
    //  input: any arraylist of Edge objects
    //NOTE: caller is responsible for not sending duplicate edges
    public void createEdgeLines(ArrayList<controllers.Edge> edgeList) {
        //for-each loop through arraylist
        for(Line currentLine : lineList) {
            admin_FloorPane.getChildren().remove(currentLine);
        }
        for(controllers.Edge thisEdge: edgeList) {
            lne = new Line();

            //add to pane
            admin_FloorPane.getChildren().add(lne);
            //set positioning
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

    public void setMapAndNodes(HashMap<Integer, Node> nodeMap){
        int currentKey;
        for( Circle current : nodeList){
            admin_FloorPane.getChildren().remove(current);
        }
        for(controllers.Node current: nodeMap.values()){
            place_Old_Buttons(current.getPosX(), current.getPosY(), current.getIsHidden(), current.getEnabled());
        }
    }

    public void place_Old_Buttons(double nodeX, double nodeY, boolean hidden, boolean enabled){
        System.out.println("checking button");
        System.out.println("make button");
        btK = new Circle(lableRadius);//new Button("node");

        btK.setOnMouseClicked(e -> {
            nodeChosen(nodeX, nodeY, 4);
        });

        // this code sets node's x and y pos to be on the plane holding the graph
        admin_FloorPane.getChildren().add(btK);
        btK.setLayoutX(nodeX);
        btK.setLayoutY(nodeY);

        //change color if required
        //hidden -> grey
        //disabled -> dark red
        if (enabled == false) {
            btK.setFill(Color.DARKRED);
        } else if (hidden == true) {
            btK.setFill(Color.GRAY);
        }

        btK.toFront();

        //copy functionality of other btKs except for placement

        nodeList.add(btK);
    }

}
