package mapManagementNodeInformation;

import DBController.DatabaseController;
import hospitalDirectorySearch.Table;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;

import org.controlsfx.control.textfield.TextFields;


/**
 * Created by AugustoR on 3/31/17.
 */
public class mmNodeInformationController extends controllers.AbsController {
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label currentAdmin_Label;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private ChoiceBox<String> title_choiceBox;

    @FXML
    private TextField lastName_TextField;

    @FXML
    private TextField Firstname_TextField;

    @FXML
    private TextField id_TextField;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private ChoiceBox mode_ChoiceBox;

    @FXML
    private Label error_LabelText;

    @FXML
    private TextField room_TextField;


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
    private TextField department_TextField;

    @FXML
    private TextField search_textField;

    int c_title;

    int ID;
    String First_N;
    String Last_N;
    String Title;
    String Department;
    String Room;


    /**
     * Flags for passing different info
     */
    // Flag for current mode chosen (add = 0, edit = 2, remove = 1)
    int c_mode = -1;



    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    public void cancelButton_Clicked() {
        System.out.println("The user has clicked the cancel Button");

    }

    public void submitButton_Clicked(){
        System.out.println("Hello world");
        ResultSet rset;
        int id = 0, xpos = 0, ypos = 0, floor = 0;
        String firstName, lastName, title, department, room;
        title = title_choiceBox.getValue();
        System.out.println("Title: " + title);
        department = department_TextField.getText();
        System.out.println("Department: " + department);
        room = room_TextField.getText();
        System.out.println("room: " + room);
        //id = Integer.parseInt(id_TextField.getText());
        firstName = Firstname_TextField.getText();
        System.out.println("First Name: " + firstName);
        lastName = lastName_TextField.getText();
        System.out.println("Last Name: " + lastName);
        rset = databaseController.getPosForRoom(room);
        try{
            while (rset.next()){
                xpos = rset.getInt("XPOS");
                ypos = rset.getInt("YPOS");
                floor = rset.getInt("FLOOR");
                System.out.println("Pos: " + xpos + " " + ypos + " " + floor);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        switch (c_mode) {
            case 0: // adding
                System.out.println("Adding professional mode");
                databaseController.newProfessional(firstName, lastName, title, department);
                rset = databaseController.getProfessional(firstName, lastName, title);
                try {
                    rset.next();
                    id = rset.getInt("ID");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                databaseController.newProfessionalLocation(id, xpos, ypos, floor);
                System.out.println("Adding professional mode ------------");
                break;
            case 1: // removing
                System.out.println("Removing professional mode");
                databaseController.deleteProfessionalLocation(id, xpos, ypos, floor);
                rset = databaseController.getProfessional(firstName, lastName, title);
                try {
                    rset.next();
                    id = rset.getInt("ID");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                databaseController.deleteProfessionalLocation(id, xpos, ypos, floor);
                databaseController.deleteProfessional(id);
                System.out.println("Removing professional mode ------------");
                break;
            case 2: // editing
                System.out.println("Editing professional mode");
                rset = databaseController.getProfessional(firstName, lastName, title);
                try {
                    rset.next();
                    id = rset.getInt("ID");
                    System.out.println("ID " + id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                databaseController.EditProfessional(id, firstName, lastName, title, department);
                databaseController.EditProfessionalLocation(id, xpos, ypos, floor);
                System.out.println("Editing professional mode ------------");
                break;
            default:
                // nothing
        }

           setUpTreeView();
    }

    //switches to the emergency scene
    public void emergencyButton_Clicked() {
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    //switches to main menu
    public void mainMenuButton_Clicked() {
        System.out.println("The user has clicked the sign out Button");

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(currentAdmin_Label.getText());

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
                title = rset.getString("TYPE");
                department = rset.getString("DEPARTMENT");
                roomNum = rset.getString("ROOMNUM");
                System.out.println("Name: " + firstName + lastName);
                //Table table = new Table(id, firstName, lastName, title, department, roomNum);
                data.add(new Table(id, firstName, lastName, title, department, roomNum));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        Table_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    ID = Table_TableView.getSelectionModel().getSelectedItem().getrID();
                    First_N = Table_TableView.getSelectionModel().getSelectedItem().getrFirstName();
                    Last_N = Table_TableView.getSelectionModel().getSelectedItem().getrLastName();
                    Title = Table_TableView.getSelectionModel().getSelectedItem().getrTitle();
                    Department = Table_TableView.getSelectionModel().getSelectedItem().getrType();
                    Room = Table_TableView.getSelectionModel().getSelectedItem().getrRoom();

                    if(Title.equals("Doctor")){
                        c_title = 0;
                    }else if(Title.equals("Nurse")){
                        c_title = 1;
                    }
                    title_choiceBox.getSelectionModel().select(c_title);
                    department_TextField.setText(Department);
                    room_TextField.setText(Room);
                    //

                    id_TextField.setText(Integer.toString(ID));
                    Firstname_TextField.setText(First_N);
                    lastName_TextField.setText(Last_N);


                }
            }
        });
        FilteredList<Table> filteredData = new FilteredList<>(data, e-> true);
        search_textField.setOnKeyReleased(e -> {
            search_textField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Table>) Table ->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if(Table.getrFirstName().toLowerCase().contains(lowerCaseFilter)){
                        return true;

                    }else if(Table.getrLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(Table.getrType().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(Table.getrTitle().toLowerCase().contains(lowerCaseFilter)){
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
    //Sets the choices for the mode Add, edit remove
    public void setModeChoices() {
        mode_ChoiceBox.getItems().addAll("Add", "Remove", "Edit");
        mode_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        // Do validation
                        System.out.println(newValue);

                        //Sets the mode to Add
                        if (newValue.intValue() == 0) {
                            add_settings();

                            //Sets the mode to remove
                        } else if (newValue.intValue() == 1) {
                            remove_settings();

                            //Sets the mode to edit
                        } else if (newValue.intValue() == 2) {
                            edit_settings();

                        }
                    }
                });

    }
    //sets the choices for the department DB
    public void setDepartmentChoices() {
        //department_ChoiceBox.getItems().addAll("Accident and emergency (A&E)", "Anaesthetics", "Breast screening");
    }

    //set the title choices for the user
    public void setTitleChoices() {
        title_choiceBox.getItems().addAll("Doctor", "Nurse");
        title_choiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        // Do validation
                        System.out.println(newValue);
                        if (newValue.intValue() == 0) {
                            System.out.println("Hello world");
                            //create_Button();
                        } else if (newValue.intValue() == 1 || newValue.intValue() == 2) {
                            //admin_FloorPane.getChildren().remove(btK);
                        }
                    }
                });
    }

    //The add settings for the user to add a Doctor/nurse
    public void add_settings() {
        c_mode = 0;
        System.out.println("Add settings");
        error_LabelText.setText("");

        //Starts the choices for the user
        title_choiceBox.getSelectionModel().select(0);
        department_TextField.setText("");
        id_TextField.setText("");
        Firstname_TextField.setText("");
        lastName_TextField.setText("");
        id_TextField.setPromptText("Automatically Generated ID");
        Firstname_TextField.setPromptText("First");
        lastName_TextField.setPromptText("Last");
        //Sets the properties
        title_choiceBox.setDisable(false);
        department_TextField.setDisable(false);
        room_TextField.setDisable(false);
        id_TextField.setEditable(false);
        Firstname_TextField.setEditable(true);
        lastName_TextField.setEditable(true);
    }

    //The remove settings for the user to remove a Doctor/nurse
    public void remove_settings() {
        c_mode = 1;
        System.out.println("remove settings");
        error_LabelText.setText("");
        //sets the properties
        title_choiceBox.setDisable(true);
        department_TextField.setDisable(true);
        room_TextField.setDisable(true);
        id_TextField.setEditable(false);
        Firstname_TextField.setEditable(false);
        lastName_TextField.setEditable(false);

    }

    //The remove settings for the user to remove a Doctor/nurse
    public void edit_settings() {
        c_mode = 2;
        System.out.println("edit settings");
        error_LabelText.setText("");

        //sets the properties
        title_choiceBox.setDisable(false);
        department_TextField.setDisable(false);
        room_TextField.setDisable(false);
        id_TextField.setEditable(false);
        Firstname_TextField.setEditable(true);
        lastName_TextField.setEditable(true);

    }

    //Sets the label of an error
    public void setError(String error) {
        error_LabelText.setText(error);
    }

    //Sets the user in the scene
    public void setUser(String user) {
        currentAdmin_Label.setText(user);
    }


    //Sets the current mode whene refreshing the scene
    public void setCurrentMode(int i) {
        mode_ChoiceBox.getSelectionModel().select(i);
    }

    //sets the choices for the rooms DB
    public void setRoomChoices() {


        ArrayList<String> rooms = new ArrayList<>();
        ArrayList<String> departments = new ArrayList<>();

        String room, department;

        ResultSet rset_departments = databaseController.getDepartmentNames();
        ResultSet rset = databaseController.getRoomNames();
        try {
            while (rset.next()){
                room = rset.getString("ROOMNUM");
                if (!rooms.contains(room)){
                    rooms.add(room);
                }

            }
            while (rset_departments.next()){
                department = rset_departments.getString("DEPARTMENT");
                if (!departments.contains(department)){
                    departments.add(department);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        TextFields.bindAutoCompletion(room_TextField,rooms);

        TextFields.bindAutoCompletion(department_TextField, departments);
    }

}




