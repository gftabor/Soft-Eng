package adminSignUp;
import DBController.DatabaseController;
import adminSignUp.adminTable;
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
    private TableColumn<adminTable, Integer> permissions_TableColumn;

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

   /* @FXML
    private HBox ID_Label;*/

    @FXML
    private Label username_Label;

    @FXML
    private TextField userName_TextField;

   /* @FXML
    private Label firstName_Label;*/

   @FXML
   private Label firstName_Lab;

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

    @FXML
    private CheckBox isAdmin_CheckBox;

    private boolean selfSelected = false;

    int c_language = 0; //English by default
    int c_mode = 0; //Keeps track of the current mode

    DatabaseController databaseController = DatabaseController.getInstance();

    int givID, givPermissions;
    String givUsername, givFirstN, givLastN, givPassword;

   public void clearInputs(){
        id_textField.clear();
        userName_TextField.clear();
        firstName_TextField.clear();
        lastName_TextField.clear();
        newPassword_TextField.clear();
       isAdmin_CheckBox.setSelected(false);
    }

    //Deal with the submit button clicked
    public void submitButton_Clicked(){
        if(mode_ChoiceBox.getValue().equals("Add") || mode_ChoiceBox.getValue().equals("Agregar")){
            addAdmin();
            setUpTreeView();
        }else if (mode_ChoiceBox.getValue().equals("Remove") || mode_ChoiceBox.getValue().equals("Borrar")){
                deleteAdmin();
                setUpTreeView();
        }else if (mode_ChoiceBox.getValue().equals("Edit") || mode_ChoiceBox.getValue().equals("Editar")){
            editAdmin();
            setUpTreeView();
        }else{
            System.out.println("Error with choicebox on admin page");
        }
        clearInputs();

    }
    //adds the admin into the database
    public void addAdmin(){
        try {
            if (databaseController.newAdmin(firstName_TextField.getText(), lastName_TextField.getText(),
                    userName_TextField.getText(), newPassword_TextField.getText(), isAdmin_CheckBox.isSelected())) {
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

    public void editAdmin(){
        try {
            if (databaseController.editAdmin(Integer.parseInt(id_textField.getText()), firstName_TextField.getText(), lastName_TextField.getText(),
                    userName_TextField.getText(), isAdmin_CheckBox.isSelected())) {
                if(newPassword_TextField.getText() != ""){
                    databaseController.editAdminPassword(Integer.parseInt(id_textField.getText()), newPassword_TextField.getText());
                }
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
    public void deleteAdmin(){
        try {
            if (databaseController.deleteAdmin(Integer.parseInt(id_textField.getText()))){
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
                            //id_TextField.setText("");

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
    }

    //The remove settings for the user to remove a Doctor/nurse
    public void remove_settings() {
        c_mode = 1;

    }

    //The remove settings for the user to remove a Doctor/nurse
    public void edit_settings() {
        c_mode = 2;


        //sets the properties


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

    //sets up the tree
    public void setUpTreeView(){
        ID_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, Integer>("rID"));
        username_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rUsername"));
        firstName_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rFirstName"));
        lastName_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rLastName"));
        permissions_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, Integer>("rPermissions"));
        //permissions_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, Integer>("rPermissions"));
        //password_TableColumn.setCellValueFactory(new PropertyValueFactory<adminTable, String>("rPassword"));

        int id, permissions;
        String userName, firstName, lastName;
        ResultSet rset = databaseController.getTableSet("ADMIN");
        ObservableList<adminTable> data = FXCollections.observableArrayList();

        try {
            while (rset.next()){
                id = rset.getInt("ID");
                firstName = rset.getString("FIRSTNAME");
                lastName = rset.getString("LASTNAME");
                userName = rset.getString("USERNAME");
                permissions = rset.getInt("PERMISSIONS");
                System.out.println(String.format("%s %s %s %d", firstName, lastName, userName, permissions));
                //Table table = new Table(id, firstName, lastName, title, department, roomNum);
                data.add(new adminTable(id, userName, firstName, lastName, permissions));
            }
            rset.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        //Getting the information from the table View to the user
        Table_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    givID = Table_TableView.getSelectionModel().getSelectedItem().getrID();
                    givUsername = Table_TableView.getSelectionModel().getSelectedItem().getrUsername();
                    givFirstN = Table_TableView.getSelectionModel().getSelectedItem().getrFirstName();
                    givLastN = Table_TableView.getSelectionModel().getSelectedItem().getrLastName();
                    givPermissions = Table_TableView.getSelectionModel().getSelectedItem().getrPermissions();

                    mode_ChoiceBox.setValue("Edit");
                    id_textField.setText(Integer.toString(givID));
                    userName_TextField.setText(givUsername);
                    firstName_TextField.setText(givFirstN);
                    lastName_TextField.setText(givLastN);
                    if(givPermissions == 2){
                        isAdmin_CheckBox.setSelected(true);
                    }else{
                        isAdmin_CheckBox.setSelected(false);
                    }
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



    //Sets the english labels
    public void englishButtons_Labels(){
        //Buttons
        c_language = 0;
        MainMenu_Button.setText("Main Menu");
        cancel_Button.setText("Clear");
        submit_Button.setText("Submit");


        //TextField
        search_textField.setPromptText("search");
        userName_TextField.setPromptText("Username");
        firstName_TextField.setPromptText("First Name");
        lastName_TextField.setPromptText("Last Name");
        newPassword_TextField.setPromptText("Password");


        //Labels
        mainTitle_Label.setText("Administrators");
        subTitle_Label.setText("Manage Administrators");
        Mode_Label.setText("Mode:");
        username_Label.setText("Username:");
        firstName_Lab.setText("First Name:");
        lastName_Label.setText("Last Name:");
        password_Label.setText("New password");
        queryStatus.setText("Query Status");

        //COLUMNS
        username_TableColumn.setText("Username");
        firstName_TableColumn.setText("First Name");
        lastName_TableColumn.setText("Last Name");
        permissions_TableColumn.setText("Permissions");



    }

    //sets the spanish labels
    public void spanishButtons_Labels(){
        c_language = 1;
        //Buttons
        MainMenu_Button.setText("Menu Principal");
        cancel_Button.setText("Borrar");
        submit_Button.setText("Enviar");

        //TextField
        search_textField.setPromptText("buscar");
        userName_TextField.setPromptText("Usuario");
        firstName_TextField.setPromptText("Nombre");
        lastName_TextField.setPromptText("Appellido");
        newPassword_TextField.setPromptText("Contrasena");

        //Labels
        mainTitle_Label.setText("Administradores");
        subTitle_Label.setText("Control de Administradores");
        Mode_Label.setText("Modo:");
        username_Label.setText("Usuario:");
        firstName_Lab.setText("Nombre:");
        lastName_Label.setText("Apellido:");
        password_Label.setText("Nueva Contrasena");
        queryStatus.setText("Estatus");

        //Columns
        username_TableColumn.setText("Usuario");
        firstName_TableColumn.setText("Nombre");
        lastName_TableColumn.setText("Apellido");
        permissions_TableColumn.setText("Permisos");

    }


    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

}
