package NewIntroUI;

import DBController.DatabaseController;
import controllers.*;
import emergency.SmsSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by AugustoR on 4/21/17.
 */
public class NewIntroUIController extends controllers.mapScene{
    @FXML
    private Tab textDirections_Tab;

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
    private Button zoomIn_button;

    @FXML
    private Button zoomOut_button;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private StackPane mapStack;

    @FXML
    private Label LogInPerson_Label;

    @FXML
    private Button cancelTo_Button;

    @FXML
    private Button cancelFirst_Button;

    @FXML
    private Button FAQ_Button;

    @FXML
    private Button DirectoryMan_Button;

    @FXML
    private Button adminMan_Button;

    @FXML
    private Button MapMan_Button;

    @FXML
    private CheckBox ThreeDPATH_CheckBox;





    int c_language = 0;

    int first_Time = 0;

    boolean second = false;

    private int currentFloor;

    private double startX;
    private double startY;
    private double endX;
    private double endY;

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

//    private final Color startColor = Color.GREEN;
//    private final Color endColor = Color.CRIMSON;
    private final Color startColor = Color.CRIMSON;
    private final Color endColor = Color.GREEN;
    private final Color kioskColor = Color.ORANGE;

    private double origPaneWidth;
    private double origPaneHeight;
    double zoom;
    double heightRatio = (1050.0/489.0);
    double widthRatio = (1600.0/920.0);

    private int permissionLevel;

    double currentHval = 0;
    double currentVval = 0;

    private boolean useStairs;

    //ArrayList<Edge> zoomPath;


    @FXML
    public void initialize() {
        permissionLevel = 0;
        graph = new controllers.MapOverlay(node_Plane, (mapScene) this);
        MapController.getInstance().requestMapCopy();

        graph.setZoom(1.0);

        //setLanguageChoices(c_language);
        setFloorChoices();
        setStartEndChoices();
        setLanguage_ChoiceBox(c_language);
        //setComboBox();
        //setFilterChoices();
        //set current floor
        //we will use floor 1 as default
        currentFloor = 1;
        c_Floor_Label.setText("1");
        useStairs = false;

        System.out.println("width/height ratios: " + widthRatio + "/" + heightRatio);

        node_Plane.setMaxWidth(4000.0);
        node_Plane.setMaxHeight(3000.0);
        node_Plane.setPrefHeight(489.0*heightRatio);
        node_Plane.setPrefWidth(920.0*widthRatio);
        map_viewer.setFitHeight(489.0*heightRatio);
        map_viewer.setFitWidth(920.0*widthRatio);
        //map_viewer.fitWidthProperty().bind(node_Plane.widthProperty());
        //map_viewer.fitHeightProperty().bind(node_Plane.heightProperty());

        graph.setWidthRatio(widthRatio);
        graph.setHeightRatio(heightRatio);

        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), false,
                currentFloor, permissionLevel);
        //set continue button invisible when not needed
        continueNew_Button.setVisible(false);
        previous_Button.setVisible(false);
        second = true;

        changeFloor(databaseController.GetKioskFloor() - 1);
        floor_ChoiceBox.getSelectionModel().select(databaseController.GetKioskFloor() - 1);

        //draw edges
        //graph.drawFloorEdges(currentFloor);
        origPaneHeight = 489;
        origPaneWidth = 920;

        //detects scrolling for zoom while keeping scrollpane from panning with mousewheel
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            mapScroll(event);
            event.consume();
        });
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
    }

    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    public void drawCircleList(ArrayList<Circle> circleList, double x, double y, Color color) {
        for (Circle c : circleList) {
            //System.out.println((c.getLayoutX()/zoom)/widthRatio);
            if (c.getLayoutX() == x && c.getLayoutY() == y) {
                c.setStrokeWidth(strokeRatio);
                c.setRadius(graph.getLabelRadius());
                c.setStroke(color);
                if (c.getFill().equals(kioskColor)) {
                    c.setFill(kioskColor);
                } else if (c.getFill() != Color.BLACK) {
                    c.setFill(color);
                }
                break;
            }
        }
    }

    //Manage the clearing of the from text field
    public void cancelFromButton_Clicked(){
        start_textField.setText("");
    }

    //Manage the clearing tof the to text field
    public void cancelToButton_Clicked(){
        end_TextField.setText("");
    }

    //Button to reverse the from and to location
    public void reverseButton_Clicked(){
        String start =start_textField.getText();
        String end = end_TextField.getText();
        if( !(start == null || start.equals("") || end == null ||end.equals("")) ){
            start_textField.setText(end);
            end_TextField.setText(start);
            submitButton_Clicked();

        }
    }
    //Continue New Button Clicked
    public void continueNewButton_Clicked() {
        if (continueNew_Button.isVisible() == true) {
            System.out.println("continue button clicked");

            //set the previous button to be enabled
            previous_Button.setVisible(true);

            //increment b/c continue button
            fragPathPos++; //continue...

            //update currentfloor
            currentFloor = globalFloorSequence.get(fragPathPos);

            System.out.println("+++++++++++++++++++++++++++++++++++++++");
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

                drawCircleList(circleList, endX, endY, endColor);
            }
        }
    }

    //previous Button clicked
    public void previousButton_Clicked() {
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

            drawCircleList(circleList, startX, startY, startColor);
        }
    }

    //Sets the choices for the language
    public void setLanguage_ChoiceBox(int lang) {
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        if(second) {
            language_ChoiceBox.getItems().remove(0, 2);
            language_ChoiceBox.getItems().addAll("English", "Espanol");
            language_ChoiceBox.getSelectionModel().select(lang);
        }else{
            language_ChoiceBox.getItems().addAll("English", "Espanol");
            language_ChoiceBox.getSelectionModel().select(lang);

        }
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
    /*public void setFilterChoices() {
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        if (c_language == 0) {
            if (second) {
                filter_ChoiceBox.getItems().remove(0, 3);
                filter_ChoiceBox.getItems().addAll("All", "Employees", "Services");
                filter_ChoiceBox.getSelectionModel().select(0);
            } else {
                filter_ChoiceBox.getItems().addAll("All", "Employees", "Services");
                filter_ChoiceBox.getSelectionModel().select(0);
            }
        } else if (c_language == 1) {
            if (second) {
                filter_ChoiceBox.getItems().remove(0, 3);
                filter_ChoiceBox.getItems().addAll("Todo", "Empleados", "Servicios");
                filter_ChoiceBox.getSelectionModel().select(0);
            } else {
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

                        } else if (newValue.intValue() == 3) {
                            //load frequently searched

                        } else if (newValue.intValue() == 4) {
                            //Miscellaneous
                        }
                    }

                });
    }*/

    //Changes the floors on the map
    public void changeFloor(Number newValue) {
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
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), false,
                currentFloor, permissionLevel);

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
                changeFloor(newValue);
            }
        });

    }

    public void setStartEndChoices(){

        ArrayList<String> roomNums = new ArrayList<>();
        ArrayList<String> professionals = new ArrayList<>();
        ArrayList<String> all = new ArrayList<>();

        roomNums = databaseController.getFilteredRoomList(permissionLevel);
        professionals = databaseController.getProfessionalList();
        all.addAll(roomNums);
        all.addAll(databaseController.getFilteredRooms(permissionLevel));
        all.addAll(professionals);


        TextFields.bindAutoCompletion(start_textField, all);
        TextFields.bindAutoCompletion(end_TextField, all);

        start_textField.setText("Kiosk");
        selectionState = 0;
    }

    //Sends the user to the about page
    public void aboutButton_clicked() {
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/aboutPageView.fxml");
        aboutPage.aboutPageController controller = loader.getController();
        controller.//sets the current language given information form other screens
        setCurrentLanguage(c_language);
        if(c_language == 0){
            controller.englishLabels();
        }else{
            controller.spanishLabels();
        }
        controller.setPermissionLevel(getPermissionLevel());
        controller.setAdmin(LogInPerson_Label.getText());

    }


    //Handles the action when the submit button is clicked
    public void submitButton_Clicked() {
        Node startN;
        Node endN;
        System.out.println("submit button clicked");
        System.out.println("click - pane-Hval = " + scrollPane.getHvalue());
        System.out.println("click - pane-Vval = " + scrollPane.getVvalue());


        //reset visibility just in case
        continueNew_Button.setVisible(false);
        previous_Button.setVisible(false);

//        if (selectionState == 2) {
//            //submit stuff
//            //createEdgeLines
//
//            //set the node if the 1st kiosk location is set
//            if (!(start_textField.getText().equals(""))) {
//                if (start_textField.getText().equals("Kiosk")){
//                    startN = mapController.getCollectionOfNodes().getNodeWithName("Kiosk");
//                } else {
//                    startN = mapController.getCollectionOfNodes().getNodeWithName(start_textField.getText().split(", ")[1]);
//                }
//                MapController.getInstance().markNode(startN.getPosX(), startN.getPosY(), 1, startN.getFloor());
//            }
//
//            //check for multifloor
//            if (mapController.areDifferentFloors()) {
//                System.out.println("Multi-floor pathfinding detected!");
//
//                //use the multifloor pathfinding function
//                multiFloorPathfind();
//            } else {
//                MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
//                path = mapController.requestPath(permissionLevel);
//                graph.createEdgeLines(path, true, false);
//                //zoomPath = path;
//                graph.setPathfinding(1);
//                textDescription_TextFArea.setText(mapController.getTextDirections(path, c_language));
//
//            }
//
//        } else {
//

        //check that the txt fields are filled
        if(!(start_textField.getText().equals("")) && !(end_TextField.getText().equals(""))) {
            if (start_textField.getText().equals("Kiosk")){
                startN = mapController.getCollectionOfNodes().getNodeWithName("Kiosk");
            } else if (start_textField.getText().contains(",")){
                startN = mapController.getCollectionOfNodes().getNodeWithName(start_textField.getText().split(", ")[1]);
            } else {
                startN = mapController.getCollectionOfNodes().getNodeWithName(start_textField.getText());
            }

            if (end_TextField.getText().contains(",")){
                endN = mapController.getCollectionOfNodes().getNodeWithName(end_TextField.getText().split(", ")[1]);
            } else {
                endN = mapController.getCollectionOfNodes().getNodeWithName(end_TextField.getText());
            }

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
                path = mapController.requestPath(permissionLevel, useStairs);

                int startfloor = mapController.returnOriginalFloor();
                if(startfloor != currentFloor) {
                    c_Floor_Label.setText(Integer.toString(startfloor));

                    //switch back to the original floor using the choicebox selection
                    if (startfloor == 0) {
                        floor_ChoiceBox.getSelectionModel().select(7);
                    } else if (startfloor > 7) {
                        floor_ChoiceBox.getSelectionModel().select(startfloor);

                    } else {
                        floor_ChoiceBox.getSelectionModel().select(startfloor - 1);
                    }
                }

                graph.setPathfinding(1);
                textDescription_TextFArea.setText(mapController.getTextDirections(path, c_language));
                setMapToPath(startX, startY, endX, endY);
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false,
                        currentFloor, permissionLevel);
                graph.createEdgeLines(path, true, false);

            }

            if(ThreeDPATH_CheckBox.isSelected()){
                //mapController.nodeListToText(pathfinder.getNodePath());
                try {
                    Runtime.getRuntime().exec(new String[] { "pathfinder3D.exe"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




        System.out.println("finished - pane-Hval = " + scrollPane.getHvalue());
        System.out.println("finished - pane-Vval = " + scrollPane.getVvalue());
    }

    //Allows the user to go through several floors while using pathfinding
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
        if (startfloor == 0) {
            floor_ChoiceBox.getSelectionModel().select(7);
        } else if (startfloor > 7) {
            floor_ChoiceBox.getSelectionModel().select(startfloor);

        } else {
            floor_ChoiceBox.getSelectionModel().select(startfloor - 1);
        }

        //maintain consistency of colors
        ArrayList<Circle> tempCircleList;
        tempCircleList = graph.getButtonList();

        drawCircleList(tempCircleList, round(startX), round(startY), startColor);
        System.out.println("start coords: "+startX + "  " +startY);


        //reset for next pathfinding session
        MapController.getInstance().getCollectionOfNodes().resetForPathfinding();
        ArrayList<Edge> reqPath = mapController.requestPath(permissionLevel, useStairs);
        if (reqPath == null || reqPath.size() == 0) { //can't find path, reset
            System.out.println("Could not pathfind. Resetting now...");
            cancelButton_Clicked();
            start_textField.setText("Kiosk");
        } else {
            System.out.println("reqpath size" + reqPath.size());
            textDescription_TextFArea.setText(mapController.getTextDirections(reqPath, c_language));

            ArrayList<ArrayList<Edge>> fragPath;
            fragPath = mapController.requestFragmentedPath(reqPath, mapController.returnOriginalFloor(), mapController.returnDestFloor());

            //loop and display the edges per floor - use the startfloor


            if (fragPath.get(0).size() == 0) {
                //only occurs if the first transition is a null
                //instead just highlight the first thing

                //todo -> highlight

            } else {
                graph.createEdgeLines(fragPath.get(0), true, false);
                graph.setPathfinding(2);
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

        graph.setZoom(1.0);
        graph.setHeightRatio(1.0);
        graph.setWidthRatio(1.0);

        if(getPermissionLevel() == 0 ) {
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminLoginMainView.fxml");
            adminLoginMain.adminLoginMainController controller = loader.getController();
            //sends the current language the next screen
            controller.setC_language(c_language);
            //set up english labels
            if (c_language == 0) {
                controller.englishButtons_Labels();

                //set up spanish labels
            } else if (c_language == 1) {
                controller.spanishButtons_Labels();
            }
            //Sets the current Language choices
            controller.setLanguageChoiceBox(c_language);

            //Signing out
        }else{
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
            //patientMenuStart.patientMenuStartController controller = loader.getController();
            NewIntroUI.NewIntroUIController controller = loader.getController();
            //sets the current language
            controller.setCurrentLanguage(c_language);
            //set up english labels
            if(c_language == 0){
                controller.englishButtons_Labels();
                //set up spanish labels
            }else if(c_language == 1){
                controller.spanishButtons_Labels();
            }
            //set permissions back
            controller.setPermissionLevel(0);
            //set label to empty
            controller.setWelcome("");

            //Sets the label of the button back to administrator
            //0 In 1 out
            controller.loginOrOut(1, c_language);
        }

    }

    //Set the button correctly
    public void loginOrOut(int inOrOut, int lang){
        //The user is signing in
        if(inOrOut == 0){
            if(lang == 0){
                admin_Button.setText("Sign Out");
            }else{
                admin_Button.setText("Salir");
            }

        }else{
            if(lang == 0){
                admin_Button.setText("Administrator");
            }else{
                admin_Button.setText("Administrador");
            }

        }

    }

    //Checks if the emergency button was clicked
    public void emergencyButton_Clicked() {
        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewEmergencyView.fxml");
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

        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false,
                currentFloor, permissionLevel);
        c_Floor_Label.setText(Integer.toString(currentFloor));

        //wipe line from map
        graph.wipeEdgeLines();
        graph.setPathfinding(0);

        //hide the continue button
        continueNew_Button.setVisible(false);
        previous_Button.setVisible(false);

        //reset the textfields
        start_textField.setText("");
        end_TextField.setText("");

        //reset any colors

    }

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        c_language = 0;
        //change the current language to english

        //Change the Buttons
        //change the Buttons
        if(permissionLevel == 2) {
            admin_Button.setText("Sign Out");
        }else {
            admin_Button.setText("Administrator");
        }
        emergency_Button.setText("EMERGENCY");
        cancel_Button.setText("Clear");
        submit_Button.setText("Submit");
        phoneSend.setText("Send");
        about_Button.setText("About");
        MapMan_Button.setText("Map Management");
        adminMan_Button.setText("Admin Management");
        DirectoryMan_Button.setText("Directory Management");


        //Change the labels
        start_Label.setText("From:");
        end_Label.setText("To:");
        mainTitle_Label.setText("Welcome to Brigham and Women's Faulkner Hospital");
        floor_Label.setText("Floor");
        phoneInfo_Label.setText("Send Directions to my phone");

        //Change the textFields
        start_textField.setPromptText("Starting position");
        end_TextField.setPromptText("Ending position");

        //Change Tabs
        textDirections_Tab.setText("Directions");

        //Change choiceBox
        setFloorChoices();


    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;
        FAQ_Button.setVisible(false);

        MapMan_Button.setText("Control de Mapa");
        adminMan_Button.setText("Control de Admins");
        DirectoryMan_Button.setText("Control del Directorio");



        //change the Buttons
        if(permissionLevel == 2) {
            admin_Button.setText("Salir");
        }else {
            admin_Button.setText("Administrador");
        }
        emergency_Button.setText("EMERGENCIA");
        cancel_Button.setText("Borrar");
        submit_Button.setText("Listo");
        phoneSend.setText("Enviar");
        about_Button.setText("Acerca");

        //change the Labels
        start_Label.setText("Inicio:");
        end_Label.setText("Destino:");
        mainTitle_Label.setText("Bienvenidos al Hospital Faulkner Brigham and Women");
        floor_Label.setText("Piso");
        phoneInfo_Label.setText("Enviar direcciones a mi celular");


        //Change the textFields
        start_textField.setPromptText("Nombre de inicio");
        end_TextField.setPromptText("Nombre del destino");

        //Change Tabs
        textDirections_Tab.setText("Direcciones");

        //Change choiceBox
        //setFilterChoices();
        setFloorChoices();


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }


    public void rightClickEvent(int x, int y, Circle c, int mode) {
        System.out.println("Right click event");
    }
    public void edgeClickRemove(int x1, int y1, int x2, int y2){}

    public void sceneEvent(int x, int y, Circle c){
        //set selectionstate


        if (!(start_textField.getText().equals(""))) {
            //reset the map display
            graph.wipeEdgeLines();

            //set the correct selection state
            selectionState = 1;
        } else {

            selectionState = 0;
        }

        System.out.println("Node at (" + x + ", " + y + ") selected during state: " + selectionState);
        if (selectionState == 0) {
            //place the black marker at the starting location
            //mapController.markNode(x, y, 1, currentFloor);

            //update the txtfield
            Node myNode = mapController.getCollectionOfNodes().getNode(x, y, currentFloor);
            start_textField.setText(myNode.getName() + ", " + myNode.getRoomNum());

            selectionState++;

            //reset the colors
            resetMapNodeColors(1);

            graph.wipeEdgeLines();
            graph.setPathfinding(0);

            start = c;
            //color
            c.setStrokeWidth(strokeRatio);
            c.setStroke(startColor);
            if (c.getFill().equals(kioskColor)) {
                c.setFill(kioskColor);
            } else if (c.getFill() == Color.BLACK) {
                c.setFill(startColor);
            }


            //location
            startX = c.getLayoutX();
            startY = c.getLayoutY();
            System.out.println("Start coords updated: " + startX + "," + startY);

            //size
            c.setRadius(graph.getLabelRadius() * sizeUpRatio);

            //hide the continue and prev button if possible
            continueNew_Button.setVisible(false);
            previous_Button.setVisible(false);
        } else if (selectionState == 1){
            //place the red marker at end location
            //mapController.markNode(x, y, 2, currentFloor);

            //set the text field
            Node myNode = mapController.getCollectionOfNodes().getNode(x, y, currentFloor);
            end_TextField.setText(myNode.getName() + ", " + myNode.getRoomNum());

            //reset the colors
            resetMapNodeColors(2);

            //selectionState++;
            end = c;
            //color
            c.setStrokeWidth(strokeRatio);
            c.setStroke(endColor);
            if(c.getFill().equals(kioskColor)){
                c.setFill(kioskColor);
            } else if (c.getFill() == Color.BLACK) {
                c.setFill(endColor);
            }


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

    //resets node fill colors on the map
    public void resetMapNodeColors(int mode) {
        System.out.println("resetting colors");
        if (mode == 1 || mode == 3) {
            if (start != null) {
                System.out.println("start....");
                start.setStroke(Color.BLACK);
                if ((!(start.getFill().equals(kioskColor))) && (start.getFill() == startColor)) {
                    start.setFill(Color.BLACK);
                }
                start.setStrokeWidth(1);

                //reset radius
                if (start.getFill() != Color.BLACK) {
                    start.setRadius(graph.getLabelTypeRadius());
                } else {
                    start.setRadius(graph.getLabelRadius());
                }
            }
        }
        if (mode == 2 || mode == 3) {
            if (end != null) {
                System.out.println("end....");
                end.setStroke(Color.BLACK);
                if ((!(end.getFill().equals(kioskColor))) && (end.getFill() == endColor)) {
                    end.setFill(Color.BLACK);
                }
                end.setStrokeWidth(1);

                //reset radius
                if (end.getFill() != Color.BLACK) {
                    end.setRadius(graph.getLabelTypeRadius());
                } else {
                    end.setRadius(graph.getLabelRadius());
                }
            }
        }
    }

    //Sends feedback according to the outcome of the text directions message
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
        graph.setPathfinding(0);
        System.out.println("creating edge lines for fp pos: " + fragPathPos);
        graph.createEdgeLines(globalFragList.get(fragPathPos), true, false);
        graph.setPathfinding(2);
    }

    public void changeZoom(){
        graph.setZoom(zoom);
        node_Plane.setPrefWidth(origPaneWidth*zoom*widthRatio);
        node_Plane.setPrefHeight(origPaneHeight*zoom*heightRatio);
        map_viewer.setFitWidth(origPaneWidth*zoom*widthRatio);
        map_viewer.setFitHeight(origPaneHeight*zoom*heightRatio);
        System.out.println("zooooomed");
    }


    public void zoomInButton_Clicked() {
        zoom = graph.getZoom();
        System.out.println(zoom);
        if (zoom < 2.2) {
            zoom += 0.03;
            if (zoom > 2.2) {
                zoom = 2.2;
            }
            changeZoom();

            graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),
                    false, currentFloor, permissionLevel);
        }
        scrollPane.setFitToHeight(false);
        scrollPane.setFitToWidth(false);
        if (graph.getPathfinding() == 1) {
            graph.createEdgeLines(path, true, false);
            //set the end goal color
            ArrayList<Circle> circleList;
            circleList = graph.getButtonList();
            drawCircleList(circleList, round(startX*zoom*widthRatio), round(startY*zoom*heightRatio), startColor);
            drawCircleList(circleList, round(endX*zoom*widthRatio), round(endY*zoom*heightRatio), endColor);
            System.out.println("drawing circles at "+startX+" and "+endX);
        } else if (graph.getPathfinding() == 2) {
            graph.createEdgeLines(globalFragList.get(fragPathPos), true, false);
        }

    }

    public void zoomOutButton_Clicked() {
        zoom = graph.getZoom();
        System.out.println(zoom);
        if (zoom > 1.0) {
            zoom = zoom - 0.03;
            if (zoom < 1.0) {
                zoom = 1.0;
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);
            }
            changeZoom();
        } else {
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);
        }

        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),
                false, currentFloor, permissionLevel);
        if (graph.getPathfinding() == 1) {
            graph.createEdgeLines(path, true, false);
            //set the end goal color
            ArrayList<Circle> circleList;
            circleList = graph.getButtonList();
            drawCircleList(circleList, round(startX*zoom*widthRatio), round(startY*zoom*heightRatio), startColor);
            drawCircleList(circleList, round(endX*zoom*widthRatio), round(endY*zoom*heightRatio), endColor);
            System.out.println("drawing circles at "+startX+" and "+endX);
        } else if (graph.getPathfinding() == 2) {
            graph.createEdgeLines(globalFragList.get(fragPathPos), true, false);
        }
    }

    //Let the user scroll through the map
    public void mapScroll(ScrollEvent event) {
        zoom = MapOverlay.getZoom();
        if (currentHval != 0) {
            //System.out.println("pre zoom currenthval: " + currentHval);
            //System.out.println("pre zoom currnetVval: " + currentVval);
        }
        currentHval = scrollPane.getHvalue();
        currentVval = scrollPane.getVvalue();
        if (event.getDeltaY() > 0) {
            if (zoom < 2.2) {
                scrollPane.setFitToHeight(false);
                scrollPane.setFitToWidth(false);
                zoom += 0.03;
                if (zoom > 2.2) {
                    zoom = 2.2;
                }
                changeZoom();
                graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),
                        false, currentFloor, permissionLevel);
            }
        } else if (event.getDeltaY() < 0) {
            if (zoom > 1.0) {
                zoom = zoom - 0.03;
                if (zoom < 1.0) {
                    zoom = 1.0;
                    scrollPane.setFitToHeight(true);
                    scrollPane.setFitToWidth(true);
                }
                changeZoom();
                graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),
                        false, currentFloor, permissionLevel);
            } else {
                scrollPane.setFitToHeight(true);
                scrollPane.setFitToWidth(true);
            }
        }
        if (graph.getPathfinding() == 1) {
            graph.createEdgeLines(path, true, false);
            //set the end goal color
            ArrayList<Circle> circleList;
            circleList = graph.getButtonList();
            drawCircleList(circleList, round(startX*zoom*widthRatio), round(startY*zoom*heightRatio), startColor);
            drawCircleList(circleList, round(endX*zoom*widthRatio), round(endY*zoom*heightRatio), endColor);

        } else if (graph.getPathfinding() == 2) {
            graph.createEdgeLines(globalFragList.get(fragPathPos), true, false);
            Node startN = mapController.getCollectionOfNodes().getNodeWithName(start_textField.getText().split(", ")[1]);
            Node endN = mapController.getCollectionOfNodes().getNodeWithName(end_TextField.getText().split(", ")[1]);


        }

        if (selectionState == 2) {

        }
        scrollPane.setHvalue(currentHval);
        scrollPane.setVvalue(currentVval);
    }


    //Gets the permissions
    public int getPermissionLevel() {
        return permissionLevel;
    }

    //Sets the permissions
    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
        System.out.println("Setting permission level to: " + permissionLevel);
        if(this.permissionLevel >= 1){
            //admin_Button.setVisible(false);
            //TODO FIX THIS
            //signOut_Button.setVisible(true);
        }
    }

    //Starts the string for the current person logged in
    public void setWelcome(String text){
        LogInPerson_Label.setText(text);
    }

    public void setMapToPath(double startNX, double startNY, double endNX, double endNY) {

        System.out.println("setting map to path");
        double deltaX = startNX - endNX;
        double deltaY = startNY - endNY;

        System.out.println("startNX: "+ startNX + " endNX: "+endNX);
        double midX = (startNX + endNX)/2;
        double midY = (startNY + endNY)/2;

        if (deltaX < 0) {
            deltaX = deltaX * -1;
        }
        if (deltaY < 0) {
            deltaY = deltaY * -1;
        }

        double scrollHeight = scrollPane.getHeight();
        double scrollWidth = scrollPane.getWidth();

        zoom = Math.min(Math.min((489/deltaY)*.6,2.2),Math.min((920/deltaX)*.6,2.2));
        System.out.println(deltaY);
        System.out.println("zoom amount: " +zoom);

        System.out.println("plane width: " + node_Plane.getWidth());
        System.out.println("midX: " + midX);
        System.out.println("previous Hvalue: " + scrollPane.getHvalue());
        System.out.println("previous Vvalue: " + scrollPane.getVvalue());

        currentHval = midX / 920;
        currentVval = midY / 489;

        System.out.println("before changezoom");
        changeZoom();
        System.out.println("after changezoom");
        if (graph.getZoom() > 1.0) {
            System.out.println("in if");
            scrollPane.setFitToWidth(false);
            scrollPane.setFitToHeight(false);
        }


        System.out.println("past zoomed");
        secret_Click();
        System.out.println("after secretclick");
    }


    public void setZoom(double zoom) {
        this.zoom = zoom;
    }


    public void FAQ_button_clicked() {
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewFAQ.fxml");
        FAQ.FAQcontroller controller = loader.getController();
        controller.setAdmin(LogInPerson_Label.getText());

        controller.setPermissionLevel(getPermissionLevel());
    }

    private int round(double input) {
        long intPart;
        double decimalPart;
        intPart = (long) input;
        decimalPart = input - intPart;

        if (decimalPart >= 0.5d) {
            return (int) intPart + 1;
        } else {
            return (int) intPart;
        }

    }

    //Sets the buttons to the admin
    public void AdminButtons(int lang){
        if(lang == 0){
            MapMan_Button.setText("Map Management");
            adminMan_Button.setText("Admin Management");
            DirectoryMan_Button.setText("Directory Management");
        }else{
            MapMan_Button.setText("Control de Mapa");
            adminMan_Button.setText("Control de Admins");
            DirectoryMan_Button.setText("Control del Directorio");
        }

        MapMan_Button.setDisable(false);
        adminMan_Button.setDisable(false);
        DirectoryMan_Button.setDisable(false);

        MapMan_Button.setVisible(true);
        adminMan_Button.setVisible(true);
        DirectoryMan_Button.setVisible(true);
    }

    //sends the user to the map management
    public void mapMan_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewMainMapManagementView.fxml");
        NewMainMapManagement.NewMainMapManagementController controller = loader.getController();
        //Set the correct username for the next scene

        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        controller.setUserString(LogInPerson_Label.getText());
        controller.setPermissionLevel(2);


    }

    //sends the user to the admin management
    public void adminMan_Clicked(){
//Change to patient menu
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewAdminManagementView.fxml");
        adminSignUp.adminSignUpController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //Gets the current admin
        controller.setUsername(LogInPerson_Label.getText());

        //set up english labels

        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }

        controller.setUpTreeView();
        controller.setModeChoices();

    }

    //sends the user to the directory management
    public void directoryMan_Clicked(){

        FXMLLoader loader= switch_screen(backgroundAnchorPane, "/views/NewDirectoryManagementView.fxml");
        mapManagementNodeInformation.mmNodeInformationController controller = loader.getController();

        //sets the current language
        controller.setC_language(c_language);

        controller.setModeChoices();
        controller.setRoomChoices();
        controller.setUpTreeView();
        controller.setUser(LogInPerson_Label.getText());

        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        //Set permissions of admin
        controller.setPermissionLevel(2);


    }

    public void secret_Click() {
        System.out.println("in secretclick");
        System.out.println("current-Hval = " + currentHval);
        System.out.println("current-Vval = " + currentVval);
        System.out.println("pane-Hval = " + scrollPane.getHvalue());
        System.out.println("pane-Vval = " + scrollPane.getVvalue());

        scrollPane.setHvalue(currentHval);
        scrollPane.setVvalue(currentVval);

        System.out.println("New Hvalue: " + scrollPane.getHvalue());
        System.out.println("New Vvalue: " + scrollPane.getVvalue());
    }
}




