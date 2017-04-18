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
    private Label phoneInfo_Label;

    @FXML
    private TextField phoneInsert;

    @FXML
    private Button phoneSend;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    @FXML
    private Label floor_Label;

    @FXML
    private Label c_Floor_Label;

    @FXML
    private ChoiceBox<String> filter_ChoiceBox;

    @FXML
    private Pane node_Plane;

    @FXML
    private ImageView map_viewer;

    @FXML
    private Button continue_Button;

    @FXML
    private Label phoneStatus;

    int c_language = 0;

    int first_Time = 0;

    boolean second = false;

    private int currentFloor;

    private double startX;
    private double startY;

    private Circle btK;

    private controllers.MapOverlay graph;

    private int selectionState = 0;

    private MapController mapController = MapController.getInstance();

    private Circle start;
    private Circle end;

    private final double sizeUpRatio = 1.7;
    private final double strokeRatio = 2.5;

    private ArrayList<Edge> [] globalFragList;

    @FXML
    public void initialize(){
        graph = new controllers.MapOverlay(node_Plane,(mapScene) this);
        MapController.getInstance().requestMapCopy();

        //setLanguageChoices(c_language);
        setFloorChoices();
        setStartEndChoices();
        setLanguage_ChoiceBox();
        setComboBox();
        setFilterChoices();
        second = true;
        //set current floor
        //we will use floor 1 as default
        currentFloor = 1;
        c_Floor_Label.setText("1");

        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);
        //set continue button invisible when not needed
        continue_Button.setVisible(false);
    }

    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();


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

    public void setComboBox(){
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        if(c_language == 0) {
            filter_ChoiceBox.getSelectionModel().clearSelection();
            filter_ChoiceBox.getItems().clear();
            filter_ChoiceBox.getItems().addAll("All", "Employees", "Services", "Frequently Searched", "Miscellaneous");
            filter_ChoiceBox.getSelectionModel().select(0);
        }else if(c_language == 1){
            filter_ChoiceBox.getSelectionModel().clearSelection();
            filter_ChoiceBox.getItems().clear();
            filter_ChoiceBox.getItems().addAll("Todo", "Empleados", "Servicios", "Buscados Frequentemente", "Varios");
            filter_ChoiceBox.getSelectionModel().select(0);

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
                            System.out.println("Hello World");
                            //Miscellaneous
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
                filter_ChoiceBox.getItems().remove(0,5);
                filter_ChoiceBox.getItems().addAll("All", "Employees", "Services", "Frequently Searched", "Miscellaneous");
                filter_ChoiceBox.getSelectionModel().select(0);
            }else{
                filter_ChoiceBox.getItems().addAll("All", "Employees", "Services", "Frequently Searched", "Miscellaneous");
                filter_ChoiceBox.getSelectionModel().select(0);
            }
        }else if(c_language == 1){
            if(second) {
                filter_ChoiceBox.getItems().remove(0,5);
                filter_ChoiceBox.getItems().addAll("Todo", "Empleados", "Servicios", "Buscados Frequentemente", "Varios");
                filter_ChoiceBox.getSelectionModel().select(0);
            }else{
                filter_ChoiceBox.getItems().addAll("Todo", "Empleados", "Servicios", "Buscados Frequentemente", "Varios");
                filter_ChoiceBox.getSelectionModel().select(0);
            }

        }

        //filter_ChoiceBox.getItems().removeAll();
        //filter_ChoiceBox.getItems().remove(filter_ChoiceBox);
        //filter_ChoiceBox.setValue("X");
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

                c_Floor_Label.setText(Integer.toString(currentFloor));
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);
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


    }

    public void aboutButton_clicked() {
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/aboutPageView.fxml");
        aboutPage.aboutPageController controller = loader.getController();
        //controller.setC_language(c_language);

    }


    //Handles the action when the submit button is clicked
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
        //MapController.getInstance().requestMapCopy();
    }

    public void multiFloorPathfind() {
        //initialize reference of the global frag list to null (set up)
        globalFragList = null;

        //set continue button visible
        continue_Button.setVisible(true);

        //switch floors to original floor's pathfinding view
        int startfloor = mapController.returnOriginalFloor();
        c_Floor_Label.setText(Integer.toString(startfloor));
        System.out.println("startfloor: " + Integer.toString(startfloor));

        //switch back to the original floor using the choicebox selection
        floor_ChoiceBox.getSelectionModel().select(startfloor - 1);
        System.out.println("Current floor: " + Integer.toString(currentFloor) + " :)");

        //maintain consistency of colors - doesn't work - references go missing
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
        ArrayList<Edge> [] fragPath;
        fragPath = mapController.requestFragmentedPath(reqPath, mapController.returnOriginalFloor(), mapController.returnDestFloor());
        System.out.println("=====================");

        System.out.println("printing the fragmented path, floor = " + Integer.toString(startfloor));
        //loop and display the edges per floor - use the startfloor


        if (fragPath[startfloor] == null) {
            //only occurs if the first transition is a null
            //instead just highlight the first thing

            //to do -> highlight

        } else {
            graph.createEdgeLines(fragPath[startfloor]);
        }

        //set the global so you can send to the continue button
        globalFragList = fragPath;


    }

    //Handling when the logIn Button is clicked
    public void logInButton_Clicked() {

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

        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),false);
        c_Floor_Label.setText(Integer.toString(currentFloor));

        //wipe line from map
        graph.wipeEdgeLines();

        //hide the continue button
        continue_Button.setVisible(false);

        start_textField.setText("");
        end_TextField.setText("");
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

        //Change the textFields
        start_textField.setPromptText("Starting position");
        end_TextField.setPromptText("Ending position");

        //Change choiceBox
        setFilterChoices();


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

        //Change the textFields
        start_textField.setPromptText("Nombre de inicio");
        end_TextField.setPromptText("Nombre del destino");

        //Change choiceBox
        setFilterChoices();


    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
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

    @FXML
    void continueButton_Clicked() {

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

}
