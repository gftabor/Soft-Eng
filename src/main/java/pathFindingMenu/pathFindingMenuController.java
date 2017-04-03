package pathFindingMenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

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

    private Button btK;

    private ArrayList ButtonList = new ArrayList();

    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    public void cancelButton_Clicked(){

        System.out.println("The user has clicked the cancel Button");
    }

    public void submitButton_Clicked(){
        //creating button with sample x and y
        create_Button(200, 200);
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
    /*public void setMapAndNodes(Hashtable nodeTable){
        int currentKey;
        Enumeration tableKeys;
        tableKeys = nodeTable.keys();



        while (tableKeys.hasMoreElements()) {
            currentKey = (int) tableKeys.nextElement();
        }
    }*/

    public void setMode(String mode){
        mode_Label.setText(mode);

    }

    public void setUserString(String user){
        username_Label.setText(user);

    }

    public void create_Button(double nodeX, double nodeY){
        System.out.println("checking button");
        System.out.println("make button");
        btK = new Button("node");

        // this code sets node's x and y pos to be on the plane below the graph
        // later, will iterate through a list of coordinates and place nodes at each point
        //btK.setLayoutX(nodeX);
        //btK.setLayoutY(nodeY);

        /*btK.setOnMouseDragged(e -> {
            btK.setLayoutX(e.getSceneX());
            btK.setLayoutY(e.getSceneY());
        });*/

        //node_Plane.setMargin(btK, new Insets(nodeY, 0,0,nodeX));
        backgroundAnchorPane.getChildren().addAll(btK);
        btK.setLayoutX(node_Plane.localToScene(node_Plane.getBoundsInLocal()).getMinX() + nodeX);
        btK.setLayoutY(node_Plane.localToScene(node_Plane.getBoundsInLocal()).getMinY() + nodeY);

        ButtonList.add(btK);
    }


}
