package mapManagementNodeInformation;

import DBController.DatabaseController;
import controllers.Professional;
import hospitalDirectorySearch.Table;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.control.TextField.*;
import org.controlsfx.control.textfield.TextFields;

import javax.xml.transform.Result;


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
    private TextField testTextField;


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
    private TextField profile_textField;



    /**
     * Flags for passing different info
     */
    // Flag for current mode chosen (add, edit, remove)
    int c_mode = -1;



    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    public void cancelButton_Clicked() {
        System.out.println("The user has clicked the cancel Button");

    }

    public void submitButton_Clicked(){
        System.out.println("Hello world");
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

     /* public void submitButton_Clicked() {

        System.out.println("The user has clicked the submit Button");
        final String tempID = id_TextField.getText();
        String tempFirstName = Firstname_TextField.getText();
        String tempLastName = lastName_TextField.getText();
        String profile = profile_textField.getText();
        boolean added = true;
        empty_inputs = (tempFirstName == null || tempFirstName.equals("")
                || tempLastName == null || tempLastName.equals(""));

        if (c_mode == 0) {
            // add
           if(!empty_inputs) {
               tempLastName = tempLastName.replaceAll("\\s", "");
               tempFirstName = tempFirstName.replaceAll("\\s", "");
           }

            if (!empty_inputs) {
                switch (title_choiceBox.getValue()) {
                    case "Doctor":
                        openDirectory = 1;
                        System.out.println("Adding new professional -------------------");
                        added = databaseController.newProfessional(tempFirstName,
                                tempLastName, "Doctor", profile);
                        System.out.println("Added the professional ====================");
                        break;
                    case "Nurse":
                        openDirectory = 2;
                        added = databaseController.newProfessional(tempFirstName,
                                tempLastName, "Nurse", profile);
                        break;
                    default:
                        System.out.println("Nothing selected for mode");
                        break;
                }
            }


        } else if (c_mode == 1) {
            // remove
            if (title_choiceBox.getValue().equals("Doctor")) {
                openDirectory = 1;
            } else if (title_choiceBox.getValue().equals("Nurse")) {
                openDirectory = 2;
            }

            if(!empty_inputs) {
                databaseController.deleteProfessional(id_TextField.getText());
            }

        } else if (c_mode == 2) {
            // edit
            if (title_choiceBox.getValue().equals("Doctor")) {
                openDirectory = 1;
            } else if (title_choiceBox.getValue().equals("Nurse")) {
                openDirectory = 2;
            }
            int id = 0;
            if(!empty_inputs) {
                ResultSet rset = databaseController.getProfessional(Integer.parseInt(id_TextField.getText()));
                System.out.println("===================== editing");

                databaseController.EditProfessional(Integer.parseInt(id_TextField.getText()), tempFirstName,
                        tempLastName, title_choiceBox.getValue(), profile_textField.getText());
            }
        } else {
            // nothing
        }

        if (c_mode != -1) {
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/mmNodeInformationView.fxml");
            mapManagementNodeInformation.mmNodeInformationController controller = loader.getController();
            controller.setOpenDirectory(openDirectory);
            controller.createDirectoryTreeView();
            controller.setTitleChoices();
            controller.setModeChoices();
            controller.setCurrentMode(c_mode);
            controller.setUser(currentAdmin_Label.getText());
            System.out.println(openDirectory);
            if (!added) {
                controller.setError("This ID already exists!");
            } else if(empty_inputs){
                controller.setError("You have entered an invalid input");
            }
        }
    } */

    //Creates the directory of the tree view
    /*public void createDirectoryTreeView() {
        TreeItem<String> root, doctors, nurses;

        root = new TreeItem<>("Professionals");
        root.setExpanded(true);

        ResultSet professionalsRset = databaseController.getTableSet("PROFESSIONAL");
        ArrayList<Professional> doctorsList = new ArrayList<>();
        ArrayList<Professional> nursesList = new ArrayList<>();

        try {
            String firstName, lastName, type, id;

            while (professionalsRset.next()) {
                firstName = professionalsRset.getString("FIRSTNAME");
                lastName = professionalsRset.getString("LASTNAME");
                type = professionalsRset.getString("TYPE");
                id = professionalsRset.getString("ID");
                if (type.equals("doctor") || type.equals("Doctor") || type.equals("DOCTOR")) {
                    doctorsList.add(new Professional(firstName, lastName, type, id));
                } else if (type.equals("nurse") || type.equals("Nurse") || type.equals("NURSE")) {
                    nursesList.add(new Professional(firstName, lastName, type, id));
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        doctors = makeBranch("Doctors", root);
        int i = 0;
        while (i < doctorsList.size()) {

            makeBranch(doctorsList.get(i).getFirstName() + " " +
                    doctorsList.get(i).getLastName(), doctors);
            i++;
        }

        doctors.setExpanded(false);
        if (openDirectory == 1) {
            doctors.setExpanded(true);
        }

        i = 0;
        nurses = makeBranch("Nurses", root);
        while (i < nursesList.size()) {

            makeBranch(nursesList.get(i).getFirstName() + " " +
                    nursesList.get(i).getLastName(), nurses);
            i++;
        }

        nurses.setExpanded(false);
        if (openDirectory == 2) {
            nurses.setExpanded(true);
        }

//        directory_TreeView.setRoot(root);
//        directory_TreeView.getSelectionModel().selectedItemProperty()
//                .addListener((v, oldValue, newValue) -> {
//                    if (newValue != null) {
//                        System.out.println(newValue.getValue());
//                        if(!(newValue.getValue().equals("Professionals"))) {
//                            pullProfessional(newValue.getValue());
//                        }
//                    }
//                });

    } */

    // this is to include id to allow for multiple doctors
   /* public void pullProfessional(String fullName) {
        ResultSet rset;
        String id = null;
        String firstName;
        String lastName;
        String type = null;

        String[] bothNames = fullName.split("\\s+");
        firstName = bothNames[0];
        lastName = bothNames[1];

        try {
            rset = databaseController.getProfessional(firstName, lastName);
            while (rset.next()) {
                id = rset.getString("ID");
                type = rset.getString("TYPE");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        int flag = 1;
        if ("Doctor".equals(type)) {
            flag = 0;
        }

        if (c_mode != 0) {
            title_choiceBox.getSelectionModel().select(flag);
            id_TextField.setText(id);
            Firstname_TextField.setText(firstName);
            lastName_TextField.setText(lastName);
        }
    } */


    //Create branches
   /* public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    } */


   /* public void setOpenDirectory(int i) {
        System.out.println("LOOK THIS");
        System.out.println(i);
        openDirectory = i;
    } */


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
        profile_textField.setText("");
        id_TextField.setText("");
        Firstname_TextField.setText("");
        lastName_TextField.setText("");
        id_TextField.setPromptText("Automatically Generated ID");
        Firstname_TextField.setPromptText("First");
        lastName_TextField.setPromptText("Last");
        //Sets the properties
        title_choiceBox.setDisable(false);
        profile_textField.setDisable(false);
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
        profile_textField.setDisable(true);
        testTextField.setDisable(true);
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
        profile_textField.setDisable(false);
        testTextField.setDisable(false);
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
        ArrayList<String> profiles = new ArrayList<>();

        ResultSet rset_profiles = databaseController.getProfileNames();
        ResultSet rset = databaseController.getRoomNames();
        try {
            while (rset.next()){
                rooms.add(rset.getString("ROOMNUM"));
            }
            while (rset_profiles.next()){
                profiles.add(rset_profiles.getString("PROFILE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        TextFields.bindAutoCompletion(testTextField,rooms);

        TextFields.bindAutoCompletion(profile_textField, profiles);
    }

}




