package pathFindingMenu;

import controllers.*;
import emergency.SmsSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URISyntaxException;
import java.util.ArrayList;

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
    private ImageView map_viewer;

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

    @FXML
    private Button continue_Button;

    @FXML
    private TextArea textDescription_TextFArea;

    @FXML
    private Label title_Label;

    @FXML
    private Label end_Label;

    @FXML
    private Label start_Label;

    @FXML
    private TextField phoneInsert;

    @FXML
    private Button phoneSend;

    @FXML
    private Label phoneStatus;

    private Circle start;
    private Circle end;

    private double startX;
    private double startY;


    private Circle btK;

    private int selectionState = 0;
    private int currentFloor;

    private final double sizeUpRatio = 1.7;
    private final double strokeRatio = 2.5;

    private controllers.MapOverlay graph;
    private MapController mapController = MapController.getInstance();

    private ArrayList<ArrayList<Edge>> globalFragList;
    private int fragPathPos; //position on the global frag list
    private ArrayList<Integer> globalFloorSequence;

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

    public void textDirections(){
        SmsSender mySMS = new SmsSender();
        try {
            if(mySMS.sendSMSDirections(textDescription_TextFArea.getText(), phoneInsert.getText()).equals("queued")){
                phoneStatus.setText("Directions Sent.");
            }else{
                phoneStatus.setText("Failed to Send.");
            }
        } catch (URISyntaxException e){
            e.getReason();
        }
    }

    @FXML
    public void initialize() {
        graph = new controllers.MapOverlay(node_Plane,(mapScene) this);
        MapController.getInstance().requestMapCopy();
        setFloorChoices();
        //set current floor
        //we will use floor 1 as default
        currentFloor = 1;
        currentFloor_Label.setText("1");
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);

        //set continue button invisible when not needed
        continue_Button.setVisible(false);

    }
    public void cancelButton_Clicked(){
        //MapController.getInstance().requestMapCopy();
        selectionState = 0;
        //Remove colored dots from map

        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);
        currentFloor_Label.setText(Integer.toString(currentFloor));

        //wipe line from map
        graph.wipeEdgeLines();

        //hide the continue button
        continue_Button.setVisible(false);

    }

    public void submitButton_Clicked(){

        if (selectionState == 2) {
            //submit stuff
            //createEdgeLines

            //check for multifloor
            if (mapController.areDifferentFloors()) {
                System.out.println("Multi-floor pathfinding detected!");

                //use the multifloor pathfinding function
                multiFloorPathfind();
            } else {
                MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
                ArrayList<Edge> path = mapController.requestPath();
                graph.createEdgeLines(path);
                textDescription_TextFArea.setText(mapController.getTextDirections(path));

            }

        }
        selectionState=0;
        System.out.println("The user has clicked the submit Button");
    }

    public void multiFloorPathfind() {
        //initialize reference of the global frag list to null (set up)
        globalFragList = null;
        globalFloorSequence = null;
        fragPathPos = 0;

        //set continue button visible
        continue_Button.setVisible(true);

        //switch floors to original floor's pathfinding view
        int startfloor = mapController.returnOriginalFloor();
        currentFloor_Label.setText(Integer.toString(startfloor));
        System.out.println("startfloor: " + Integer.toString(startfloor));

        //switch back to the original floor using the choicebox selection
        floor_ChoiceBox.getSelectionModel().select(startfloor - 1);
        System.out.println("Current floor: " + Integer.toString(currentFloor) + " :)");

        //todo: maintain consistency of colors - doesn't work - references go missing
        // start.setStrokeWidth(strokeRatio);
        // start.setStroke(Color.ORANGERED);
        //start.setRadius(graph.getLabelRadius());

        //reset for next pathfinding session
        MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
        ArrayList<Edge> reqPath = mapController.requestPath();
        textDescription_TextFArea.setText(mapController.getTextDirections(reqPath));

        //original call below >
        //graph.createEdgeLines(reqPath);
        System.out.println("=====================");
        ArrayList<ArrayList<Edge>> fragPath;
        fragPath = mapController.requestFragmentedPath(reqPath, mapController.returnOriginalFloor(), mapController.returnDestFloor());

        System.out.println("frag path info:");
        System.out.println("---");
        System.out.println("size: " + fragPath.size());
        System.out.println("---");
        for (int i = 0; i < fragPath.size(); i++) {
            System.out.println(fragPath.get(i).size());
        }
        System.out.println("-----");
        System.out.println("=====================");

        System.out.println("printing the fragmented path for (startfloor) floor = " + Integer.toString(startfloor));
        //loop and display the edges per floor - use the startfloor


        if (fragPath.get(0) == null) {
            //only occurs if the first transition is a null
            //instead just highlight the first thing

            //todo -> highlight

        } else {
            graph.createEdgeLines(fragPath.get(0));
        }

        //set the globals so you can send to the continue button
        globalFragList = fragPath;
        globalFloorSequence = mapController.getFloorSequence();

        //print floor sequence (testing)
        System.out.println("_____");
        System.out.println("floor sequence: ");
        for (int i = 0; i < globalFloorSequence.size(); i++) {
            System.out.println(globalFloorSequence.get(i));
        }
        System.out.println("_____");


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
            mapController.markNode(x, y, 1, currentFloor);
            selectionState++;
            if(start != null) {
                start.setStroke(Color.BLACK);
                start.setStrokeWidth(1);
                start.setRadius(graph.getLabelRadius());
            }
            if(end != null) {
                end.setStroke(Color.BLACK);
                end.setStrokeWidth(1);
                end.setRadius(graph.getLabelRadius());
            }
            graph.wipeEdgeLines();
            start =c;
            //color
            c.setStrokeWidth(strokeRatio);
            c.setStroke(Color.ORANGERED);

            startX = c.getCenterX();
            startY = c.getCenterY();

            //size
            c.setRadius(graph.getLabelRadius() * sizeUpRatio);

            //hide the continue button if possible
            continue_Button.setVisible(false);
        } else if (selectionState == 1){
            //place the red marker at end location
            mapController.markNode(x, y, 2, currentFloor);
            selectionState++;
            end = c;
            //color
            c.setStrokeWidth(strokeRatio);
            c.setStroke(Color.FUCHSIA);

            //size
            c.setRadius(graph.getLabelRadius() * sizeUpRatio);
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


    //Sets the map of the desired floor
    public void setFloorChoices(){
        floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7");
        floor_ChoiceBox.getSelectionModel().select(0);
        map_viewer.setImage(new Image("/images/cleaned1.png"));
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                //Print the floors accordingly
                //CODE HERE!!!!!!!

                currentFloor = newValue.intValue() + 1;

                mapImage newMapImage = new proxyMap(currentFloor);
                newMapImage.display(map_viewer);

                currentFloor_Label.setText(Integer.toString(currentFloor));
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);
            }
        });

    }

    public void setFloorChoiceRemote(int floor) {
        floor_ChoiceBox.getSelectionModel().select(floor - 1);
    }

    public void createEdgeLines(ArrayList<Edge> path) {
        graph.createEdgeLines(path);
    }

    public void continueButton_Clicked() {

        if (continue_Button.isVisible() == true) {
            System.out.println("continue button clicked");

            //increment b/c continue button
            fragPathPos++; //continue...

            //update currentfloor
            currentFloor = globalFloorSequence.get(fragPathPos);

            System.out.println("current floor displayed: " + currentFloor);
            System.out.println("frag path pos updated to: " + fragPathPos);
            multifloorUpdate();

            //disable the continue button if you reach the end
            if (fragPathPos == globalFragList.size() - 1) {
                continue_Button.setVisible(false);
            }
        }
    }

    //abstracted floor refresh for multifloor pathfinding
    public void multifloorUpdate() {
        System.out.println("cf: " + currentFloor + "   size: " + globalFragList.get(fragPathPos).size());

        //otherwise, change to the appropriate screen and display edges
        graph.wipeEdgeLines();
        floor_ChoiceBox.getSelectionModel().select(currentFloor - 1);
        System.out.println("creating edge lines for fp pos: " + fragPathPos);
        graph.createEdgeLines(globalFragList.get(fragPathPos));
    }

//    public void previousButton_Clicked() {
//
//        //todo: fix continue button check later
//        if (continue_Button.isVisible() == true) {
//            System.out.println(".previous. button clicked");
//            System.out.println("going up:" );
//            System.out.println(mapController.goingUp());
//            if (mapController.goingUp()) {
//
//                //loop until you hit the top of the hospital
//                while (currentFloor != 0) {
//                    System.out.println("Prev: going down loop");
//
//                    //increment floor
//                    currentFloor --;
//
//                    //if there are no edges of interest, do not display them
//                    if (globalFragList[currentFloor] == null || globalFragList[currentFloor].isEmpty()) {
//                        continue;
//                    }
//                    multifloorUpdate();
//
//                    break;
//
//                }
//            } else {
//                //loop until you hit the bottom of the hospital
//                while (currentFloor != 8) {
//
//                    System.out.println("Prev: going up loop");
//                    //decrement floor
//                    currentFloor ++;
//
//                    //if there are no edges of interest, do not display them
//                    if (globalFragList[currentFloor] == null || globalFragList[currentFloor].isEmpty()) {
//                        continue;
//                    }
//                    multifloorUpdate();
//
//                    break;
//                }
//
//            }
//
//            //disable the continue button if you reach the end
//            int startFloor = mapController.returnOriginalFloor();
//            if (currentFloor == startFloor) {
//                //todo fix later - set to prev button
////                continue_Button.setVisible(false);
//            }
//        }
//    }

}
