package mapManagementFloorAndMode;

import controllers.MapController;
import controllers.Node;
import controllers.mapScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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

    private int nodeEdgeX1;
    private int nodeEdgeY1;
    private int nodeEdgeX2;
    private int nodeEdgeY2;

    private int edgesSelected = 0;

    private controllers.MapOverlay graph;

    private static final double lableRadius = 8.5;

    private Node firstNode;

    private Circle lastColored;

    private Circle btK;

    public void initialize() {
        setUserString(username_Label.getText());
        setModeChoices();
        setTitleChoices();

        graph = new controllers.MapOverlay(admin_FloorPane,(mapScene) this);
        MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(4),true);

    }


    public void emergencyButton_Clicked() {
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
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

        //reset last colored stroke to default
        if (lastColored != null) {
            lastColored.setStroke(lastColored.getFill());
            lastColored.setStrokeWidth(1);
        }
    }

    public void sceneEvent(int x, int y, Circle c) {
        edgesSelected++;
        if (edgesSelected == 1) {
            //display edges already associated with selected node
            if (edgesSelected == 1 || mode_ChoiceBox.getValue().equals("Edit Node")
                    || mode_ChoiceBox.getValue().equals("Remove Node")) {
                //display edges already associated witdh selected node
                nodeEdgeX1 = (int) x;
                nodeEdgeY1 = (int) y;
                System.out.println(nodeEdgeX1 + "     " + nodeEdgeY1);
                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                        .getNode(nodeEdgeX1, nodeEdgeY1, 4);
                graph.createEdgeLines(firstNode.getEdgeList());
            } else if (edgesSelected == 2) {
                //create edge between the two nodes
                nodeEdgeX2 = (int) x;
                nodeEdgeY2 = (int) y;

                lastColored = c;

                c.setStrokeWidth(2.5);
                c.setStroke(Color.FUCHSIA);
            }
        }
    }

    //submit button is clicked
    //To Do - use this to send the information of your changes to the DB to get updated
    public void submitButton_Clicked() {
        final String tempName = name_TextField.getText();
        final String tempRoom = room_TextField.getText();
        final int floor = 4;
        final String name = "Mark?";

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
                            floor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom);
                    DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                            floor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom);
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
                        firstNode.getFloor(), newHidden, newEnabled, newType, newName, newRoomnum);

                break;
            case "Remove Node":
                System.out.println("Mode = remove node");
                for (controllers.Edge thisEdge : firstNode.getEdgeList()) {
                    thisEdge.getNeighbor(firstNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                DBController.DatabaseController.getInstance().deleteNode(firstNode.getPosX(), firstNode.getPosY(), 4);
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
                if (edgesSelected == 2) {
                    System.out.println("Mode = add edge");
                    DBController.DatabaseController.getInstance().deleteEdge(nodeEdgeX1,
                            nodeEdgeY1, 4, nodeEdgeX2, nodeEdgeY2, 4);
                    System.out.println("added edge");
                }
                System.out.println("Mode = remove edge");
                break;
            default:
                System.out.println("Nothing selected for mode");
                break;
        }
        controllers.MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(4),true);
        edgesSelected = 0;


        //try to display last touched edge list
        //requery firstnode to reset edge list
        if(firstNode != null) {
            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                    .getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
            //don't know if above method is successful
            //must check again if firstNode is not null
            if (firstNode != null) {
                graph.createEdgeLines(firstNode.getEdgeList());
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
    }

    public void setUserString(String user) {
        username_Label.setText(user);
    }

    public void setModeChoices() {
        mode_ChoiceBox.getItems().addAll("---", "Add Node", "Remove Node", "Edit Node", "Add Edge", "Remove Edge");
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

    public void setTitleChoices() {

        title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom");
    }

    public void create_Button() {
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

        admin_FloorPane.getChildren().add(btK);
    }

}