package adminSignUp;
import DBController.DatabaseController;
import adminSignUp.adminTable;
import hospitalDirectorySearch.Table;
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
import javafx.scene.layout.HBox;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Created by AugustoR on 4/17/17.
 */
public class adminSignUpController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label currentAdmin_Label;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private Button MainMenu_Button;

    @FXML
    private TableView<adminTable> Table_TableView;

    @FXML
    private TableColumn<adminTable, Integer> ID_TableColumn;

    @FXML
    private TableColumn<adminTable, String> username_TableColumn;

    @FXML
    private TableColumn<adminTable, String> firstName_TableColumn;

    @FXML
    private TableColumn<adminTable, String> lastName_TableColumn;

    @FXML
    private TableColumn<adminTable, String> password_TableColumn;

    @FXML
    private Label queryStatus;

    @FXML
    private Label subTitle_Label;

    @FXML
    private TextField search_textField;

    @FXML
    private Label error_LabelText;

    @FXML
    private Label Mode_Label;

    @FXML
    private ChoiceBox<String> mode_ChoiceBox;

    @FXML
    private HBox ID_Label;

    @FXML
    private Label username_Label;

    @FXML
    private TextField userName_TextField;

    @FXML
    private HBox firstName_Label;

    @FXML
    private TextField firstName_TextField;

    @FXML
    private Label lastName_Label;

    @FXML
    private TextField lastName_TextField;

    @FXML
    private Label password_Label;

    @FXML
    private PasswordField newPassword_TextField;

    @FXML
    private TextField id_textField;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    int c_language = 0; //English by default

    DatabaseController databaseController = DatabaseController.getInstance();

    int givID;
    String givUsername, givFirstN, givLastN, givPassword;

    //Deal with the clear button clicked
    public void cancelButton_Clicked(){

    }

    //Deal with the submit button clicked
    public void submitButton_Clicked(){

    }



    //Sends the admin back to the main menu
    public void mainMenuButton_Clicked(){
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

    //Set the username coming from a scene
    public void setUsername(String user){
        currentAdmin_Label.setText(user);
    }

    //adds the admin into the database
    public void addAdminButton_Clicked(){
        try {
            if (databaseController.newAdmin(firstName_TextField.getText(), lastName_TextField.getText(),
                    userName_TextField.getText(), newPassword_TextField.getText())) {
                queryStatus.setText("Admin Added");
            } else {
                queryStatus.setText("Error Adding Admin");
            }
        }
        catch(Exception e){
            queryStatus.setText("ERROR: Exception");
            e.printStackTrace();
        }
    }

    //edits admin
    //FIXME: GET THE ID FROM THE CHART SELECTION


    public void editAdminButton_Clicked(){
        try {
            if (databaseController.editAdmin(999, firstName_TextField.getText(), lastName_TextField.getText(),
                    userName_TextField.getText(), newPassword_TextField.getText())) {
                queryStatus.setText("Admin Edited");
            } else {
                queryStatus.setText("Error Edit Admin");
            }
        }
        catch(Exception e){
            queryStatus.setText("ERROR: Exception");
            e.printStackTrace();
        }
    }

    //deletes admin from database
    //FIXME: GET THE ID FROM THE CHART SELECTION
    public void deleteAdminButton_Clicked(){
        try {
            if (databaseController.deleteAdmin(999)){
                queryStatus.setText("Admin Deleted");
            } else {
                queryStatus.setText("Error Deleting Admin");
            }
        }
        catch(Exception e){
            queryStatus.setText("ERROR: Exception");
            e.printStackTrace();

         }
    }

    //sets up the tree
    public void setUpTreeView(){
        ID_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, Integer>("rID"));
        username_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rUsername"));
        firstName_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rFirstName"));
        lastName_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rLastName"));
        //password_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rPassword"));

        int ID;
        String username, firstName, lastName, password;

        ObservableList<adminTable> data = FXCollections.observableArrayList();

        data.add(new adminTable(1, "GriffTab", "Griffin", "Tabor","1234"));
        data.add(new adminTable(2, "GriffCec", "Griffin", "Cecil","12345"));
        data.add(new adminTable(3, "WongWong", "Wilson", "Wong","123456"));

        //Getting the infomration from the table View to the user
        Table_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    givID = Table_TableView.getSelectionModel().getSelectedItem().getrID();
                    givUsername = Table_TableView.getSelectionModel().getSelectedItem().getrUsername();
                    givFirstN = Table_TableView.getSelectionModel().getSelectedItem().getrFirstName();
                    givLastN = Table_TableView.getSelectionModel().getSelectedItem().getrLastName();
                    //givPassword = Table_TableView.getSelectionModel().getSelectedItem().getrPassword();

                    id_textField.setText(Integer.toString(givID));
                    userName_TextField.setText(givUsername);
                    firstName_TextField.setText(givFirstN);
                    lastName_TextField.setText(givLastN);
                    //newPassword_TextField.setText(givPassword);
                }
            }
        });


        FilteredList<adminTable> filteredData = new FilteredList<>(data, e-> true);
        search_textField.setOnKeyReleased(e -> {
            search_textField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super adminTable>) adminTable ->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    System.out.println("");
                    if(adminTable.getrFirstName().toLowerCase().contains(lowerCaseFilter)){
                        return true;

                    }else if(adminTable.getrLastName().toLowerCase().contains(lowerCaseFilter)){
                        System.out.println("X");
                        return true;
                    }else if(adminTable.getrUsername().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                    return false;
                });
            });
        });
        SortedList<adminTable> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(Table_TableView.comparatorProperty());
        Table_TableView.setItems(sortedData);


    }

    /*public void setUpTreeView(){
        ID_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
        firstName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rFirstName"));
        lastName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rLastName"));
        //title_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rTitle"));
        //department_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rType"));
        //room_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rRoom"));


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
                if (c_language == 0) {
                    System.out.println("Getting ENGLISH type and department c_language NOW = " + c_language);
                    title = rset.getString("TYPE");
                    department = rset.getString("DEPARTMENT");
                } else {
                    System.out.println("Getting SPANISH type and department c_language NOW = " + c_language);
                    title = rset.getString("SPTYPE");
                    System.out.println("getting title " + title);
                    department = rset.getString("SPDEPARTMENT");
                    System.out.println("getting department " + department);
                }
                roomNum = rset.getString("ROOMNUM");
                System.out.println("Name: " + firstName + lastName);
                //Table table = new Table(id, firstName, lastName, title, department, roomNum);
                data.add(new Table(id, firstName, lastName, title, department, roomNum));
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
                if (c_language == 0) {
                    System.out.println("Getting ENGLISH type and department c_language = " + c_language);
                    title = rset2.getString("TYPE");
                    department = rset2.getString("DEPARTMENT");
                } else {
                    System.out.println("Getting SPANISH type and department c_language = " + c_language);
                    title = rset2.getString("SPTYPE");
                    System.out.println("getting title " + title);
                    department = rset2.getString("SPDEPARTMENT");
                    System.out.println("getting department " + department);
                }
                System.out.println("Name: " + firstName + lastName);
                if (!ids.contains(id)) {
                    //Something bad happened
                    roomNum= "empty";
                    Table table = new Table(id, firstName, lastName, title, department, roomNum);
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
                    department_TextField.setText(Department);
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
    */





    //Sets the english labels
    public void englishButtons_Labels(){
        //Buttons
        MainMenu_Button.setText("Main Menu");


        //TextField

    }

    //sets the spanish labels
    public void spanishButtons_Labels(){
        //Buttons
        MainMenu_Button.setText("Menu Principal");


        //TextField


    }


    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

}
