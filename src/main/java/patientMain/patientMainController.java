package patientMain;

/**
 * Created by AugustoR on 4/15/17.
 */
import DBController.DatabaseController;
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
import org.controlsfx.control.textfield.TextFields;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class patientMainController extends controllers.mapScene {

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private ChoiceBox<String> language_ChoiceBox;

    @FXML
    private Button about_Button;

    @FXML
    private Button admin_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Label start_Label;

    @FXML
    private TextField start_textField;

    @FXML
    private Label end_Label;

    @FXML
    private TextField end_TextField;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private Label textD_Label;

    @FXML
    private TextArea textDescription_TextFArea;

    @FXML
    private TextField phoneInsert;

    @FXML
    private Button phoneSend;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    @FXML
    private Label floor_Label;

    @FXML
    private Label phoneInfo_Label;

    @FXML
    private Label c_Floor_Label;

    @FXML
    private ChoiceBox<String> filter_ChoiceBox;

    @FXML
    private Pane node_Plane;

    @FXML
    private ImageView map_viewer;

    @FXML
    private Label phoneStatus;

    @FXML
    private Button directory_Button;

    @FXML
    private Button previous_Button;

    @FXML
    private Button continueNew_Button;

    @FXML
    private Button zoom_button;




    int c_language = 0;

    int first_Time = 0;

    boolean second = false;

    private int currentFloor;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    private boolean usingMap;

    private controllers.MapOverlay graph;

    private int selectionState;

    private MapController mapController = MapController.getInstance();

    private Circle start;
    private Circle end;

    private final double sizeUpRatio = 1.9;
    private final double strokeRatio = 4;

    private ArrayList<ArrayList<Edge>> globalFragList;
    private int fragPathPos; //position on the global frag list
    private ArrayList<Integer> globalFloorSequence;
    private ArrayList<Edge> path;

    private final Color startColor = Color.RED;
    private final Color endColor = Color.GREEN;

    private double origPaneWidth;
    private double origPaneHeight;
    double zoom;

    //ArrayList<Edge> zoomPath;

    @FXML
    public void initialize(){
        graph = new controllers.MapOverlay(node_Plane,(mapScene) this);
        MapController.getInstance().requestMapCopy();

        //setLanguageChoices(c_language);
        setFloorChoices();
        setStartEndChoices();
        setLanguage_ChoiceBox();
        //setComboBox();
        setFilterChoices();
        //set current floor
        //we will use floor 1 as default
        currentFloor = 1;
        c_Floor_Label.setText("1");
        usingMap = false;

        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false, currentFloor);
        //set continue button invisible when not needed
        continueNew_Button.setVisible(false);
        previous_Button.setVisible(false);
        second = true;

        //draw edges
        //graph.drawFloorEdges(currentFloor);
        
        origPaneHeight = 489;
        origPaneWidth = 920;
    }

    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    //Continue New Button Clicked
    public void continueNewButton_Clicked(){
        if (continueNew_Button.isVisible() == true) {
            System.out.println("continue button clicked");

            //set the previous button to be enabled
            previous_Button.setVisible(true);

            //increment b/c continue button
            fragPathPos++; //continue...

            //update currentfloor
            currentFloor = globalFloorSequence.get(fragPathPos);

            System.out.println("++++++++++++++++++++++++++++++=============+++++++++");
            System.out.println("current floor displayed: " + currentFloor);
            System.out.println("frag path pos updated to: " + fragPathPos);
            multifloorUpdate();

            //disable the continue button if you reach the end
            //also update the color
            if (fragPathPos == globalFragList.size() - 1) {
                continueNew_Button.setVisible(false);

                //set the end goal color
                ArrayList<Circle> circleList;
                circleList = graph.getButtonList();

                for (Circle c: circleList) {
                    if(c.getLayoutX() == endX && c.getLayoutY() == endY) {
                        c.setStrokeWidth(strokeRatio);
                        c.setRadius(graph.getLabelRadius()*sizeUpRatio);
                        c.setStroke(endColor);
                        c.setFill(endColor);
                        break;
                    }
                }
            }
        }
    }

    //previoys Button clicked
    public void previousButton_Clicked(){
        System.out.println("prev button clicked");

        //show the continue button
        continueNew_Button.setVisible(true);

        //decrement frag path pos
        fragPathPos--;

        //update currentfloor
        currentFloor = globalFloorSequence.get(fragPathPos);

        multifloorUpdate();

        //disable the previous button if you reach the beginning
        //also update the color
        if (fragPathPos == 0) {
            previous_Button.setVisible(false);

            //set the end goal color
            ArrayList<Circle> circleList;
            circleList = graph.getButtonList();

            for (Circle c: circleList) {
                if(c.getLayoutX() == startX && c.getLayoutY() == startY) {
                    c.setStrokeWidth(strokeRatio);
                    c.setRadius(graph.getLabelRadius()*sizeUpRatio);
                    c.setStroke(startColor);
                    c.setFill(startColor);
                    break;
                }
            }
        }
    }

    //Sets the choices for the language
    public void setLanguage_ChoiceBox(){
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        language_ChoiceBox.getItems().addAll("English", "Espanol");
        language_ChoiceBox.getSelectionModel().select(0);
        language_ChoiceBox.setTooltip(new Tooltip("Select the language"));

        //Checks if the user has decided to change languages
        language_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load English View
                            System.out.println("languages");
                            englishButtons_Labels();

                        } else if (newValue.intValue() == 1) {
                            //Load Spanish View
                            spanishButtons_Labels();

                        }
                    }

                });

    }

    //Set the choices for Filter
    public void setFilterChoices(){
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        if(c_language == 0) {
            if(second) {
                filter_ChoiceBox.getItems().remove(0,3);
                filter_ChoiceBox.getItems().addAll("All", "Employees", "Services");
                filter_ChoiceBox.getSelectionModel().select(0);
            }else{
                filter_ChoiceBox.getItems().addAll("All", "Employees", "Services");
                filter_ChoiceBox.getSelectionModel().select(0);
            }
        }else if(c_language == 1){
            if(second) {
                filter_ChoiceBox.getItems().remove(0,3);
                filter_ChoiceBox.getItems().addAll("Todo", "Empleados", "Servicios");
                filter_ChoiceBox.getSelectionModel().select(0);
            }else{
                filter_ChoiceBox.getItems().addAll("Todo", "Empleados", "Servicios");
                filter_ChoiceBox.getSelectionModel().select(0);
            }
        }

        //Checks if the user has decided to change languages
        filter_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load everything

                        } else if (newValue.intValue() == 1) {
                            //Load only employees

                        } else if (newValue.intValue() == 2) {
                            //Load services

                        }else if(newValue.intValue() == 3){
                            //load frequently searched

                        }else if(newValue.intValue() == 4){
                            //Miscellaneous
                        }
                    }

                });
    }



    //Sets the map of the desired floor
    public void setFloorChoices(){

        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        if(c_language == 0) {
            if(second) {
                floor_ChoiceBox.getItems().remove(0,13);
                floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Outside",
                        "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Belkin Basement");
                floor_ChoiceBox.getSelectionModel().select(0);
            }else{
                floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Outside",
                        "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Belkin Basement");
                floor_ChoiceBox.getSelectionModel().select(0);
            }
        }else if(c_language == 1){
            if(second) {
                floor_ChoiceBox.getItems().remove(0,13);
                floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Afuera",
                        "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Sotano de Belkin");
                floor_ChoiceBox.getSelectionModel().select(0);
            }else{
                floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Afuera",
                        "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Sotano de Belkin");
                floor_ChoiceBox.getSelectionModel().select(0);
            }

        }

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
                System.out.println("currentfloor updated to: " + currentFloor);

                if (currentFloor == 0) {
                    System.out.println("outside");
                    outside = true;
                    if (c_language == 0) {
                        currentF = "Outside";
                    } else {
                        currentF = "Afuera";
                    }
                }

                if (currentFloor == 8) {
                    //outside
                    outside = true;
                    currentF = "Belkin 1";

                } else if (currentFloor == 9) {
                    //belkin
                    outside = true;
                    currentF = "Belkin 2";

                } else if (currentFloor == 10) {
                    outside = true;
                    currentF = "Belkin 3";

                } else if (currentFloor == 11) {
                    outside = true;
                    currentF = "Belkin 4";

                } else if (currentFloor == 12) {
                    outside = true;
                    if (c_language == 0) {
                        currentF = "Belkin Basement";
                    } else {
                        currentF = "Sotano de Belkin";
                    }
                }


                mapImage newMapImage = new proxyMap(currentFloor);
                newMapImage.display(map_viewer);

                if (!outside) {
                    c_Floor_Label.setText(Integer.toString(currentFloor));
                    if (c_language == 0) {
                        floor_Label.setText("Floor");
                    } else {
                        floor_Label.setText("Piso");
                    }
                } else {
                    c_Floor_Label.setText("");
                    floor_Label.setText(currentF);
                }
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), false, currentFloor);
            }

        });

    }

    public void setStartEndChoices(){

        ArrayList<String> roomNums = new ArrayList<>();
        ArrayList<String> professionals = new ArrayList<>();
        ArrayList<String> all = new ArrayList<>();

        roomNums = databaseController.getRoomList();
        professionals = databaseController.getProfessionalList();
        all.addAll(roomNums);
        all.addAll(professionals);


        TextFields.bindAutoCompletion(start_textField, all);
        TextFields.bindAutoCompletion(end_TextField, all);

        start_textField.setText("Kiosk");
        selectionState = 0;
    }

    public void aboutButton_clicked() {
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/aboutPageView.fxml");
        aboutPage.aboutPageController controller = loader.getController();
        //controller.setC_language(c_language);

    }


    //Handles the action when the submit button is clicked
    public void submitButton_Clicked() {
        Node startN;
        Node endN;

        //reset visibility just in case
        continueNew_Button.setVisible(false);
        previous_Button.setVisible(false);

        if (selectionState == 2) {
            //submit stuff
            //createEdgeLines

            //set the node if the 1st kiosk location is set
            if (!(start_textField.getText().equals(""))) {
                if (start_textField.getText().equals("Kiosk")){
                    startN = mapController.getCollectionOfNodes().getNodeWithName("Kiosk");
                } else {
                    startN = mapController.getCollectionOfNodes().getNodeWithName(start_textField.getText().split(", ")[1]);
                }
                MapController.getInstance().markNode(startN.getPosX(), startN.getPosY(), 1, startN.getFloor());
            }

            //check for multifloor
            if (mapController.areDifferentFloors()) {
                System.out.println("Multi-floor pathfinding detected!");

                //use the multifloor pathfinding function
                multiFloorPathfind();
            } else {
                MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
                path = mapController.requestPath();
                graph.createEdgeLines(path, true);
                //zoomPath = path;
                controllers.MapOverlay.setPathfinding(1);
                textDescription_TextFArea.setText(mapController.getTextDirections(path, c_language));
                
            }

        } else { //not the map :)


            //check that the txt fields are filled
            if(!(start_textField.getText().equals("")) && !(end_TextField.getText().equals(""))) {
                if (start_textField.getText().equals("Kiosk")){
                    startN = mapController.getCollectionOfNodes().getNodeWithName("Kiosk");
                } else {
                    startN = mapController.getCollectionOfNodes().getNodeWithName(start_textField.getText().split(", ")[1]);
                }
                endN = mapController.getCollectionOfNodes().getNodeWithName(end_TextField.getText().split(", ")[1]);

                //set up for colors :)
                startX = startN.getPosX();
                startY = startN.getPosY();
                endX = endN.getPosX();
                endY = endN.getPosY();

                //mark the nodes
                MapController.getInstance().markNode(startN.getPosX(), startN.getPosY(), 1, startN.getFloor());
                MapController.getInstance().markNode(endN.getPosX(), endN.getPosY(), 2, endN.getFloor());

                //detect multiflooring
                if (startN.getFloor() != endN.getFloor()) {
                    //multifloor pathfinding detected
                    System.out.println("directory -> multifloor pathfinding");


                    multiFloorPathfind();
                } else {
                    //no multifloor pathfinding (simple)

                    MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
                    ArrayList<Edge> path = mapController.requestPath();
                    graph.createEdgeLines(path, true);
                    textDescription_TextFArea.setText(mapController.getTextDirections(path, c_language));
                }
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
        continueNew_Button.setVisible(true);

        //switch floors to original floor's pathfinding view
        int startfloor = mapController.returnOriginalFloor();
        c_Floor_Label.setText(Integer.toString(startfloor));

        //switch back to the original floor using the choicebox selection
        floor_ChoiceBox.getSelectionModel().select(startfloor - 1);

        //maintain consistency of colors
        ArrayList<Circle> tempCircleList;
        tempCircleList = graph.getButtonList();
        for (Circle c: tempCircleList) {
            if(c.getLayoutX() == startX && c.getLayoutY() == startY) {
                c.setStrokeWidth(strokeRatio);
                c.setRadius(graph.getLabelRadius()*sizeUpRatio);
                c.setStroke(startColor);
                c.setFill(startColor);
                break;
            }
        }


        //reset for next pathfinding session
        MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
        ArrayList<Edge> reqPath = mapController.requestPath();
        if (reqPath == null) { //can't find path, reset
            System.out.println("Could not pathfind. Resetting now...");
            cancelButton_Clicked();
        } else {
            textDescription_TextFArea.setText(mapController.getTextDirections(reqPath, c_language));

            ArrayList<ArrayList<Edge>> fragPath;
            fragPath = mapController.requestFragmentedPath(reqPath, mapController.returnOriginalFloor(), mapController.returnDestFloor());

            //loop and display the edges per floor - use the startfloor


            if (fragPath.get(0).size() == 0) {
                //only occurs if the first transition is a null
                //instead just highlight the first thing

                //todo -> highlight

            } else {
                graph.createEdgeLines(fragPath.get(0), true);
                controllers.MapOverlay.setPathfinding(2);
            }

            //set the globals so you can send to the continue button
            globalFragList = fragPath;
            globalFloorSequence = mapController.getFloorSequence();

            //print floor sequence (testing)
            for (int i = 0; i < globalFloorSequence.size(); i++) {
                System.out.println(globalFloorSequence.get(i));
            }
        }


    }

    //Handling when the logIn Button is clicked
    public void logInButton_Clicked() {

        controllers.MapOverlay.setZoom(1.0);

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminLoginMainView.fxml");
        adminLoginMain.adminLoginMainController controller = loader.getController();
        //sends the current language the next screen
        controller.setC_language(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
    }

    //Checks if the emergency button was clicked
    public void emergencyButton_Clicked() {
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
        }
    }

    //Handles the action when the clear button is clicked
    public void cancelButton_Clicked(){
        //MapController.getInstance().requestMapCopy();
        selectionState = 0;

        //Remove colored dots from map

        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false, currentFloor);
        c_Floor_Label.setText(Integer.toString(currentFloor));

        //wipe line from map
        graph.wipeEdgeLines();
        controllers.MapOverlay.setPathfinding(0);

        //hide the continue button
        continueNew_Button.setVisible(false);

        //reset the textfields
        start_textField.setText("");
        end_TextField.setText("");

        //reset the usingMap
        usingMap = false;
    }

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        c_language = 0;
        //change the current language to english

        //Change the Buttons
        admin_Button.setText("Administrator");
        emergency_Button.setText("EMERGENCY");
        cancel_Button.setText("Clear");
        submit_Button.setText("Submit");
        phoneSend.setText("Send");
        about_Button.setText("About");


        //Change the labels
        start_Label.setText("Start");
        end_Label.setText("End");
        mainTitle_Label.setText("Welcome to Brigham and Women's Faulkner Hospital");
        floor_Label.setText("Floor");
        textD_Label.setText("Text Description");
        phoneInfo_Label.setText("Send Directions to my phone");

        //Change the textFields
        start_textField.setPromptText("Starting position");
        end_TextField.setPromptText("Ending position");

        //Change choiceBox
        setFilterChoices();
        setFloorChoices();


    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        admin_Button.setText("Administrador");
        emergency_Button.setText("EMERGENCIA");
        cancel_Button.setText("Borrar");
        submit_Button.setText("Listo");
        phoneSend.setText("Enviar");
        about_Button.setText("Acerca");

        //change the Labels
        start_Label.setText("Inicio");
        end_Label.setText("Destino");
        mainTitle_Label.setText("Bienvenidos al Hospital Faulkner Brigham and Women");
        floor_Label.setText("Piso");
        textD_Label.setText("Descripciones Escritas");
        phoneInfo_Label.setText("Enviar direcciones a mi celular");


        //Change the textFields
        start_textField.setPromptText("Nombre de inicio");
        end_TextField.setPromptText("Nombre del destino");

        //Change choiceBox
        setFilterChoices();
        setFloorChoices();


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }


    public void sceneEvent(int x, int y, Circle c){
        //set selectionstate
        if (!usingMap) {
            System.out.println("not using map");
            if (!(start_textField.getText().equals(""))) {
                //reset the map display
                if(start != null) {
                    start.setStroke(Color.BLACK);
                    start.setFill(Color.BLACK);
                    start.setStrokeWidth(1);
                    start.setRadius(graph.getLabelRadius());
                }
                if(end != null) {
                    end.setStroke(Color.BLACK);
                    end.setFill(Color.BLACK);
                    end.setStrokeWidth(1);
                    end.setRadius(graph.getLabelRadius());
                }
                graph.wipeEdgeLines();

                //set the correct selection state
                selectionState = 1;
            } else {
                usingMap = true;
                selectionState = 0;
            }
        }
        System.out.println("Node at (" + x + ", " + y + ") selected during state: " + selectionState);
        if (selectionState == 0) {
            //place the black marker at the starting location
            mapController.markNode(x, y, 1, currentFloor);
            selectionState++;
            if(start != null) {
                start.setStroke(Color.BLACK);
                start.setFill(Color.BLACK);
                start.setStrokeWidth(1);
                start.setRadius(graph.getLabelRadius());
            }
            if(end != null) {
                end.setStroke(Color.BLACK);
                end.setFill(Color.BLACK);
                end.setStrokeWidth(1);
                end.setRadius(graph.getLabelRadius());
            }
            graph.wipeEdgeLines();
            controllers.MapOverlay.setPathfinding(0);

            start =c;
            //color
            c.setStrokeWidth(strokeRatio);
            c.setStroke(startColor);
            c.setFill(startColor);

            //location
            startX = c.getLayoutX();
            startY = c.getLayoutY();
            System.out.println("Start coords updated: " + startX + "," + startY);

            //size
            c.setRadius(graph.getLabelRadius() * sizeUpRatio);

            //hide the continue button if possible
            continueNew_Button.setVisible(false);
        } else if (selectionState == 1){
            //place the red marker at end location
            mapController.markNode(x, y, 2, currentFloor);
            selectionState++;
            end = c;
            //color
            c.setStrokeWidth(strokeRatio);
            c.setStroke(endColor);
            c.setFill(endColor);

            //location
            endX = c.getLayoutX();
            endY = c.getLayoutY();
            System.out.println("End coords updated: " + endX + "," + endY);

            //size
            c.setRadius(graph.getLabelRadius() * sizeUpRatio);
        } else {
            //do nothing
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
    void directoryClicked() {

    }

    //abstracted floor refresh for multifloor pathfinding
    public void multifloorUpdate() {
        System.out.println("cf: " + currentFloor + "   size: " + globalFragList.get(fragPathPos).size());

        //otherwise, change to the appropriate screen and display edges
        graph.wipeEdgeLines();
        System.out.println("multifloor update called. Currentfloor = " + currentFloor);
        if (currentFloor == 0) {
            System.out.println("currentfloor outside!!!!");
            floor_ChoiceBox.getSelectionModel().select(7);
        } else if (currentFloor > 7) {
            floor_ChoiceBox.getSelectionModel().select(currentFloor);

        } else {
            floor_ChoiceBox.getSelectionModel().select(currentFloor - 1);
        }
        controllers.MapOverlay.setPathfinding(0);
        System.out.println("creating edge lines for fp pos: " + fragPathPos);
        graph.createEdgeLines(globalFragList.get(fragPathPos), true);
        controllers.MapOverlay.setPathfinding(2);
    }

    public void zoomButton_Clicked() {
        zoom = controllers.MapOverlay.getZoom();
        System.out.println(zoom);
        if (zoom < 1.6) {
            zoom += 0.3;
            controllers.MapOverlay.setZoom(zoom);
            node_Plane.setPrefWidth(origPaneWidth*zoom);
            node_Plane.setPrefHeight(origPaneHeight*zoom);
            map_viewer.setFitWidth(origPaneWidth*zoom);
            map_viewer.setFitHeight(origPaneHeight*zoom);



        } else {
            System.out.println("set to 1.0");
            zoom = 1.0;
            controllers.MapOverlay.setZoom(1);
            node_Plane.setPrefWidth(origPaneWidth);
            node_Plane.setPrefHeight(origPaneHeight);
            map_viewer.setFitWidth(origPaneWidth);
            map_viewer.setFitHeight(origPaneHeight);


        }
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),
                false, currentFloor);
        if (controllers.MapOverlay.getPathfinding() == 1) {
            graph.createEdgeLines(path, true);
        } else if (controllers.MapOverlay.getPathfinding() == 2) {
            graph.createEdgeLines(globalFragList.get(fragPathPos), true);
        }

    }
}
