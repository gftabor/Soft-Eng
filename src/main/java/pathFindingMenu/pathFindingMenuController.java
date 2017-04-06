package pathFindingMenu;

import controllers.MapController;
import controllers.mapScene;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by AugustoR on 3/30/17.
 */
public class pathFindingMenuController extends controllers.mapScene{
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

    private Circle start;
    private Circle end;

    private Circle btK;

    private int selectionState = 0;
    private controllers.MapOverlay graph;

    private MapController mapController = MapController.getInstance();


    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }
    @FXML
    public void initialize() {
        graph = new controllers.MapOverlay(node_Plane,(mapScene) this);
        MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(4),false);
    }
    public void cancelButton_Clicked(){
        MapController.getInstance().requestMapCopy();
        selectionState = 0;
        //Remove black and red dots from map
    }

    public void submitButton_Clicked(){

        if (selectionState == 2) {
            //submit stuff
            //createEdgeLines
            MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
            graph.createEdgeLines(mapController.requestPath());
        }
        selectionState=0;
        System.out.println("The user has clicked the submit Button");
        cancelButton_Clicked();
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

    public void sceneEvent(int x, int y, Circle c){
        System.out.println("Node at (" + x + ", " + y + ") selected during state: " + selectionState);
        if (selectionState == 0) {
            //place the black marker at the starting location
            mapController.markNode(x, y, 1);
            selectionState++;
            if(start != null)
                start.setFill(Color.BLACK);
            if(end != null)
                end.setFill(Color.BLACK);
            graph.wipeEdgeLines();
            start =c;
            //color
            c.setFill(Color.MAGENTA);
        } else if (selectionState == 1){
            //place the red marker at end location
            mapController.markNode(x, y, 2);
            selectionState++;
            end = c;
            //color
            c.setFill(Color.BROWN);
        } else {
            //do nothing
        }
    }

    public void setMode(String mode){
        mode_Label.setText(mode);

    }

    public void setUserString(String user){
        username_Label.setText(user);

    }
}
