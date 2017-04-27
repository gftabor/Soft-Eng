package mapManagementFloorAndMode;

import DBController.DatabaseController;
import controllers.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;

import javax.xml.soap.Text;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by AugustoR on 3/31/17.
 */
public class mmFloorAndModeController extends controllers.mapScene{
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

    @FXML
    private CheckBox enabled_CheckBox;

    @FXML
    private Label title_Label;

    @FXML
    private Label subTitile_Label;

    @FXML
    private Label nodeTitile_Label;

    @FXML
    private ImageView map_viewer;

    @FXML
    private Label name_Label;

    @FXML
    private Label room_Label;

    @FXML
    private Label hidden_Label;

    @FXML
    private Label enabled_Label;

    @FXML
    private Label floorMap_Label;

    @FXML
    private Label chooseFloor_Label;

    @FXML
    private Label mode_Label;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    private int nodeEdgeX1;
    private int nodeEdgeY1;
    private int nodeEdgeX2;
    private int nodeEdgeY2;

    private int edgesSelected = 0;

    private controllers.MapOverlay graph;

    private static final double labelRadius = 10.5;

    private Node firstNode;
    Circle dragCircle;
    Node dragNode;

    private Circle lastColoredStart;
    private Circle lastColoredEnd;

    private Circle btK;
    private boolean addSingleEdgeMode;
    private boolean addMultiEdgeMode;

    private int permissionLevel;


    //Set to english by default
    int c_language = 0;

    private int currentFloor;
    private int floor1;
    private int floor2;

    DatabaseController databaseController = DatabaseController.getInstance();

    public void initialize() {
        setUserString(username_Label.getText());
        setModeChoices();
        setTitleChoices();
        addSingleEdgeMode = false;
        addMultiEdgeMode = false;
        final Circle[] temporaryButton = {null};

        //set default floor to start
        //we will use floor 1 for now
        currentFloor = 1;
        //set to admin level
        permissionLevel = 2;

        graph = new controllers.MapOverlay(admin_FloorPane,(mapScene) this);
        MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true,
                currentFloor, permissionLevel);

        setFloorChoices();

        // creates a node when clicking the map
        map_viewer.setOnMouseClicked((MouseEvent e) -> {
            //clear on any selection stuff for the rest of the map
            addSingleEdgeMode = false;
            if (addMultiEdgeMode) {
                //dont try to add node if just trying to click out of multi-edge selection
                addMultiEdgeMode = false;
            } else {
                graph.wipeEdgeLines();

                if (temporaryButton[0] != null && !databaseController.isActualLocation((int) temporaryButton[0].getLayoutX(), (int) temporaryButton[0].getLayoutY(), currentFloor)){
                    admin_FloorPane.getChildren().remove(temporaryButton[0]);
                }
                btK = new Circle(labelRadius);//new Button();
                btK.setLayoutX(e.getX());
                btK.setLayoutY(e.getY());
                admin_FloorPane.getChildren().add(btK);
                temporaryButton[0] = btK;

                PopOver pop = new PopOver();
                createPop(pop, btK, "Create");
                pop.show(btK);
            }
        });
    }

    public PopOver createMultiFloorPop(PopOver pop, Circle btK, ArrayList<Integer> floors, Node selectedNode){

        //ArrayList<TextField> fields = new ArrayList<>();
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

        //
        // give it a list of multifloor edges for this node
        // so it can parse through and get the floors it is connected to
        for (int f : floors){
           // fields.add(new TextField(/*floor number parsed*/));
            TextField thisField = new TextField(Integer.toString(f));
            thisField.setAlignment(Pos.CENTER);
            thisField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals("")){
                    // get node with these button coordinates
                    ArrayList<Edge> edges = selectedNode.getEdgeList();
                    for (Edge e : edges){
                        if (e.getEndNode().getFloor() == Integer.parseInt(oldValue)){
                            databaseController.deleteEdge(e.getStartNode().getPosX(), e.getStartNode().getPosY(),
                                    e.getStartNode().getFloor(), e.getEndNode().getPosX(), e.getEndNode().getPosY(),
                                    e.getEndNode().getFloor());
                        }
                    }

                }

                // else here if you can create edges just by user number input RYAN
            });
            vb.getChildren().add(thisField);
        }

        vb.getChildren().addAll(hbCancelSave);
        anchorpane.getChildren().addAll(grid,vb);
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

        TextField textField = new TextField();


        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                pop.hide();
                resetScreen();
            }

        });


        return pop;

    }

    public PopOver createPop(PopOver pop, Circle btK, String mode){

        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");
        Label nameLabel = new Label("Name");
        Label typeLabel = new Label("Type");
        Label roomLabel = new Label("Room Number");
        TextField nodeName = new TextField();
        TextField nodeType = new TextField();
        ArrayList<String> types = new ArrayList<>();
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
        vb.setPadding(new Insets(10, 10, 5, 10));
        vb.setSpacing(10);
        hbCancelSave.setPadding(new Insets(0, 0, 0, 0));
        hbCancelSave.setSpacing(60);
        hbCancelSave.getChildren().addAll(buttonCancel, buttonSave);
        hbCheckBox.getChildren().addAll(isHidden, isEnabled);
        hbCheckBox.setSpacing(25);

        vb.getChildren().addAll(nameLabel, nodeName,
                typeLabel, nodeType, roomLabel, nodeRoom, hbCheckBox, hbCancelSave);
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
                    if (mode.equals("Edit")) {
                        DBController.DatabaseController.getInstance().updateNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                                currentFloor, isHidden.isSelected(), isEnabled.isSelected(), thisNodeType, thisNodeName, thisNodeRoom, 0);
                        pop.hide();
                        admin_FloorPane.getChildren().remove(btK);
                        resetScreen();
                    } else if (mode.equals("Create")){
                        Node newNode = new Node((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                                currentFloor, isHidden.isSelected(), isEnabled.isSelected(), thisNodeType, thisNodeName, thisNodeRoom, 0);
                        DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                                currentFloor, isHidden.isSelected(), isEnabled.isSelected(), thisNodeType, thisNodeName, thisNodeRoom, 0);
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
            }
        });

        return pop;
    }

    public void emergencyButton_Clicked() {

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



    public void clearButton_Clicked() {
        //if(mode_ChoiceBox.getValue().equals("Add Node")) {
        if ("Add Node".equals(mode_ChoiceBox.getValue())) {
            admin_FloorPane.getChildren().remove(btK);
        }

        name_TextField.clear();
        room_TextField.clear();
        graph.wipeEdgeLines();
        edgesSelected = 0;

        addSingleEdgeMode = false;
        addMultiEdgeMode = false;

        //reset last colored stroke to default
        if (lastColoredStart != null) {
            lastColoredStart.setStroke(lastColoredStart.getFill());
            lastColoredStart.setStrokeWidth(1);
        }

        if (lastColoredEnd != null) {
            lastColoredEnd.setStroke(lastColoredEnd.getFill());
            lastColoredEnd.setStrokeWidth(1);
        }
    }

    //requeries database and resets screen
    public void resetScreen() {
        controllers.MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), true,
                currentFloor, permissionLevel);
        graph.wipeEdgeLines();
        edgesSelected = 0;

    }

    public void doubleClickEvent(int x, int y, Circle c, int mode) {
        // delete this later
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
                final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());
                dragCircle = c;
                dragNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                if (dragNode == null) {
                    System.out.println("fuck");
                    break;
                }
                System.out.println("test old: x= " + dragNode.getPosX() + ", y= " + dragNode.getPosY());

                //This code is for placing nodes
                c.setOnMouseDragged(e -> {
                    if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                            && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                        c.setLayoutX((e.getSceneX() - paneBounds.getMinX()));
                        c.setLayoutY((e.getSceneY() - paneBounds.getMinY()));
                        System.out.println("dragging...");
                    }
                });
//                c.setOnDragDropped(new EventHandler <DragEvent>() {
//                    public void handle(DragEvent event) {
//                        /* data dropped */
//                        System.out.println("onDragDropped");
//                        System.out.println("--");
//                        System.out.println("old: x= " + dragNode.getPosX() + ", y= " + dragNode.getPosY());
//                        System.out.println("new: x= " + c.getLayoutX() + ", y= " + c.getLayoutY());
//                        System.out.println("---");
//                        event.setDropCompleted(true);
//
//                        event.consume();
//                    }
//                });

                c.setOnMouseDragReleased(e -> {
                    System.out.println("drag done");
                    System.out.println("old: x= "+dragNode.getPosX()+", y= "+dragNode.getPosY());
                    System.out.println("new: x= "+c.getLayoutX()+", y= "+c.getLayoutY());
                    System.out.println("---");
                    DatabaseController.getInstance().

                    updateNode((int) c.

                    getLayoutX(), (int)c.getLayoutY(),currentFloor,
                            dragNode.getIsHidden(),dragNode.getEnabled(),dragNode.getType(),dragNode.getName(),
                            dragNode.getRoomNum(),dragNode.getPermissionLevel());
                    c.setOnMouseDragged(en -> {
                        //nothing
                    });

                    resetScreen();

                });
                break;
            default:
                System.out.println("default. This probably should not have been possible...");
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
                    if (!floors.contains(e.getEndNode().getFloor())){
                        floors.add(e.getEndNode().getFloor());
                    }
                }
                PopOver pop2 = new PopOver();
                createMultiFloorPop(pop2, c, floors, selectedNode2);
                pop2.show(c);
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

    public void showMultifloorMenu(int x, int y, Circle c) {
        // delete later
    }

    public void sceneEvent(int x, int y, Circle c) {

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
                }
            }

            return;
        }


        //highlight the node
        nodeEdgeX1 = (int) x;
        nodeEdgeY1 = (int) y;
        System.out.println(nodeEdgeX1 + "     " + nodeEdgeY1);
        firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                .getNode(nodeEdgeX1, nodeEdgeY1, currentFloor);
        graph.createEdgeLines(firstNode.getEdgeList(), true, true);

        //color the node as well
        if (lastColoredStart !=  null) {
            lastColoredStart.setStroke(lastColoredStart.getFill());
            lastColoredStart.setStrokeWidth(1);
        }
        lastColoredStart = c;
        c.setStrokeWidth(6.5);
        c.setStroke(Color.ROYALBLUE);

//        //display edges already associated with selected node
//        if (edgesSelected == 1 || mode_ChoiceBox.getValue().equals("Edit Node")
//                || mode_ChoiceBox.getValue().equals("Remove Node")) {
//            System.out.println("Edge stage 1");
//            //display edges already associated with selected node
//            nodeEdgeX1 = (int) x;
//            nodeEdgeY1 = (int) y;
//            System.out.println(nodeEdgeX1 + "     " + nodeEdgeY1);
//            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
//                    .getNode(nodeEdgeX1, nodeEdgeY1, currentFloor);
//            graph.createEdgeLines(firstNode.getEdgeList(), true, true);
//
//            //color the node as well
//            if (lastColoredStart !=  null) {
//                lastColoredStart.setStroke(lastColoredStart.getFill());
//                lastColoredStart.setStrokeWidth(1);
//            }
//            lastColoredStart = c;
//            c.setStrokeWidth(2.5);
//            c.setStroke(Color.ROYALBLUE);
//
//            //log the floor
//            floor1 = currentFloor;
//        } else if (edgesSelected == 2) {
//            System.out.println("Edge stage 2");
//            //create edge between the two nodes
//            nodeEdgeX2 = (int) x;
//            nodeEdgeY2 = (int) y;
//
//            //color the node
//            lastColoredEnd = c;
//            c.setStrokeWidth(2.5);
//            c.setStroke(Color.FUCHSIA);
//
//            //log the floor
//            floor2 = currentFloor;
//        }
        String type = "", name = "", room = "";
        boolean hidden = false, enabled = false;
        int perms = -1;
        ResultSet rset = databaseController.getNode(x, y, currentFloor);
        try {
            while (rset.next()){
                type = rset.getString("TYPE");
                name = rset.getString("NAME");
                room = rset.getString("ROOMNUM");
                hidden = rset.getBoolean("ISHIDDEN");
                enabled = rset.getBoolean("ENABLED");
                perms = rset.getInt("PERMISSIONS");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        switch(type){
        case "Doctor's Office":
            title_ChoiceBox.getSelectionModel().select(0);
        case "Food Service":
            title_ChoiceBox.getSelectionModel().select(1);
        case "Restroom":
            title_ChoiceBox.getSelectionModel().select(2);
        case "Information":
            title_ChoiceBox.getSelectionModel().select(3);
        case "Laboratory":
            title_ChoiceBox.getSelectionModel().select(4);
        case "Waiting Room":
            title_ChoiceBox.getSelectionModel().select(5);
        }

        name_TextField.setText(name);
        room_TextField.setText(room);
        hidden_CheckBox.setSelected(hidden);
        enabled_CheckBox.setSelected(enabled);


    }

    //submit button is clicked
    //To Do - use this to send the information of your changes to the DB to get updated
    public void submitButton_Clicked() {
        final String tempName = name_TextField.getText();
        final String tempRoom = room_TextField.getText();
        //String type = title_ChoiceBox.getValue();

        if (mode_ChoiceBox.getValue() == null) {
            System.out.println("incoming null ptr exception");
        }
        switch (mode_ChoiceBox.getValue()) {
            case "---":
                System.out.println("Mode = default");

                break;
            case "Add Node":

                if (!("".equals(title_ChoiceBox.getValue())) &&
                        !("".equals(name_TextField.getText())) &&
                        !("".equals(room_TextField.getText()))) {

                    System.out.println("Mode = add");
                    //get type
                    String type;
                    switch (title_ChoiceBox.getValue()) {
                        case "---":
                            type = "Doctor's Office"; //doctor by default
                            break;
                        default:
                            type = title_ChoiceBox.getValue();
                            break;
                    }


                    Node newNode = new Node((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                            currentFloor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom, 0);
                    DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                            currentFloor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom, 0);
                }
                mode_ChoiceBox.getSelectionModel().select("---");

                break;

            case "Edit Node":
                System.out.println("Mode = edit node");
                //get latest touched node
                boolean newHidden = hidden_CheckBox.isSelected();
                boolean newEnabled = enabled_CheckBox.isSelected();
                String newType;
                String newName = name_TextField.getText();
                String newRoomnum = room_TextField.getText();

                switch (title_ChoiceBox.getValue()) {
                    case "---":
                        newType = "Doctor's Office"; //doctor by default
                        break;
                    default:
                        newType = title_ChoiceBox.getValue();
                        break;
                }



                //update to new version in db
                DBController.DatabaseController.getInstance().updateNode(firstNode.getPosX(), firstNode.getPosY(),
                        firstNode.getFloor(), newHidden, newEnabled, newType, newName, newRoomnum, 0);

                break;
            case "Remove Node":
                System.out.println("Mode = remove node");
                for (controllers.Edge thisEdge : firstNode.getEdgeList()) {
                    thisEdge.getNeighbor(firstNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                DBController.DatabaseController.getInstance().deleteNode(firstNode.getPosX(), firstNode.getPosY(), currentFloor);
                break;
            case "Add Edge":
                if (edgesSelected == 2) {
                    System.out.println("Mode = add edge");
                    DBController.DatabaseController.getInstance().newEdge(nodeEdgeX1,
                            nodeEdgeY1, floor1, nodeEdgeX2, nodeEdgeY2, floor2);

                    System.out.println("added edge");
                }
                break;
            case "Remove Edge":
                if (edgesSelected == 2) {
                    System.out.println("Mode = delete edge");
                    DBController.DatabaseController.getInstance().deleteEdge(nodeEdgeX1,
                            nodeEdgeY1, floor1, nodeEdgeX2, nodeEdgeY2, floor2);
                    System.out.println("removed edge");
                }
                System.out.println("Mode = remove edge");
                break;
            default:
                System.out.println("Nothing selected for mode");
                break;
        }
        controllers.MapController.getInstance().requestMapCopy();

        //show edge lines to tell user change has been made
        //check so edge lines do not show up on wrong floor
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), true,
                currentFloor, permissionLevel);
        edgesSelected = 0;


        //try to display last touched edge list
        //requery firstnode to reset edge list
        //check so edge lines do not show up on wrong floor
        if(firstNode != null && floor1 == floor2 && (mode_ChoiceBox.getValue().equals("Add Edge") || mode_ChoiceBox.getValue().equals("Remove Edge"))) {
            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                    .getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
            //don't know if above method is successful
            //must check again if firstNode is not null
            if (firstNode != null) {
                graph.createEdgeLines(firstNode.getEdgeList(), true, false);
            }
        }

    }

    //Change to main Menu
    public void mainMenuButton_Clicked() {

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(username_Label.getText());
        System.out.println(username_Label.getText());

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
        username_Label.setText(user);
    }

    public void setModeChoices() {
        mode_ChoiceBox.getItems().addAll("---", "Add Node", "Remove Node", "Edit Node", "Add Edge", "Remove Edge");
        mode_ChoiceBox.getSelectionModel().selectFirst();
        mode_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Do validation
                clearButton_Clicked();
                System.out.println(newValue);
                if (newValue.intValue() == 1) {
                    create_Button();
                }

            }
        });
    }

    //Sets the map of the desired floor
    /*public void setFloorChoices(){
        floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7");
        floor_ChoiceBox.getSelectionModel().select(0);
        map_viewer.setImage(new Image("/images/cleaned1.png"));
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                System.out.println(newValue);
                currentFloor = newValue.intValue() + 1;

                mapImage newMapImage = new proxyMap(currentFloor);
                newMapImage.display(map_viewer);

                graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true, currentFloor);

                //draw edges
                graph.drawFloorEdges(currentFloor);
            }
        });
    }*/

    //Sets the map of the desired floor
    public void setFloorChoices(){
        floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Outside",
                "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Belkin Basement");

        floor_ChoiceBox.getSelectionModel().select(0);
        map_viewer.setImage(new Image("/images/cleaned1.png"));
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                boolean outside = false;
                String currentF = "";
                //Print the floors accordingly
                //CODE HERE!!!!!!!

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

                /////////////////////////////////


                /////////////////////////////////
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

    public void setTitleChoices() {

        title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom", "Elevator", "Stair",
                "Information", "Laboratory", "Waiting Room");
    }

    public void create_Button() {
        btK = new Circle(labelRadius);//new Button();
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

        admin_FloorPane.getChildren().add(btK);
    }

    //sets the current language given information form other screens
    public void setC_language(int i){
        c_language = i;
    }

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        //change the current language to english
        c_language = 0;

        //Change the Buttons
        mainMenu_Button.setText("Main Menu");
        emergency_Button.setText("EMERGENCY");
        submit_Button.setText("Submit");
        clear_Button.setText("Clear");

        //Change the labels
        title_Label.setText("Map Management");
        subTitile_Label.setText("Information:");
        nodeTitile_Label.setText("Title:");
        name_Label.setText("Name:");
        room_Label.setText("Room:");
        hidden_Label.setText("Hidden:");
        enabled_Label.setText("Enabled:");
        floorMap_Label.setText("Floor Map");
        chooseFloor_Label.setText("Floor:");
        mode_Label.setText("Mode:");



        //text fields
        name_TextField.setPromptText("Name");
        room_TextField.setPromptText("Room");


    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //Change the Buttons
        mainMenu_Button.setText("Menú Principal");
        emergency_Button.setText("EMERGENCIA");
        submit_Button.setText("Listo");
        clear_Button.setText("Borrar");

        //Change the labels
        title_Label.setText("Control de Mapa");
        subTitile_Label.setText("Información:");
        nodeTitile_Label.setText("Título:");
        name_Label.setText("Nombre:");
        room_Label.setText("Habitacion:");
        hidden_Label.setText("Oculto:");
        enabled_Label.setText("Habilitar:");
        floorMap_Label.setText("Mapa del Piso");
        chooseFloor_Label.setText("Piso:");
        mode_Label.setText("Modo:");

        //text fields
        name_TextField.setPromptText("Name");
        room_TextField.setPromptText("Room");


    }
}
