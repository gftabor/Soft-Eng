package pathFindingMenu;

import controllers.MapController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by AugustoR on 3/30/17.
 */
public class pathFindingMenuController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Circle blackCircle_Circle;

    @FXML
    private Circle redCircle_Circle;

    @FXML
    private Button emergency_Button;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private Label currentFloor_Label;

    @FXML
    private Label mode_Label;

    @FXML
    private Label username_Label;

    @FXML
    private Pane node_Plane;

    @FXML
    private Circle start_Dot;

    @FXML
    private Circle end_Dot;

    private Circle btK;

    private Line lne;

    private ArrayList ButtonList = new ArrayList();

    private ArrayList<Line> lineList = new ArrayList();

    private int selectionState = 0;

    private MapController mapController = MapController.getInstance();

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    public void cancelButton_Clicked(){
        selectionState = 0;
        //Remove black and red dots from map
    }

    public void submitButton_Clicked(){
        if (selectionState == 2) {
            //submit stuff
            createEdgeLines(mapController.getEdgeCollection());
            mapController.requestPath();
        }
        selectionState=0;
        System.out.println("The user has clicked the submit Button");
    }

    public void mainMenuButton_Clicked(){
        if(username_Label.getText().equals("")) {
            switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
        }else{
            //Get the scene loader
            FXMLLoader loader = switch_screen(backgroundAnchorPane,"/views/adminMenuStartView.fxml");
            //Get the controller of the scene
            adminMenuStart.adminMenuStartController controller = loader.getController();
            controller.setUsername(username_Label.getText());

        }

    }

    //takes in a Hashtable when scene is switched and calls setNodes
    public void setMapAndNodes(HashMap<Integer, controllers.Node> nodeMap){
        int currentKey;
        for(controllers.Node current: nodeMap.values()){
         create_Button(current.getPosX(), current.getPosY());
        }
    }

    public void setMode(String mode){
        mode_Label.setText(mode);

    }

    public void setUserString(String user){
        username_Label.setText(user);

    }

    public void create_Button(double nodeX, double nodeY){
        //System.out.println("checking button");
        //System.out.println("make button");
        btK = new Circle(7);
        btK.setOnMouseClicked(e -> {
            nodeSelected((int)((nodeX)), (int)((nodeY)));
        });

        // this code sets node's x and y pos to be on the plane holding the graph
        node_Plane.getChildren().add(btK);
        btK.setLayoutX(nodeX);
        btK.setLayoutY(nodeY);
        btK.toFront();

        ButtonList.add(btK);
    }

    public int nodeSelected(int x, int y) {
        System.out.println("Node at (" + x + ", " + y + ") selected during state: " + selectionState);
        if (selectionState == 0) {
            //place the black marker at the starting location
            mapController.markNode(x, y, 1);
            selectionState++;
            return 0;
        } else if (selectionState == 1){
            //place the red marker at end location
            mapController.markNode(x, y, 2);
            selectionState++;
            return 0;
        } else {
            //do nothing
            return 0;
        }
    }

    //creates visual representations of the edges of nodes on the pane
    //  input: any arraylist of Edge objects
    //NOTE: caller is responsible for not sending duplicate edges
    public void createEdgeLines(ArrayList<controllers.Edge> edgeList) {
        //for-each loop through arraylist
        for(Line currentLine : lineList) {
            node_Plane.getChildren().remove(currentLine);
        }
        for(controllers.Edge thisEdge: edgeList) {
            lne = new Line();

            //add to pane
            node_Plane.getChildren().add(lne);
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

}
