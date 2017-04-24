package mapManagementFloorAndMode;

import DBController.DatabaseController;
import controllers.MapController;
import controllers.Node;
import controllers.mapScene;
import controllers.proxyMap;
import controllers.mapImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PopOver;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by AugustoR on 3/31/17.
 */
public class mmFloorAndModeController extends controllers.mapScene{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button emergency_Button;

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

    @FXML
    private Button clear_Button;

    //Add the name and Room
    @FXML
    private TextField name_TextField;

    @FXML
    private TextField room_TextField;

    @FXML
    private Pane admin_FloorPane;

    @FXML
    private CheckBox enabled_CheckBox;

    @FXML
    private Label title_Label;

    @FXML
    private Label subTitile_Label;

    @FXML
    private Label nodeTitile_Label;

    @FXML
    private ImageView map_viewer;

    @FXML
    private Label name_Label;

    @FXML
    private Label room_Label;

    @FXML
    private Label hidden_Label;

    @FXML
    private Label enabled_Label;

    @FXML
    private Label floorMap_Label;

    @FXML
    private Label chooseFloor_Label;

    @FXML
    private Label mode_Label;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    private int nodeEdgeX1;
    private int nodeEdgeY1;
    private int nodeEdgeX2;
    private int nodeEdgeY2;

    private int edgesSelected = 0;

    private controllers.MapOverlay graph;

    private static final double labelRadius = 10.5;

    private Node firstNode;

    private Circle lastColoredStart;
    private Circle lastColoredEnd;

    private Circle btK;
    private boolean addSingleEdgeMode;
    private boolean addMultiEdgeMode;


    //Set to english by default
    int c_language = 0;

    private int currentFloor;
    private int floor1;
    private int floor2;

    DatabaseController databaseController = DatabaseController.getInstance();

    public void initialize() {
        setUserString(username_Label.getText());
        setModeChoices();
        setTitleChoices();
        addSingleEdgeMode = false;
        addMultiEdgeMode = false;

        //set default floor to start
        //we will use floor 1 for now
        currentFloor = 1;

        graph = new controllers.MapOverlay(admin_FloorPane,(mapScene) this);
        MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true, currentFloor);

        String type = "", tempName = "", tempRoom = "";

        setFloorChoices();


        // creates a node when clicking the map
        map_viewer.setOnMouseClicked((MouseEvent e) -> {
            btK = new Circle(labelRadius);//new Button();
            btK.setLayoutX(e.getX());
            btK.setLayoutY(e.getY());
            TextField nodeName = new TextField();
            admin_FloorPane.getChildren().add(btK);

            PopOver pop = new PopOver();
            pop.setDetachable(true);
            pop.setDetached(false);
            pop.setCornerRadius(4);
            pop.show(btK);


            Node newNode = new Node((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                    currentFloor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom);
        });

//        // creates a node when clicking the map
//        map_viewer.setOnMouseClicked((MouseEvent e) -> {
//            btK = new Circle(labelRadius);//new Button();
//            btK.setLayoutX(e.getX());
//            btK.setLayoutY(e.getY());
//            TextField nodeName = new TextField();
//
////            PopOver popOver = new PopOver();
////            popOver.setDetachable(true);
////            popOver.setDetached(true);
////            popOver.setCornerRadius(4);
////            popOver.show();
//            btK.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    if (event.getButton() == MouseButton.SECONDARY){
//                        // Create ContextMenu
//                        ContextMenu contextMenu = new ContextMenu();
//
//                        MenuItem item1 = new MenuItem("Remove");
//                        item1.setOnAction(new EventHandler<ActionEvent>() {
//                            @Override public void handle(ActionEvent e) {
//                                admin_FloorPane.getChildren().remove(btK);
//                                // delete from database here
////                                databaseController.deleteNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
////                                        Integer.parseInt(floor_ChoiceBox.getValue()));
//                                //
//                                //
//                            }
//                        });
//                        MenuItem item2 = new MenuItem("Edit");
//                        // Add MenuItem to ContextMenu
//                        contextMenu.getItems().addAll(item1, item2);
//                        contextMenu.show(btK, event.getScreenX(), event.getScreenY());
//                    }
//                }
//            });
//            admin_FloorPane.getChildren().add(btK);
//            Node newNode = new Node((int) btK.getLayoutX(), (int) btK.getLayoutY(),
//                    currentFloor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom);
//        });



    }


    public void emergencyButton_Clicked() {

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

    public void clearButton_Clicked() {
        //if(mode_ChoiceBox.getValue().equals("Add Node")) {
        if ("Add Node".equals(mode_ChoiceBox.getValue())) {
            admin_FloorPane.getChildren().remove(btK);
        }

        name_TextField.clear();
        room_TextField.clear();
        graph.wipeEdgeLines();
        edgesSelected = 0;

        addSingleEdgeMode = false;
        addMultiEdgeMode = false;

        //reset last colored stroke to default
        if (lastColoredStart != null) {
            lastColoredStart.setStroke(lastColoredStart.getFill());
            lastColoredStart.setStrokeWidth(1);
        }

        if (lastColoredEnd != null) {
            lastColoredEnd.setStroke(lastColoredEnd.getFill());
            lastColoredEnd.setStrokeWidth(1);
        }
    }

    //requeries database and resets screen
    public void resetScreen() {
        controllers.MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), true, currentFloor);
        edgesSelected = 0;
    }

    public void rightClickEvent(int x, int y, Circle c, int mode) {
        //CODE TO HANDLE RIGHT CLICK MENU STUFF GOES HERE:
        switch (mode) {
            case 1:
                Node selectedNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                //handle errors
                if (selectedNode == null) {
                    break;
                }
                for (controllers.Edge thisEdge : selectedNode.getEdgeList()) {
                    thisEdge.getNeighbor(selectedNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                databaseController.deleteNode(x, y, currentFloor);
                resetScreen();
                break;
            case 2:
                break;
            case 3:
                MapController.getInstance().attachSurroundingNodes(x, y, currentFloor);
                resetScreen();
                //show some edge lines as visual feedback:
                Node temp = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                graph.createEdgeLines(temp.getEdgeList(), true, true);
                break;
            case 4:
                addSingleEdgeMode = true;

                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                        .getNode(nodeEdgeX1, nodeEdgeY1, currentFloor);
                graph.createEdgeLines(firstNode.getEdgeList(), true, false);

                break;
            case 5:
                addMultiEdgeMode = true;
                break;
            default:
                System.out.println("default. This probably should not have been possible...");
                break;
        }
    }

    //handle a click on an edge.
    public void edgeClickRemove(int x1, int y1, int x2, int y2){
        DBController.DatabaseController.getInstance().deleteEdge(x1,
                y1, currentFloor, x2, y2, currentFloor);
        System.out.println("removed edge on click");
        resetScreen();
        if (firstNode != null) {
            Node temp = MapController.getInstance().getCollectionOfNodes().getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
            graph.createEdgeLines(temp.getEdgeList(), true, true);
        }
    }

    public void sceneEvent(int x, int y, Circle c) {
        edgesSelected++;

        //add edge from menu
        if (edgesSelected == 1 && addSingleEdgeMode) {
            nodeEdgeX2 = (int) x;
            nodeEdgeY2 = (int) y;
            DBController.DatabaseController.getInstance().newEdge(firstNode.getPosX(),
                    firstNode.getPosY(), firstNode.getFloor(), nodeEdgeX2, nodeEdgeY2, currentFloor);
            resetScreen();
            addSingleEdgeMode = false;
            graph.createEdgeLines(firstNode.getEdgeList(), true, true);
            return;
        }

        //display edges already associated with selected node
        if (edgesSelected == 1 || mode_ChoiceBox.getValue().equals("Edit Node")
                || mode_ChoiceBox.getValue().equals("Remove Node")) {
            System.out.println("Edge stage 1");
            //display edges already associated with selected node
            nodeEdgeX1 = (int) x;
            nodeEdgeY1 = (int) y;
            System.out.println(nodeEdgeX1 + "     " + nodeEdgeY1);
            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                    .getNode(nodeEdgeX1, nodeEdgeY1, currentFloor);
            graph.createEdgeLines(firstNode.getEdgeList(), true, true);

            //color the node as well
            if (lastColoredStart !=  null) {
                lastColoredStart.setStroke(lastColoredStart.getFill());
                lastColoredStart.setStrokeWidth(1);
            }
            lastColoredStart = c;
            c.setStrokeWidth(2.5);
            c.setStroke(Color.ROYALBLUE);

            //log the floor
            floor1 = currentFloor;
        } else if (edgesSelected == 2) {
            System.out.println("Edge stage 2");
            //create edge between the two nodes
            nodeEdgeX2 = (int) x;
            nodeEdgeY2 = (int) y;

            //color the node
            lastColoredEnd = c;
            c.setStrokeWidth(2.5);
            c.setStroke(Color.FUCHSIA);

            //log the floor
            floor2 = currentFloor;
        }
        String type = "", name = "", room = "";
        boolean hidden = false, enabled = false;
        ResultSet rset = databaseController.getNode(x, y, currentFloor);
        try {
            while (rset.next()){
                type = rset.getString("TYPE");
                name = rset.getString("NAME");
                room = rset.getString("ROOMNUM");
                hidden = rset.getBoolean("ISHIDDEN");
                enabled = rset.getBoolean("ENABLED");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        switch(type){
        case "Doctor's Office":
            title_ChoiceBox.getSelectionModel().select(0);
        case "Food Service":
            title_ChoiceBox.getSelectionModel().select(1);
        case "Restroom":
            title_ChoiceBox.getSelectionModel().select(2);
        case "Information":
            title_ChoiceBox.getSelectionModel().select(3);
        case "Laboratory":
            title_ChoiceBox.getSelectionModel().select(4);
        case "Waiting Room":
            title_ChoiceBox.getSelectionModel().select(5);
        }

        name_TextField.setText(name);
        room_TextField.setText(room);
        hidden_CheckBox.setSelected(hidden);
        enabled_CheckBox.setSelected(enabled);


    }

    //submit button is clicked
    //To Do - use this to send the information of your changes to the DB to get updated
    public void submitButton_Clicked() {
        final String tempName = name_TextField.getText();
        final String tempRoom = room_TextField.getText();
        //String type = title_ChoiceBox.getValue();

        if (mode_ChoiceBox.getValue() == null) {
            System.out.println("incoming null ptr exception");
        }
        switch (mode_ChoiceBox.getValue()) {
            case "---":
                System.out.println("Mode = default");

                break;
            case "Add Node":

                if (!("".equals(title_ChoiceBox.getValue())) &&
                        !("".equals(name_TextField.getText())) &&
                        !("".equals(room_TextField.getText()))) {

                    System.out.println("Mode = add");
                    //get type
                    String type;
                    switch (title_ChoiceBox.getValue()) {
                        case "---":
                            type = "Doctor's Office"; //doctor by default
                            break;
                        default:
                            type = title_ChoiceBox.getValue();
                            break;
                    }


                    Node newNode = new Node((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                            currentFloor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom);
                    DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                            currentFloor, hidden_CheckBox.isSelected(), enabled_CheckBox.isSelected(), type, tempName, tempRoom);
                }
                mode_ChoiceBox.getSelectionModel().select("---");

                break;

            case "Edit Node":
                System.out.println("Mode = edit node");
                //get latest touched node
                boolean newHidden = hidden_CheckBox.isSelected();
                boolean newEnabled = enabled_CheckBox.isSelected();
                String newType;
                String newName = name_TextField.getText();
                String newRoomnum = room_TextField.getText();

                switch (title_ChoiceBox.getValue()) {
                    case "---":
                        newType = "Doctor's Office"; //doctor by default
                        break;
                    default:
                        newType = title_ChoiceBox.getValue();
                        break;
                }



                //update to new version in db
                DBController.DatabaseController.getInstance().updateNode(firstNode.getPosX(), firstNode.getPosY(),
                        firstNode.getFloor(), newHidden, newEnabled, newType, newName, newRoomnum);

                break;
            case "Remove Node":
                System.out.println("Mode = remove node");
                for (controllers.Edge thisEdge : firstNode.getEdgeList()) {
                    thisEdge.getNeighbor(firstNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                DBController.DatabaseController.getInstance().deleteNode(firstNode.getPosX(), firstNode.getPosY(), currentFloor);
                break;
            case "Add Edge":
                if (edgesSelected == 2) {
                    System.out.println("Mode = add edge");
                    DBController.DatabaseController.getInstance().newEdge(nodeEdgeX1,
                            nodeEdgeY1, floor1, nodeEdgeX2, nodeEdgeY2, floor2);

                    System.out.println("added edge");
                }
                break;
            case "Remove Edge":
                if (edgesSelected == 2) {
                    System.out.println("Mode = delete edge");
                    DBController.DatabaseController.getInstance().deleteEdge(nodeEdgeX1,
                            nodeEdgeY1, floor1, nodeEdgeX2, nodeEdgeY2, floor2);
                    System.out.println("removed edge");
                }
                System.out.println("Mode = remove edge");
                break;
            default:
                System.out.println("Nothing selected for mode");
                break;
        }
        controllers.MapController.getInstance().requestMapCopy();

        //show edge lines to tell user change has been made
        //check so edge lines do not show up on wrong floor
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), true, currentFloor);
        edgesSelected = 0;


        //try to display last touched edge list
        //requery firstnode to reset edge list
        //check so edge lines do not show up on wrong floor
        if(firstNode != null && floor1 == floor2 && (mode_ChoiceBox.getValue().equals("Add Edge") || mode_ChoiceBox.getValue().equals("Remove Edge"))) {
            firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                    .getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
            //don't know if above method is successful
            //must check again if firstNode is not null
            if (firstNode != null) {
                graph.createEdgeLines(firstNode.getEdgeList(), true, false);
            }
        }

    }

    //Change to main Menu
    public void mainMenuButton_Clicked() {

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(username_Label.getText());
        System.out.println(username_Label.getText());

        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        controller.setLanguageChoices();
    }

    public void setUserString(String user) {
        username_Label.setText(user);
    }

    public void setModeChoices() {
        mode_ChoiceBox.getItems().addAll("---", "Add Node", "Remove Node", "Edit Node", "Add Edge", "Remove Edge");
        mode_ChoiceBox.getSelectionModel().selectFirst();
        mode_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // Do validation
                clearButton_Clicked();
                System.out.println(newValue);
                if (newValue.intValue() == 1) {
                    create_Button();
                }

            }
        });
    }

    //Sets the map of the desired floor
    /*public void setFloorChoices(){
        floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7");
        floor_ChoiceBox.getSelectionModel().select(0);
        map_viewer.setImage(new Image("/images/cleaned1.png"));
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                System.out.println(newValue);
                currentFloor = newValue.intValue() + 1;

                mapImage newMapImage = new proxyMap(currentFloor);
                newMapImage.display(map_viewer);

                graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true, currentFloor);

                //draw edges
                graph.drawFloorEdges(currentFloor);
            }
        });
    }*/

    //Sets the map of the desired floor
    public void setFloorChoices(){
        floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Outside",
                "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Belkin Basement");

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

                mapImage newMapImage = new proxyMap(currentFloor);
                newMapImage.display(map_viewer);

                /////////////////////////////////


                /////////////////////////////////
                if(!outside) {
                    //c_Floor_Label.setText(Integer.toString(currentFloor));
                }else{
                    //c_Floor_Label.setText("");
                    //floor_Label.setText(currentF);
                }
                //true ot see nodes false otherwise
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true, currentFloor);
            }
        });

    }

    public void setTitleChoices() {

        title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom", "Elevator", "Stair",
                "Information", "Laboratory", "Waiting Room");
    }

    public void create_Button() {
        btK = new Circle(labelRadius);//new Button();
        // this code drags the button
        final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());

        //This code is for placing nodes
        btK.setOnMouseDragged(e -> {
            if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                    && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                btK.setLayoutX((e.getSceneX() - paneBounds.getMinX()));
                btK.setLayoutY((e.getSceneY() - paneBounds.getMinY()));
            }
        });

        admin_FloorPane.getChildren().add(btK);
    }

    //sets the current language given information form other screens
    public void setC_language(int i){
        c_language = i;
    }

    //switches all the labels and Buttons to english
    public void englishButtons_Labels(){
        //change the current language to english
        c_language = 0;

        //Change the Buttons
        mainMenu_Button.setText("Main Menu");
        emergency_Button.setText("EMERGENCY");
        submit_Button.setText("Submit");
        clear_Button.setText("Clear");

        //Change the labels
        title_Label.setText("Map Management");
        subTitile_Label.setText("Information:");
        nodeTitile_Label.setText("Title:");
        name_Label.setText("Name:");
        room_Label.setText("Room:");
        hidden_Label.setText("Hidden:");
        enabled_Label.setText("Enabled:");
        floorMap_Label.setText("Floor Map");
        chooseFloor_Label.setText("Floor:");
        mode_Label.setText("Mode:");



        //text fields
        name_TextField.setPromptText("Name");
        room_TextField.setPromptText("Room");


    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //Change the Buttons
        mainMenu_Button.setText("Menú Principal");
        emergency_Button.setText("EMERGENCIA");
        submit_Button.setText("Listo");
        clear_Button.setText("Borrar");

        //Change the labels
        title_Label.setText("Control de Mapa");
        subTitile_Label.setText("Información:");
        nodeTitile_Label.setText("Título:");
        name_Label.setText("Nombre:");
        room_Label.setText("Habitacion:");
        hidden_Label.setText("Oculto:");
        enabled_Label.setText("Habilitar:");
        floorMap_Label.setText("Mapa del Piso");
        chooseFloor_Label.setText("Piso:");
        mode_Label.setText("Modo:");



        //text fields
        name_TextField.setPromptText("Name");
        room_TextField.setPromptText("Room");


    }





}