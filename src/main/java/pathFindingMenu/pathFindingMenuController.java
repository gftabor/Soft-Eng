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
    private Label username_Label;

    @FXML
    private Pane node_Plane;

    @FXML
    private Label title_Label;

    @FXML
    private Label end_Label;

    @FXML
    private Label start_Label;


    private Circle start;
    private Circle end;

    private Circle btK;

    private int selectionState = 0;
    private controllers.MapOverlay graph;

    private MapController mapController = MapController.getInstance();


    //flags for the english/spanish feature
    int c_language = 0;


    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");
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
            System.out.println("");
        }
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
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
            patientMenuStart.patientMenuStartController controller = loader.getController();
            //sets the current language
            controller.setCurrentLanguage(c_language);
            //set up english labels
            if(c_language == 0){
                controller.englishButtons_Labels();

                //set up spanish labels
            }else if(c_language == 1){
                controller.spanishButtons_Labels();
            }

            //admin view
        }else{
            //Get the scene loader
            FXMLLoader loader = switch_screen(backgroundAnchorPane,"/views/adminMenuStartView.fxml");
            //Get the controller of the scene
            adminMenuStart.adminMenuStartController controller = loader.getController();
            controller.setUsername(username_Label.getText());
            //sets the current language
            controller.setCurrentLanguage(c_language);
            controller.setLanguageChoices();
            //set up english labels
            if(c_language == 0){
                controller.englishButtons_Labels();

                //set up spanish labels
            }else if(c_language == 1){
                controller.spanishButtons_Labels();
            }
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

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        //change the current language to english
        c_language = 0;

        //Change the Buttons
        emergency_Button.setText("EMERGENCY");
        mainMenu_Button.setText("Main Menu");
        submit_Button.setText("Submit");
        cancel_Button.setText("Cancel");


        //Change the labels
        start_Label.setText("Start Point");
        end_Label.setText("End Point");
        title_Label.setText("Map");




    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        emergency_Button.setText("EMERGENCIA");
        mainMenu_Button.setText("Menú Principal");
        submit_Button.setText("Listo");
        cancel_Button.setText("Borrar");

        //change the Labels
        start_Label.setText("Inicio: ");
        end_Label.setText("Destino: ");
        title_Label.setText("Mapa");

    }

    //sets the current language given information form other screens
    public void setC_language(int i){
        c_language = i;
    }

}
