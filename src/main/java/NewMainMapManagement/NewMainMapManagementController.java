package NewMainMapManagement;

import DBController.DatabaseController;
import controllers.*;
import controllers.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;
//import sun.misc.resources.Messages_pt_BR;

import javax.swing.text.View;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by AugustoR on 4/27/17.
 */
public class NewMainMapManagementController extends controllers.mapScene {
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private Button directoryManagement_Button;

    @FXML
    private Button adminManagement_Button;

    @FXML
    private Button signOut_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Label LogInPerson_Label;

    @FXML
    private StackPane mapStack;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane admin_FloorPane;

    @FXML
    private ImageView map_viewer;

    @FXML
    private Button zoomOut_button;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    @FXML
    private Button zoomIn_button;

    @FXML
    private Button clear_Button;

    @FXML
    private Button save_Button;


    private int nodeEdgeX1;
    private int nodeEdgeY1;
    private int nodeEdgeX2;
    private int nodeEdgeY2;

    private controllers.MapOverlay graph;

    private static final double labelRadius = 10.5;

    private Node firstNode;
    private Circle dragCircle;
    private Node dragNode;

    private Circle lastColoredStart;


    private int edgesSelected = 0;

    private boolean selectedNode;


    private Circle btK;
    private boolean addSingleEdgeMode;
    private boolean addMultiEdgeMode;
    private boolean dragMode;
    private boolean multiDragMode;
    private boolean popoverShown;

    private int permissionLevel;

    private final Circle[] temporaryButton = {null};
    private ArrayList<Circle> floatingCircles;
    private ArrayList<Node> floatingNodes;



    //Set to english by default
    private int c_language = 0;


    private int currentFloor;

    private DatabaseController databaseController = DatabaseController.getInstance();

    public void initialize() {
        setUserString(LogInPerson_Label.getText());
        addSingleEdgeMode = false;
        addMultiEdgeMode = false;
        popoverShown = false;

        selectedNode = false;

        //set default floor to start
        //we will use floor 1 for now
        currentFloor = 1;
        //set to admin level
        permissionLevel = 2;

        dragMode = false;

        graph = new controllers.MapOverlay(admin_FloorPane,(mapScene) this);
        MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true,
                currentFloor, permissionLevel);

        setFloorChoices();

        // creates a node when clicking the map
        map_viewer.setOnMouseClicked((MouseEvent e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                //clear on any selection stuff for the rest of the map
                addSingleEdgeMode = false;
                if (addMultiEdgeMode) {
                    //dont try to add node if just trying to click out of multi-edge selection
                    addMultiEdgeMode = false;
                } else if (dragMode) {
                    dragMode = false;
                    dragModeUpdate("SINGLE");
                } else if (selectedNode) {
                    selectedNode = false;
                    graph.wipeEdgeLines();
                    //color the node as well
                    if (lastColoredStart != null) {
                        lastColoredStart.setStroke(lastColoredStart.getFill());
                        lastColoredStart.setStrokeWidth(1);
                        lastColoredStart = null;
                    }
                } else if (popoverShown) {
                    popoverShown = false;
                    if (temporaryButton[0] != null && !databaseController.isActualLocation((int) temporaryButton[0].getLayoutX(), (int) temporaryButton[0].getLayoutY(), currentFloor)) {
                        admin_FloorPane.getChildren().remove(temporaryButton[0]);
                    }
                } else {
                    if (!multiDragMode) {
                        graph.wipeEdgeLines();

                        if (temporaryButton[0] != null && !databaseController.isActualLocation((int) temporaryButton[0].getLayoutX(), (int) temporaryButton[0].getLayoutY(), currentFloor)) {
                            admin_FloorPane.getChildren().remove(temporaryButton[0]);
                        }
                        btK = new Circle(labelRadius);//new Button();
                        btK.setLayoutX(e.getX());
                        btK.setLayoutY(e.getY());
                        admin_FloorPane.getChildren().add(btK);
                        temporaryButton[0] = btK;

                        //set the popovershown var
                        popoverShown = true;

                        PopOver pop = new PopOver();
                        createPop(pop, btK, "Create");
                        pop.show(btK);
                    }
                }
            } else {
                // show a context menu for clear and automatic edges radius
                ContextMenu contextMenu = new ContextMenu();
                contextMenu.setImpl_showRelativeToWindow(true);
                MenuItem clearOption = new MenuItem("Clear");
                clearOption.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent ee) {
                        clearButton_Clicked();
                    }
                });
                MenuItem radiusOption = new MenuItem("Edit Automatic Edges Radius");
                radiusOption.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent ee) {
                       // set the edge radius here
                        PopOver pop = new PopOver();
                        Circle tempCircle = new Circle(labelRadius);//new Button();
                        tempCircle.setLayoutX(e.getX());
                        tempCircle.setLayoutY(e.getY());
                        tempCircle.setVisible(false);
                        admin_FloorPane.getChildren().add(tempCircle);
                        createRadiusPop(pop, tempCircle);
                        pop.show(tempCircle);

                    }
                });
                MenuItem draggableOption = new MenuItem("Make nodes draggable");
                draggableOption.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent ee) {
                        // make nodes draggable here
                        if(!multiDragMode) {
                            clearButton_Clicked();
                            dragMode = false;
                            multiDragMode = true;
                            resetScreen();
                            unhookAllCircles();
                        }
                    }
                });
                contextMenu.getItems().addAll(clearOption, radiusOption, draggableOption);
                contextMenu.show(map_viewer, e.getScreenX(), e.getScreenY());

            }

        });
    }

    public PopOver createRadiusPop(PopOver pop, Circle tempCircle){

        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");
        TextField radiusField = new TextField();
        Label edgeRadiusLabel = new Label("Enter Automatic Edges Radius:");
        // get current edge radius and fill in the text box
        radiusField.setText("" + MapController.getInstance().getSurroundingRadius());

      // radiusField.setText(currentEdgeRadius);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 5, 10));

        VBox vb = new VBox();

        HBox hbCancelSave = new HBox();

        vb.setPadding(new Insets(10, 10, 5, 10));
        vb.setSpacing(10);

        hbCancelSave.setPadding(new Insets(0, 0, 0, 0));
        hbCancelSave.setSpacing(10);
        hbCancelSave.getChildren().addAll(buttonCancel, buttonSave);
        hbCancelSave.setAlignment(Pos.CENTER);

        vb.getChildren().addAll(edgeRadiusLabel, radiusField, hbCancelSave);
        anchorpane.getChildren().addAll(grid, vb);
        AnchorPane.setBottomAnchor(vb, 8.0);
        AnchorPane.setRightAnchor(vb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        pop.setDetachable(true);
        pop.setDetached(true);
        pop.setCornerRadius(4);
        pop.setContentNode(anchorpane);

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // don't do anything
                pop.hide();
                admin_FloorPane.getChildren().remove(tempCircle);
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // set the global radius here
                if (radiusField.getText() != null) {
                    MapController.getInstance().setSurroundingRadius(Double.parseDouble(radiusField.getText()));

                }

                pop.hide();
                resetScreen();
                admin_FloorPane.getChildren().remove(tempCircle);
            }
        });
        return pop;

    }
    public PopOver createMultiFloorPop(PopOver pop, Circle btK, ArrayList<Integer> floors, Node selectedNode) {

        ArrayList<Edge> deleteThese = new ArrayList<>();
        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 5, 10));

        VBox vb = new VBox();

        HBox hbCancelSave = new HBox();

        vb.setPadding(new Insets(10, 10, 5, 10));
        vb.setSpacing(10);

        hbCancelSave.setPadding(new Insets(0, 0, 0, 0));
        hbCancelSave.setSpacing(60);
        hbCancelSave.getChildren().addAll(buttonCancel, buttonSave);

        // give it a list of multifloor edges for this node
        // so it can parse through and get the floors it is connected to
        for (int f : floors) {
            // fields.add(new TextField(/*floor number parsed*/));
            TextField thisField = new TextField(Integer.toString(f));
            thisField.setAlignment(Pos.CENTER);
            thisField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals("")) {
                    // get node with these button coordinates
                    ArrayList<Edge> edges = selectedNode.getEdgeList();
                    for (Edge e : edges) {
                        if (e.getEndNode().getFloor() == Integer.parseInt(oldValue)) {
                            deleteThese.add(e);
                        }
                    }

                }
            });
            vb.getChildren().add(thisField);
        }

        vb.getChildren().addAll(hbCancelSave);
        anchorpane.getChildren().addAll(grid, vb);
        AnchorPane.setBottomAnchor(vb, 8.0);
        AnchorPane.setRightAnchor(vb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        pop.setDetachable(true);
        pop.setDetached(false);
        pop.setCornerRadius(4);
        pop.setContentNode(anchorpane);

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pop.hide();
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (Edge ed : deleteThese) {
                    databaseController.deleteEdge(ed.getStartNode().getPosX(), ed.getStartNode().getPosY(),
                            ed.getStartNode().getFloor(), ed.getEndNode().getPosX(), ed.getEndNode().getPosY(),
                            ed.getEndNode().getFloor());
                }
                pop.hide();
                resetScreen();
            }
        });
        return pop;

    }

    private void dragModeUpdate(String mode) {
        graph.wipeEdgeLines();
        if (dragNode != null) {
            System.out.println("drag done");
            System.out.println("old: x= " + dragNode.getPosX() + ", y= " + dragNode.getPosY());
            System.out.println("new: x= " + dragCircle.getLayoutX() + ", y= " + dragCircle.getLayoutY());
            System.out.println("---");
            ArrayList<Node> neighborlist = new ArrayList<>();

            //need to see if actually moved it though.
            if (dragNode.getPosX() != dragCircle.getLayoutX() || dragNode.getPosY() != dragCircle.getLayoutY() ||
                    dragNode.getFloor() != currentFloor) {

                for (controllers.Edge thisEdge : dragNode.getEdgeList()) {
                    Node temp = thisEdge.getNeighbor(dragNode);
                    neighborlist.add(temp);
                    temp.getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }

                databaseController.newNode((int) dragCircle.getLayoutX(), (int) dragCircle.getLayoutY(), currentFloor, dragNode.getIsHidden(),
                        dragNode.getEnabled(), dragNode.getType(), dragNode.getName(),
                        "SOFTENGWPIsjijflkjjfjjfklaljjjfalkjooejallajjjflijjfflRyanIsAwesome",
                        dragNode.getPermissionLevel());

                databaseController.transferNodeLoc(dragNode.getPosX(), dragNode.getPosY(), dragNode.getFloor(),
                        (int) dragCircle.getLayoutX(), (int) dragCircle.getLayoutY(), currentFloor);

                databaseController.deleteNode(dragNode.getPosX(), dragNode.getPosY(), currentFloor);

                databaseController.updateNode((int) dragCircle.getLayoutX(), (int) dragCircle.getLayoutY(), currentFloor, dragNode.getIsHidden(),
                        dragNode.getEnabled(), dragNode.getType(), dragNode.getName(),
                        dragNode.getRoomNum(), dragNode.getPermissionLevel());

                //add the edges to the new node
                for (Node n : neighborlist) {
                    DatabaseController.getInstance().newEdge((int) dragCircle.getLayoutX(),
                            (int) dragCircle.getLayoutY(), currentFloor,
                            n.getPosX(), n.getPosY(), n.getFloor());
                }
            }
            if (mode.equalsIgnoreCase("SINGLE")) {
                resetScreen();
            }

            //if successful, highlight node and show edges
            //TODO >??
        }
    }

    public PopOver createPop(PopOver pop, Circle btK, String mode){

        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");
        Label nameLabel = new Label("Name");
        Label typeLabel = new Label("Type");
        Label roomLabel = new Label("Room Number");
        Label permissionLabel = new Label("Permission");
        ChoiceBox permissionBox = new ChoiceBox(FXCollections.observableArrayList(
                "Visitor", "Employee", "Admin"));
        TextField nodeName = new TextField();
        TextField nodeType = new TextField();
        ArrayList<String> types;
        types = databaseController.getNodeTypes();
        TextFields.bindAutoCompletion(nodeType, types);
        TextField nodeRoom = new TextField();
        CheckBox isHidden = new CheckBox("Hidden");
        CheckBox isEnabled = new CheckBox("Enabled");
        isHidden.setSelected(false);
        isEnabled.setSelected(true);
        nodeName.setPromptText("Name");
        nodeType.setPromptText("Type");
        nodeRoom.setPromptText("Room Number");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 5, 10));

        VBox vb = new VBox();
        HBox hbCancelSave = new HBox();
        HBox hbCheckBox = new HBox();
        HBox hbPermission = new HBox();
        vb.setPadding(new Insets(10, 10, 5, 10));
        vb.setSpacing(10);
        hbCancelSave.setPadding(new Insets(0, 0, 0, 0));
        hbCancelSave.setSpacing(60);
        hbCancelSave.getChildren().addAll(buttonCancel, buttonSave);
        hbCheckBox.getChildren().addAll(isHidden, isEnabled);
        hbCheckBox.setSpacing(25);
        hbPermission.setPadding(new Insets(0, 0, 0, 0));
        hbPermission.getChildren().addAll(permissionLabel, permissionBox);
        hbPermission.setSpacing(10);

        vb.getChildren().addAll(nameLabel, nodeName,
                typeLabel, nodeType, roomLabel, nodeRoom, hbCheckBox,
                hbPermission, hbCancelSave);
        anchorpane.getChildren().addAll(grid,vb);   // Add grid from Example 1-5
        AnchorPane.setBottomAnchor(vb, 8.0);
        AnchorPane.setRightAnchor(vb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        if (mode.equals("Edit")){
            ResultSet rset = databaseController.getNode((int) btK.getLayoutX(), (int) btK.getLayoutY(), currentFloor);
            try {
                while (rset.next()){
                    nodeName.setText(rset.getString("NAME"));
                    nodeType.setText(rset.getString("TYPE"));
                    nodeRoom.setText(rset.getString("ROOMNUM"));
                    if (rset.getInt("PERMISSIONS") == 0){
                        permissionBox.getSelectionModel().select(0);
                    } else if (rset.getInt("PERMISSIONS") == 1){
                        permissionBox.getSelectionModel().select(1);
                    } else {
                        permissionBox.getSelectionModel().select(2);
                    }
                    if(rset.getBoolean("ISHIDDEN")){
                        isHidden.setSelected(true);
                    }
                    if (rset.getBoolean("ENABLED")){
                        isEnabled.setSelected(true);
                    }
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }


        pop.setDetachable(true);
        pop.setDetached(false);
        pop.setCornerRadius(4);
        pop.setContentNode(anchorpane);

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String thisNodeName = nodeName.getText();
                String thisNodeType = nodeType.getText();
                String thisNodeRoom = nodeRoom.getText();
                if (!thisNodeName.equals("") && !thisNodeType.equals("") && !thisNodeRoom.equals("")) {
                    int permission;
                    if (permissionBox.getValue() == null) {
                        //nothing selected
                        permission = 0;
                    } else {
                        if (permissionBox.getValue().toString().equals("Admin")) {
                            permission = 2;
                        } else if (permissionBox.getValue().toString().equals("Employee")) {
                            permission = 1;
                        } else {
                            permission = 0;
                        }
                    }
                    if (mode.equals("Edit")) {
                        DBController.DatabaseController.getInstance().updateNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                                currentFloor, isHidden.isSelected(), isEnabled.isSelected(), thisNodeType, thisNodeName, thisNodeRoom, permission);
                        pop.hide();
                        admin_FloorPane.getChildren().remove(btK);
                        resetScreen();
                    } else if (mode.equals("Create")){
                        DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                                currentFloor, isHidden.isSelected(), isEnabled.isSelected(), thisNodeType, thisNodeName, thisNodeRoom, permission);
                        pop.hide();
                        admin_FloorPane.getChildren().remove(btK);
                        resetScreen();
                    }
                }
            }
        });

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pop.hide();
                if (mode.equals("Create")) {
                    admin_FloorPane.getChildren().remove(btK);
                }
                popoverShown = false;
            }
        });

        return pop;
    }

    //requeries database and resets screen
    public void resetScreen() {
        controllers.MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), true,
                currentFloor, permissionLevel);
        graph.wipeEdgeLines();
        edgesSelected = 0;

    }

    public void rightClickEvent(int x, int y, Circle c, int mode) {
        //CODE TO HANDLE RIGHT CLICK MENU STUFF GOES HERE:
        switch (mode) {
            case 1: // remove node
                Node selectedNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                //handle errors
                if (selectedNode == null) {
                    break;
                }
                for (controllers.Edge thisEdge : selectedNode.getEdgeList()) {
                    thisEdge.getNeighbor(selectedNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                databaseController.deleteNode(x, y, currentFloor);
                System.out.println("deleting node...");

                resetScreen();
                break;
            case 2:  // edit node
                PopOver pop = new PopOver();
                createPop(pop, c, "Edit");
                pop.show(c);
                break;
            case 3:
                MapController.getInstance().attachSurroundingNodes(x, y, currentFloor);
                resetScreen();
                //show some edge lines as visual feedback:
                Node temp = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                graph.createEdgeLines(temp.getEdgeList(), true, true);
                break;
            case 4:
                addSingleEdgeMode = true;

                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                        .getNode(x, y, currentFloor);
                if (firstNode == null) {
                    System.out.println("RIP trying to get node");
                    resetScreen();
                    clearButton_Clicked();
                    break;
                }
                graph.createEdgeLines(firstNode.getEdgeList(), true, false);

                break;
            case 5:
                addMultiEdgeMode = true;
                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                        .getNode(x, y, currentFloor);
                if (firstNode == null) {
                    System.out.println("RIP trying to get node");
                    resetScreen();
                    clearButton_Clicked();
                    break;
                }
                graph.createEdgeLines(firstNode.getEdgeList(), true, false);
                break;
            case 6:
                Node thisNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                //handle errors
                if (thisNode == null) {
                    break;
                }
                for (controllers.Edge thisEdge : thisNode.getEdgeList()) {
                    thisEdge.getNeighbor(thisNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                resetScreen();
                break;
            case 7:
                //draggable code:
                System.out.println("draggable");
                dragMode = true;
                final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());
                dragCircle = c;
                dragNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                if (dragNode == null) {
                    System.out.println("ERROR GETTING DRAG NODE");
                    break;
                }
                System.out.println("test old: x= " + dragNode.getPosX() + ", y= " + dragNode.getPosY());
                //wipe edge lines from screen to not clutter stuff up
                graph.wipeEdgeLines();
                //This code is for placing nodes
                c.setOnMouseDragged(e -> {
                    if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                            && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                        c.setLayoutX((e.getSceneX() - paneBounds.getMinX()));
                        c.setLayoutY((e.getSceneY() - paneBounds.getMinY()));
                    }
                });
                break;
            case 8: //  click on stair/elevator node
                Node selectedNode2 = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                //handle errors
                if (selectedNode2 == null) {
                    break;
                }
                ArrayList<Edge> edges = selectedNode2.getEdgeList();
                ArrayList<Integer> floors = new ArrayList<>();
                for (Edge e : edges){
                    int floor = e.getNeighbor(selectedNode2).getFloor();
                    if ((floor != selectedNode2.getFloor()) && (!floors.contains(floor)) ){
                        floors.add(floor);
                    }
                }
                PopOver pop2 = new PopOver();
                createMultiFloorPop(pop2, c, floors, selectedNode2);
                pop2.show(c);
                break;
            case 9:
                //toggle enabled
                Node eNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                if (eNode == null) {
                    break;
                }
                databaseController.updateNode(x, y, currentFloor, eNode.getIsHidden(), !(eNode.getEnabled()), eNode.getType(),
                        eNode.getName(), eNode.getRoomNum(), eNode.getPermissionLevel());
                resetScreen();
                break;
            case 10:
                //toggle hidden
                Node hNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                if (hNode == null) {
                    break;
                }
                databaseController.updateNode(x, y, currentFloor, !(hNode.getIsHidden()), hNode.getEnabled(), hNode.getType(),
                        hNode.getName(), hNode.getRoomNum(), hNode.getPermissionLevel());
                resetScreen();
                break;
            default:
                System.out.println("default. This probably should not have been possible...");
                break;
        }
    }

    //handle a click on an edge.
    public void edgeClickRemove(int x1, int y1, int x2, int y2){
        DBController.DatabaseController.getInstance().deleteEdge(x1,
                y1, currentFloor, x2, y2, currentFloor);
        System.out.println("removed edge on click");
        resetScreen();
        if (firstNode != null) {
            Node temp = MapController.getInstance().getCollectionOfNodes().getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
            graph.createEdgeLines(temp.getEdgeList(), true, true);
        }
    }

    public void sceneEvent(int x, int y, Circle c) {

        if (multiDragMode) {
            System.out.println("clicked on a draggable node");
        } else {

            //add edge from menu (both multi and single)
            if (addSingleEdgeMode || addMultiEdgeMode) {
                System.out.println("adding edge...");
                nodeEdgeX2 = (int) x;
                nodeEdgeY2 = (int) y;
                DBController.DatabaseController.getInstance().newEdge(firstNode.getPosX(),
                        firstNode.getPosY(), firstNode.getFloor(), nodeEdgeX2, nodeEdgeY2, currentFloor);
                resetScreen();
                addSingleEdgeMode = false;
                if (firstNode != null) {
                    firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                            .getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
                    //don't know if above method is successful
                    //must check again if firstNode is not null
                    if (firstNode != null) {
                        graph.createEdgeLines(firstNode.getEdgeList(), true, true);
                        c.toFront();
                    }
                }

                return;
            }

            //don't highlight if in drag mode
            if (dragMode) {
                return;
            }
            //set the selected node switch
            selectedNode = true;

            //remove any temporary nodes
            if (popoverShown) {
                if (temporaryButton[0] != null && !databaseController.isActualLocation((int) temporaryButton[0].getLayoutX(), (int) temporaryButton[0].getLayoutY(), currentFloor)) {
                    admin_FloorPane.getChildren().remove(temporaryButton[0]);
                }

            }
            //highlight the node
            nodeEdgeX1 = (int) x;
            nodeEdgeY1 = (int) y;
            System.out.println(nodeEdgeX1 + "     " + nodeEdgeY1);
            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                    .getNode(nodeEdgeX1, nodeEdgeY1, currentFloor);
            graph.createEdgeLines(firstNode.getEdgeList(), true, true);

            //color the node as well
            if (lastColoredStart != null) {
                lastColoredStart.setStroke(lastColoredStart.getFill());
                lastColoredStart.setStrokeWidth(1);
                lastColoredStart = null;
            }
            lastColoredStart = c;
            c.setStrokeWidth(6.5);
            c.setStroke(Color.ROYALBLUE);
            c.toFront();

        }
    }

    //Change to main Menu
    public void mainMenuButton_Clicked() {

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(LogInPerson_Label.getText());
        System.out.println(LogInPerson_Label.getText());

        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        controller.setLanguageChoices();
    }

    public void setUserString(String user) {

        LogInPerson_Label.setText(user);
    }


    //Sets the map of the desired floor
    public void setFloorChoices(){
        if(c_language == 0) {
            floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Outside",
                    "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Belkin Basement");
        }else{
            floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Afuera",
                    "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Sotano de Belkin");
        }

        //reset ui interaction
        dragMode = false;
        popoverShown = false;
        selectedNode = false;


        floor_ChoiceBox.getSelectionModel().select(0);
        map_viewer.setImage(new Image("/images/cleaned1.png"));
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                boolean outside = false;
                String currentF = "";


                if (newValue.intValue() == 7) {
                    //outside
                    currentFloor = 0;
                } else if(newValue.intValue() > 7) {
                    currentFloor = newValue.intValue();
                } else {
                    currentFloor = newValue.intValue() + 1;
                }

                mapImage newMapImage = new proxyMap(currentFloor);
                newMapImage.display(map_viewer);

                if(!outside) {
                    //c_Floor_Label.setText(Integer.toString(currentFloor));
                }else{
                    //c_Floor_Label.setText("");
                    //floor_Label.setText(currentF);
                }
                //true ot see nodes false otherwise
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true,
                        currentFloor, permissionLevel);
            }
        });

    }

    //sets the current language given information form other screens
    public void setC_language(int i){
        c_language = i;
    }

    //Signs the user out
    public void signOutButton_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        //patientMenuStart.patientMenuStartController controller = loader.getController();
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
        controller.englishButtons_Labels();
        //set up spanish labels
    }else if(c_language == 1){
        controller.spanishButtons_Labels();
    }
        //set permissions back
        controller.setPermissionLevel(0);
        controller.loginOrOut(1,c_language);
        //set label to empty
        controller.setWelcome("");
    }

    //Manage when the directory button is clicked
    public void DirectoryManButton_Clicked(){
        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/NewDirectoryManagementView.fxml");
        mapManagementNodeInformation.mmNodeInformationController controller = loader.getController();

        //sets the current language
        controller.setC_language(c_language);

        controller.setModeChoices();
        controller.setRoomChoices();
        controller.setUpTreeView();
        controller.setUser(LogInPerson_Label.getText());

        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        //Set permissions of admin
        controller.setPermissionLevel(2);


    }

    //Manages when the admin management button is clicked
    public void AdminManButton_Clicked(){


    }

    //Manages when the emergency button is clicked
    public void emergencyButton_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
        emergency.emergencyController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
    }

    //Manages when the user clicks the save button
    public void saveButton_Clicked(){
        if (multiDragMode) {
            multiDragMode = false;

            if (floatingNodes.size() != floatingCircles.size()) {
                System.out.println("something got really messed up, the " +
                        "list sizes are different");
                resetScreen();
                return;
            }
            ;
            while (!floatingCircles.isEmpty() && !floatingNodes.isEmpty()) {
                dragCircle = floatingCircles.remove(0);
                dragNode = floatingNodes.remove(0);
                dragModeUpdate("MULTI");
            }
            System.out.println("finished.");
            resetScreen();
        } else {
            dragMode = false;
            dragModeUpdate("SINGLE");
        }
    }

    //Manages when the user clicks the clear button
    public void clearButton_Clicked(){

        graph.wipeEdgeLines();
        edgesSelected = 0;

        addSingleEdgeMode = false;
        addMultiEdgeMode = false;
        popoverShown = false;
        selectedNode = false;
        admin_FloorPane.getChildren().remove(temporaryButton[0]);

        //reset last colored stroke to default
        if (lastColoredStart != null) {
            lastColoredStart.setStroke(lastColoredStart.getFill());
            lastColoredStart.setStrokeWidth(1);
        }

    }


    //Zooms the map in
    public void zoomInButton_Clicked() {

    }

    //Zooms out the map
    public void zoomOutButton_Clicked() {

    }

    //Labels for english
    public void englishButtons_Labels(){
        setC_language(0);
        //Labels
        mainTitle_Label.setText("Map Management Tool");

        //Buttons
        directoryManagement_Button.setText("Directory Management");
        adminManagement_Button.setText("Admin Management");
        signOut_Button.setText("Sign Out");
        emergency_Button.setText("EMERGENCY");
        clear_Button.setText("Clear");
        save_Button.setText("Save");

        //Choice Box
        setFloorChoices();

    }
    //Labels for spanish
    public void spanishButtons_Labels(){
        setC_language(1);
        //Labels
        System.out.println("FUCKING BITCHES EN ESPANOL");

        mainTitle_Label.setText("Control de Mapas");

        //Buttons
        directoryManagement_Button.setText("Control del Directorio");
        adminManagement_Button.setText("Control de Admins");
        signOut_Button.setText("Salir");
        emergency_Button.setText("EMERGENCIA");
        clear_Button.setText("Borrar");
        save_Button.setText("Guardar");

        //Choice Box
        setFloorChoices();

    }

    public void setUser(String user){
        LogInPerson_Label.setText(user);

    }

    //Gets the permissions
    public int getPermissionLevel() {
        return permissionLevel;
    }

    //Sets the permissions
    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
        System.out.println("Setting permission level to: " + permissionLevel);

    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }


    public void unhookAllCircles() {
        floatingCircles = new ArrayList<>();
        floatingNodes = new ArrayList<>();
        ObservableList<javafx.scene.Node> sceneObjects = admin_FloorPane.getChildren();

        for (javafx.scene.Node n: sceneObjects) {
            if (n instanceof Circle) {
                floatingCircles.add((Circle) n);
            }
        }

        //set all circles to be floating
        for (Circle c: floatingCircles) {
            final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());
            dragCircle = c;
            dragNode = MapController.getInstance().getCollectionOfNodes().getNode((int) c.getLayoutX(), (int) c.getLayoutY(), currentFloor);
            if (dragNode == null) {
                System.out.println("ERROR GETTING DRAG NODE");
                continue;
            }
            //System.out.println("test old: x= " + dragNode.getPosX() + ", y= " + dragNode.getPosY());
            //This code is for placing nodes
            c.setOnMouseDragged(e -> {
                if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                        && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                    c.setLayoutX((e.getSceneX() - paneBounds.getMinX()));
                    c.setLayoutY((e.getSceneY() - paneBounds.getMinY()));
                }
            });

            floatingNodes.add(dragNode);
        }
    }

}
