package mapManagementNodeInformation;

import DBController.DatabaseController;
import controllers.Professional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.Accordion;
import javafx.scene.control.TreeView;

import javax.xml.crypto.Data;


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
    private TextField room_TextField;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private TreeView<String> directory_TreeView;

    @FXML
    private ChoiceBox mode_ChoiceBox;


    boolean flag = false;


    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    public void cancelButton_Clicked() {
        System.out.println("The user has clicked the cancel Button");

    }

    public void submitButton_Clicked() {
        System.out.println("The user has clicked the submit Button");
        final String tempID = id_TextField.getText();
        final String tempFirstName = Firstname_TextField.getText();
        final String tempLastName = lastName_TextField.getText();
        switch(title_choiceBox.getValue()) {
            case "Doctor":
                databaseController.newProfessional(tempID, 4, 5, 0,
                        tempFirstName, tempLastName, "Doctor");
                System.out.println("Adding doctor");
                break;
            case "Nurse":
                databaseController.newProfessional(tempID, 0, 0, 0,
                        tempFirstName, tempLastName, "Nurse");
                System.out.println("Adding Nurse");
                break;
            default:
                System.out.println("Nothing selected for mode");
                break;
        }
        createDirectoryTreeView();
    }

    public void emergencyButton_Clicked() {
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }


    public void mainMenuButton_Clicked() {
        System.out.println("The user has clicked the sign out Button");
        switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");

    }

    //Creates the directory of the tree view
    public void createDirectoryTreeView() {
        TreeItem<String> root, doctors, nurses;

        root = new TreeItem<>("List of Directories");
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
        doctors = makeBranch("Doctor", root);
        int i = 0;
        while (i < doctorsList.size()) {

            makeBranch(doctorsList.get(i).getFirstName() + " " +
                    doctorsList.get(i).getLastName(), doctors);
            i++;
        }
        if(!flag){
          doctors.setExpanded(false);
        }

        i = 0;
        nurses = makeBranch("Nurse", root);
        while (i < nursesList.size()) {

            makeBranch(nursesList.get(i).getFirstName() + " " +
                    nursesList.get(i).getLastName(), nurses);
            i++;
        }
        if(!flag) {
            nurses.setExpanded(false);
        }
        directory_TreeView.setRoot(root);
        directory_TreeView.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) -> {
                    if (newValue != null) {
                        System.out.println(newValue.getValue());
                    }
                });
        flag = false;
    }

    //Create branches
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    //set the title choices for the user
    public void setTitleChoices() {
        title_choiceBox.getItems().addAll("Doctors", "Nurses");
        title_choiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        // Do validation
                        System.out.println(newValue);
                        if(newValue.intValue()==0){
                            System.out.println("Hello world");
                            //create_Button();
                        } else if (newValue.intValue() == 1 || newValue.intValue() == 2) {
                            //admin_FloorPane.getChildren().remove(btK);
                        }
                    }
                });
    }

    //Sets the choices for the mode Add, edit remove
    public void setModeChoices(){
        mode_ChoiceBox.getItems().addAll("Add", "Remove", "Edit");
        mode_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        // Do validation
                        System.out.println(newValue);

                        //Sets the mode to Add
                        if(newValue.intValue()==0){
                            add_settings();

                            //Sets the mode to remove
                        } else if (newValue.intValue() == 1) {
                            remove_settings();

                            //Sets the mode to edit
                        }else if(newValue.intValue() == 2){
                            edit_settings();

                        }
                    }
                });

    }

    //The add settings for the user to add a Doctor/nurse
    public void add_settings(){
        System.out.println("Add settings");
    }

    //The remove settings for the user to remove a Doctor/nurse
    public void remove_settings(){
        System.out.println("remove settings");

    }

    //The remove settings for the user to remove a Doctor/nurse
    public void edit_settings(){
        System.out.println("edit settings");

    }
}


