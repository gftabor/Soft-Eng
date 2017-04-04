package mapManagementFloorAndMode;

import controllers.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.ArrayList;

/**
 * Created by AugustoR on 3/31/17.
 */
public class mmFloorAndModeController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button emergency_Button;

    @FXML
    private Spinner<Integer> floorChoices_Spinner;

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

    //Add the name and Room
    @FXML
    private TextField name_TextField;

    @FXML
    private TextField room_TextField;

    @FXML
    private Pane admin_FloorPane;

    private ArrayList nodeList = new ArrayList();

    private ArrayList EdgeList = new ArrayList();

    private Line lne;

    private int selectNodeX;
    private int selectNodeY;

    private int nodeEdgeX;
    private int nodeEdgeY;

    private int edgesSelected = 0;

    private Node firstNode;
    private Node secondNode;

    private Button btK;
    public void emergencyButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
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
                System.out.println("Mode = add");
                Node newNode = new Node((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                        hidden_CheckBox.isSelected(), true, name, floor);
                DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                    floor, hidden_CheckBox.isSelected(), true, "Doctor", tempName, tempRoom);
                nodeList.add(newNode);

                Button newButton = new Button();
                newButton.setLayoutX(newNode.getPosX());
                newButton.setLayoutY(newNode.getPosY());
                newButton.setOnMouseClicked(e -> {
                    if (mode_ChoiceBox.getValue().equals( "Add Edge")) {
                        edgesSelected++;

                        if (edgesSelected == 1){
                            //display edges already associated with selected node
                            nodeEdgeX = (int) newButton.getLayoutX();
                            nodeEdgeY = (int) newButton.getLayoutY();

                            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                                    .getNode(nodeEdgeX, nodeEdgeY, 4);

                            createEdgeLines(firstNode.getEdgeList());

                        }
                        if (edgesSelected == 2) {
                            //create edge between the two nodes
                            nodeEdgeX = (int) newButton.getLayoutX();
                            nodeEdgeY = (int) newButton.getLayoutY();

                            secondNode = controllers.MapController.getInstance().getCollectionOfNodes()
                                    .getNode(nodeEdgeX, nodeEdgeY, 4);

                            DBController.DatabaseController.getInstance().newEdge( firstNode.getPosX(),
                                    firstNode.getPosY(),4, secondNode.getPosX(), secondNode.getPosY(), 4);

                            edgesSelected = 0;
                        }
                    }
                });

                admin_FloorPane.getChildren().add(newButton);
                newButton.toFront();

                mode_ChoiceBox.getSelectionModel().select("---");
                break;
            case "Edit Node":
                System.out.println("Mode = edit node");
                break;
            case "Remove Node":
                System.out.println("Mode = remove node");
                break;
            case "Add Edge":
                System.out.println("Mode = add edge");
                break;
            case "Remove Edge":
                System.out.println("Mode = remove edge");
                break;
            default:
                System.out.println("Nothing selected for mode");
                break;
        }
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
                        System.out.println(newValue);
                        if(newValue.intValue()==1){
                            create_Button();
                        } else {
                            admin_FloorPane.getChildren().remove(btK);
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
            btK = new Button();
            // this code drags the button
            final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());

            //This code is for placing nodes
            btK.setOnMouseDragged(e -> {
                if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                        && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                    btK.setLayoutX((e.getSceneX() - paneBounds.getMinX()) - (btK.getWidth()/2));
                    btK.setLayoutY((e.getSceneY() - paneBounds.getMinY()) - (btK.getHeight()/2));
                }
            });

            //Clicking on Nodes added to the Map




            admin_FloorPane.getChildren().add(btK);
            btK.toFront();

    }
    //creates visual representations of the edges of nodes on the pane
    //  input: any arraylist of Edge objects
    //NOTE: caller is responsible for not sending duplicate edges
    public void createEdgeLines(ArrayList<controllers.Edge> edgeList) {
        //for-each loop through arraylist
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
            EdgeList.add(lne);
        }
    }

}
