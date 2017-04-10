package pathFindingMenu;

import controllers.MapController;
import controllers.mapScene;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private ChoiceBox<String> floor_ChoiceBox;



    @FXML
    private Label username_Label;

    @FXML
    private Pane node_Plane;

    private Circle start;
    private Circle end;

    private Circle btK;

    private int selectionState = 0;
    private int currentFloor;

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

        //set current floor
        //we will use floor 1 as default
        currentFloor = 1;
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);
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

    //Sets the string of the user into the scene
    public void setUserString(String user){
        username_Label.setText(user);

    }

    //Sets the map of the desired floor
    public void setFloorChoices(){
        floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7");
        floor_ChoiceBox.getSelectionModel().select(0);
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                System.out.println(newValue);
                //Print the floors accordingly
                //CODE HERE!!!!!!!
                if (newValue.intValue() == 0) {
                    System.out.println("Showing first floor");
                    currentFloor = 1;

                }else if(newValue.intValue() == 1){
                    System.out.println("Showing second floor");
                    currentFloor = 2;

                }else if(newValue.intValue() == 2){
                    System.out.println("Showing third floor");
                    currentFloor = 3;

                }else if(newValue.intValue() == 3){
                    System.out.println("Showing fourth floor");
                    currentFloor = 4;

                }else if(newValue.intValue() == 4){
                    System.out.println("Showing fifth floor");
                    currentFloor = 5;

                }else if(newValue.intValue() == 5){
                    System.out.println("Showing sixth floor");
                    currentFloor = 6;

                }else if(newValue.intValue() == 6){
                    System.out.println("Showing seventh floor");
                    currentFloor = 7;

                }
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);
            }
        });

    }
}
