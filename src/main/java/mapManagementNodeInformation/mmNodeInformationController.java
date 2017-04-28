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
import org.controlsfx.control.textfield.TextFields;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

//import org.controlsfx.control.textfield.TextFields;


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

   /* @FXML
    private Label error_LabelText;*/

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

    /*@FXML
    private TextField department_TextField; */

    @FXML
    private TextField search_textField;

    @FXML
    private TextField title_TextField;

    @FXML
    private Label title_Label;

    @FXML
    private Label subTitle_Label;

    @FXML
    private Label Mode_Label;

    @FXML
    private Label docTitle_Label;

    /*@FXML
    private Label department_Label;*/

    @FXML
    private Label room_Label;

    @FXML
    private Label firstName_Label;

    @FXML
    private Label lastName_Label;

    @FXML
    private Button mapManagement_Button;


    int ID;

    boolean inDataBase = false;
    String First_N;
    String Last_N;
    String Title;
    String Department;
    String Room;

    String spTitle = "", spDepartment = "";
    String enTitle = "", enDepartment = "";
    String title, department;



    /**
     * Flags for passing different info
     */
    // Flag for current mode chosen (add = 0, edit = 2, remove = 1)
    int c_mode = -1;

    //set to english by default (english = 0, spanish = 1)
    int c_language = 0;

    private int permissionLevel;


    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    //handle the clear button
    public void cancelButton_Clicked() {
        System.out.println("The user has clicked the cancel Button");

        //department_TextField.setText("");
        room_TextField.setText("");
        id_TextField.setText("");
        Firstname_TextField.setText("");
        lastName_TextField.setText("");
        title_TextField.setText("");
        mode_ChoiceBox.getSelectionModel().select(0);

    }

    //Handle the submiut button clicked
    public void submitButton_Clicked(){

        String cTitle = title_TextField.getText();
        //String cDepartment = department_TextField.getText();

        // Get user input for fields that don't care about language
        String room = room_TextField.getText();
        String firstName = Firstname_TextField.getText();
        String lastName = lastName_TextField.getText();
        String title = title_TextField.getText();

//        if(c_language == 0){
//            // language is english
//            ArrayList<String> english_departments = databaseController.getEnglishDepartmentList();
            /*if (!(english_departments.contains(cDepartment))) {
                // if current english department not in the list of available english departments
                // pop up window should get the new department in spanish (since we don't know the
                // translation)
                //Get spanish translation of unknown department
                TextInputDialog dialogDepartment = new TextInputDialog("");
                dialogDepartment.setTitle("Add New Department");
                dialogDepartment.setHeaderText("You have entered an unknown department! Please provide Spanish translation.");
                dialogDepartment.setContentText("Department in Spanish:");
                // The Java 8 way to get the response value (with lambda expression).
                // Traditional way to get the response value.
                Optional<String> result = dialogDepartment.showAndWait();
                result.ifPresent(name -> spDepartment = name);
                department = cDepartment; // department to be added in the database later is current department
                databaseController.addInTranslation(cDepartment, spDepartment);
            }*/
            //department = databaseController.getSpanish(cDepartment);
//            ArrayList<String> english_titles = databaseController.getEnglishTitleList();
//            if (!(english_titles.contains(cTitle))){
//                // if current english title not in the list of available english titles
//                // pop up window should get the new title in spanish (since we don't know the
//                // translation)
//                //Get spanish translation of new title
//                TextInputDialog dialogTitle = new TextInputDialog("");
//                dialogTitle.setTitle("Add new Title");
//                dialogTitle.setHeaderText("You have entered an unknown title! Please provide Spanish translation.");
//                dialogTitle.setContentText("Title in Spanish:");
//                Optional<String> result = dialogTitle.showAndWait();
//                result.ifPresent(name -> spTitle = name);
//                title = cTitle;
//                databaseController.addInTranslation(cTitle, spTitle);
//            }
            //title = databaseController.getSpanish(cTitle);
//        } else if(c_language == 1) {
//            // language is spanish
//            ArrayList<String> spanish_departments = databaseController.getSpanishDepartmentList();
//            /*if (!(spanish_departments.contains(cDepartment))) {
//                TextInputDialog dialog = new TextInputDialog("");
//                dialog.setTitle("Agrega un Nuevo Departamento");
//                dialog.setHeaderText("Has ingresado un departamento desconocido! Por favor agrega la versión en Inglés.");
//                dialog.setContentText("Departamento en Inglés:");
//                // The Java 8 way to get the response value (with lambda expression).
//                // Traditional way to get the response value.
//                Optional<String> result = dialog.showAndWait();
//                result.ifPresent(name -> enDepartment = name);
//                department = enDepartment;
//                databaseController.addInTranslation(enDepartment, cDepartment);
//            }*/
//            //department = databaseController.getEnglish(cDepartment);
//            ArrayList<String> spanish_titles = databaseController.getSpanishTitleList();
//            if (!(spanish_titles.contains(cTitle))){
//                // if current spanish title not in the list of available spanish titles
//                // pop up window should get the new title in english (since we don't know the
//                // translation)
//                //Get englsih translation of new title
//                TextInputDialog dialogTitle = new TextInputDialog("");
//                dialogTitle.setTitle("Agrega un Nuevo Título");
//                dialogTitle.setHeaderText("Has ingresado un título desconocido! Por favor agrega la versión en Inglés.");
//                dialogTitle.setContentText("Título en Inglés:");
//                Optional<String> result = dialogTitle.showAndWait();
//                result.ifPresent(name -> enTitle = name);
//                title = enTitle;
//                databaseController.addInTranslation(enTitle, cTitle);
//            }
//           title = databaseController.getEnglish(cTitle);
//        }

        ResultSet rset;
        int id = 0, xpos = 0, ypos = 0, floor = 0;

        System.out.println("room: " + room);
        System.out.println("First Name: " + firstName);
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
                databaseController.newProfessional(firstName, lastName, title);
                rset = databaseController.getProfessional(firstName, lastName, title);
                try {
                    rset.next();
                    id = rset.getInt("ID");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (!("".equals(room_TextField.getText()))) {
                    databaseController.newProfessionalLocation(id, xpos, ypos, floor);
                    System.out.println("Adding without location");
                }
                System.out.println("Adding professional mode ------------");
                cleaningTextFields(c_mode);
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
                cleaningTextFields(c_mode);
                break;
            case 2: // editing
                System.out.println("Editing professional mode");
                rset = databaseController.getProfessional(Integer.parseInt(id_TextField.getText()));
                try {
                    rset.next();
                    id = rset.getInt("ID");
                    System.out.println("ID " + id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                databaseController.EditProfessional(id, firstName, lastName, title);
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

    //switches to main menu
    public void mainMenuButton_Clicked() {
        System.out.println("The user has clicked the sign out Button");

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(currentAdmin_Label.getText());
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

    //sets up the tree
    public void setUpTreeView(){
        ID_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
        firstName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rFirstName"));
        lastName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rLastName"));
        title_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rTitle"));
        department_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rType"));
        room_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rRoom"));


        ResultSet rset, rset2;
        rset = databaseController.getProRoomNums();
        rset2 = databaseController.getProsWithoutRooms();
        ArrayList<Integer> ids = new ArrayList<>();

        ObservableList<Table> data = FXCollections.observableArrayList();

        int id;
        String firstName, lastName, title, department, roomNum;
        try {
            while (rset.next()){
                id = rset.getInt("ID");
                ids.add(id);
                firstName = rset.getString("FIRSTNAME");
                lastName = rset.getString("LASTNAME");
                title = rset.getString("TYPE");
                roomNum = rset.getString("ROOMNUM");
                System.out.println("Name: " + firstName + lastName);
                //Table table = new Table(id, firstName, lastName, title, department, roomNum);
                data.add(new Table(id, firstName, lastName, title, "", roomNum));
            }
            rset.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
        try {
            while (rset2.next()){
                id = rset2.getInt("ID");
                firstName = rset2.getString("FIRSTNAME");
                lastName = rset2.getString("LASTNAME");
                title = rset2.getString("TYPE");
                System.out.println("Name: " + firstName + lastName);
                if (!ids.contains(id)) {
                    //Something bad happened
                    roomNum= "empty";
                    Table table = new Table(id, firstName, lastName, title, "", roomNum);
                    data.add(table);
                }
            }
            rset2.close();
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
                    //department_TextField.setText(Department);
                    room_TextField.setText(Room);
                    //
                    title_TextField.setText(Title);

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
        if(c_language == 0) {
            mode_ChoiceBox.getItems().addAll("Add", "Remove", "Edit");
        }else{
            mode_ChoiceBox.getItems().addAll("Agregar", "Borrar", "Editar");
        }
        mode_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        // Do validation
                        System.out.println(newValue);

                        //Sets the mode to Add
                        if (newValue.intValue() == 0) {
                            add_settings();
                            id_TextField.setText("");

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

    //The add settings for the user to add a Doctor/nurse
    public void add_settings() {
        c_mode = 0;
        System.out.println("Add settings");
        //error_LabelText.setText("");

        //Starts the choices for the user
        title_TextField.setText("");
        //department_TextField.setText("");
        id_TextField.setText("");
        Firstname_TextField.setText("");
        lastName_TextField.setText("");
        id_TextField.setPromptText("Automatically Generated ID");
        Firstname_TextField.setPromptText("First");
        lastName_TextField.setPromptText("Last");
        //Sets the properties
        title_TextField.setDisable(false);
        //department_TextField.setDisable(false);
        room_TextField.setDisable(false);
        id_TextField.setEditable(false);
        Firstname_TextField.setEditable(true);
        lastName_TextField.setEditable(true);
    }

    //The remove settings for the user to remove a Doctor/nurse
    public void remove_settings() {
        c_mode = 1;
        System.out.println("remove settings");
        //error_LabelText.setText("");
        //sets the properties
        title_TextField.setDisable(true);
        //department_TextField.setDisable(true);
        room_TextField.setDisable(true);
        id_TextField.setEditable(false);
        Firstname_TextField.setEditable(false);
        lastName_TextField.setEditable(false);

    }

    //The remove settings for the user to remove a Doctor/nurse
    public void edit_settings() {
        c_mode = 2;
        System.out.println("edit settings");
        //error_LabelText.setText("");

        //sets the properties
        title_TextField.setDisable(false);
        //department_TextField.setDisable(false);
        room_TextField.setDisable(false);
        id_TextField.setEditable(false);
        Firstname_TextField.setEditable(true);
        lastName_TextField.setEditable(true);

    }

    //Sets the label of an error
    //
    /*public void setError(String error) {
        error_LabelText.setText(error);
    }*/

    //Sets the user in the scene
    public void setUser(String user) {

        currentAdmin_Label.setText(user);
    }


    //Sets the current mode whene refreshing the scene
    public void setCurrentMode(int i) {

        mode_ChoiceBox.getSelectionModel().select(i);
    }

    //sets the choices for the rooms and departments
    public void setRoomChoices() {

        ArrayList<String> rooms = new ArrayList<>();
        ArrayList<String> departments = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        rooms = databaseController.getFilteredRooms();
        titles = databaseController.getTitles();

        // rooms not affected by language
        TextFields.bindAutoCompletion(room_TextField,rooms);

        TextFields.bindAutoCompletion(title_TextField, titles);
    }

    //Cleans the text fields
    public void cleaningTextFields(int mode){
        //department_TextField.setText("");
        room_TextField.setText("");
        Firstname_TextField.setText("");
        lastName_TextField.setText("");

        if (mode == 1 || mode == 0){
            title_TextField.setText("");
            id_TextField.setText("");
        }
    }

    //sets the current language to the given language
    public void setC_language(int i){
        c_language = i;
    }

    //Changes the buttons and labels to english
    public void englishButtons_Labels(){
        setC_language(0);
        //Buttons
        emergency_Button.setText("EMERGENCY");
        submit_Button.setText("Submit");
        cancel_Button.setText("Clear");
        mapManagement_Button.setText("Map Management");

        //Labels
        title_Label.setText("Directory Management");
        subTitle_Label.setText("Manage Directory");
        Mode_Label.setText("Mode:");
        docTitle_Label.setText("Title:");
        //department_Label.setText("Department:");
        room_Label.setText("Room:");
        firstName_Label.setText("First Name");
        lastName_Label.setText("Last Name");

        //text fields
        search_textField.setPromptText("search");
        room_TextField.setPromptText("room");
        Firstname_TextField.setPromptText("First Name");
        lastName_TextField.setPromptText("Last Name");
        id_TextField.setPromptText("ID");

        //Table columns
        firstName_TableColumn.setText("First Name");
        lastName_TableColumn.setText("Last Name");
        title_TableColumn.setText("Title");
        department_TableColumn.setText("Department");
        room_TableColumn.setText("Room");

    }

    //Changes the buttons and labels to spanish
    public void spanishButtons_Labels(){
        setC_language(1);
        //Buttons
        emergency_Button.setText("EMERGENCIA");
        submit_Button.setText("Listo");
        cancel_Button.setText("Borrar");
        mapManagement_Button.setText("Control de Mapas");

        //Labels
        title_Label.setText("Directorio");
        subTitle_Label.setText("Control de Directorio");
        Mode_Label.setText("Modo:");
        docTitle_Label.setText("Título");
        //department_Label.setText("Departamento:");
        room_Label.setText("Habitación:");
        firstName_Label.setText("Nombre");
        lastName_Label.setText("Apellido");

        //text fields
        search_textField.setPromptText("busca");
        room_TextField.setPromptText("Habitación");
        Firstname_TextField.setPromptText("Nombre");
        lastName_TextField.setPromptText("Apellido");
        //department_TextField.setPromptText("Departamento");
        id_TextField.setPromptText("ID");

        //Table columns
        firstName_TableColumn.setText("Nombre");
        lastName_TableColumn.setText("Apellido");
        title_TableColumn.setText("Título");
        department_TableColumn.setText("Departamento");
        room_TableColumn.setText("Habitación");
    }

    //Adds the information to the database and adds the professional
    public void saveToDatabase(String d, String t ){
        //DB stuff
        System.out.println("DO DATABASE STUFF PLEASE");
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

    //Changes scene bakc to map management
    public void mapManagementButton_Clicked(){
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
        controller.setUserString("Admin: " + currentAdmin_Label.getText());
        controller.setPermissionLevel(2);

    }

}

