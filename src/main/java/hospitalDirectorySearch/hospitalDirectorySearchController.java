package hospitalDirectorySearch;


import DBController.DatabaseController;
import controllers.MapController;
import controllers.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import pathFindingMenu.Pathfinder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.function.Predicate;


/**
 * Created by AugustoR on 4/6/17.
 */
public class hospitalDirectorySearchController extends controllers.AbsController{

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private TextField search_TextField;

    @FXML
    private TextField from_TextField;

    @FXML
    private TextField to_TextField;

    @FXML
    private Button clear_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private Label currentAdmin_Label;

    @FXML
    private TableView<Table> Table_TableView;

    @FXML
    private TableColumn<Table, Integer> ID_TableColumn;

    @FXML
    private TableColumn<Table, String> firstName_TableColumn;

    @FXML
    private TableColumn<Table, String> lastName_TableColumn;

    @FXML
    private TableColumn<Table, String> title_TableColumn;

    @FXML
    private TableColumn<Table, String> department_TableColumn;

    @FXML
    private TableColumn<Table, String> room_TableColumn;

    @FXML
    private Label error_Label;

    @FXML
    private Label to_Label;

    @FXML
    private Label from_Label;

    @FXML
    private Label title_Label;



    int iNumber = 1;
    //Flag to check if the user has selected a first and second choice
    int flag = 0;


    boolean invalid_input = false;


    //Gets the strings To and From that the users wants to create the path to
    String from_String = "";
    String to_String = "";

    //Set to english by default
    int c_language = 0;



    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    public void getPathNodes(String from_String, String to_String){

    }

    //
    public void mainMenuButton_Clicked(){
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
    }

    //
    public void emergencyButton_Clicked(){

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

    //
    public void clearButton_Clicked(){
        System.out.println("The user has clicked the clear button");
    }

    //
    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit button");

        invalid_input = to_String.equals("") || to_String == null || from_String.equals("") || from_String == null;


        if(!invalid_input) {
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/pathFindingMenuView.fxml");
            pathFindingMenu.pathFindingMenuController controller = loader.getController();
            MapController.getInstance().requestFloorMapCopy();
            MapController.getInstance().requestMapCopy();
            HashMap<Integer, Node> DBMap = MapController.getInstance().getCollectionOfNodes().getMap(4);
            Pathfinder pathfinder = new Pathfinder();
            Node start = MapController.getInstance().getCollectionOfNodes().getNodeWithName(to_String);
            Node end = MapController.getInstance().getCollectionOfNodes().getNodeWithName(from_String);
            pathfinder.generatePath(start,end);
            controller.setUserString("");
            controller.createEdgeLines(pathfinder.getPath());
        }else{
            //There could be information too
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Submitting information");
            alert.setContentText("Cannot submit empty information");
            alert.showAndWait();

        }

    }



    //sets up the tree
    public void setUpTreeView(){
        ID_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
        firstName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rFirstName"));
        lastName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rLastName"));
        title_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rTitle"));
        department_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rType"));
        room_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rRoom"));


        ResultSet rset;
        rset = databaseController.getProRoomNums();

        ObservableList<Table> data = FXCollections.observableArrayList();

        int id;
        String firstName, lastName, title, department, roomNum;
        try {
            while (rset.next()){
                id = rset.getInt("ID");
                firstName = rset.getString("FIRSTNAME");
                lastName = rset.getString("LASTNAME");
                if (c_language == 0) {
                    title = rset.getString("TYPE");
                    department = rset.getString("DEPARTMENT");
                } else {
                    title = rset.getString("SPTYPE");
                    department = rset.getString("SPDEPARTMENT");
                }
                roomNum = rset.getString("ROOMNUM");
                System.out.println("Name: " + firstName + lastName);
                data.add(new Table(id, firstName, lastName, title, department, roomNum));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        Table_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    if(flag == 0) {
                        from_TextField.setText(Table_TableView.getSelectionModel().getSelectedItem().getrFirstName());
                        from_String = Table_TableView.getSelectionModel().getSelectedItem().getrRoom();
                        search_TextField.setText("");
                        flag++;
                    }else if(flag == 1){
                        to_TextField.setText(Table_TableView.getSelectionModel().getSelectedItem().getrFirstName());
                        to_String = Table_TableView.getSelectionModel().getSelectedItem().getrRoom();
                        search_TextField.setText("");
                        flag--;
                    }else{
                        flag = 0;
                    }


                }
            }
        });
        FilteredList<Table> filteredData = new FilteredList<>(data, e-> true);
        search_TextField.setOnKeyReleased(e -> {
            search_TextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Table>) Table ->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(Table.getrFirstName().toLowerCase().contains(lowerCaseFilter)){
                        System.out.println("Hello World");
                        return true;

                    }else if(Table.getrLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(Table.getrType().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(Table.getrTitle().toLowerCase().contains(lowerCaseFilter)){
                        System.out.println("Hello World");
                        return true;
                    }else if(Table.getrRoom().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                    return false;
                });
            });
        });
        SortedList<Table> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(Table_TableView.comparatorProperty());
        Table_TableView.setItems(sortedData);
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
        from_Label.setText("From:");
        to_Label.setText("To:");
        title_Label.setText("Directory");

        //Text Field
        search_TextField.setPromptText("search");

        //Table columns
        firstName_TableColumn.setText("First Name");
        lastName_TableColumn.setText("Last Name");
        title_TableColumn.setText("Title");
        department_TableColumn.setText("Department");
        room_TableColumn.setText("Room");


    }

    //switches all teh labels to spanish
    public void spanishButtons_Labels() {
        //change the current language to spanish
        c_language = 1;

        //change the Buttons
        mainMenu_Button.setText("Menu Principal");
        emergency_Button.setText("EMERGENCIA");
        submit_Button.setText("Listo");
        clear_Button.setText("Borrar");


        //Change the labels
        from_Label.setText("Inicio:");
        to_Label.setText("Destino:");
        title_Label.setText("Directorio");


        //Text Field
        search_TextField.setPromptText("busca");

        //Table columns
        firstName_TableColumn.setText("Nombre");
        lastName_TableColumn.setText("Apellido");
        title_TableColumn.setText("Título");
        department_TableColumn.setText("Departamento");
        room_TableColumn.setText("Habitación");

    }

    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }


}
