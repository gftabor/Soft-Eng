package hospitalDirectorySearch;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import DBController.DatabaseController;
import controllers.Professional;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;


import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private TextField serach_TextField;

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

    int iNumber = 1;


    //
    public void mainMenuButton_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(currentAdmin_Label.getText());
    }

    //
    public void emergencyButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    //
    public void clearButton_Clicked(){
        System.out.println("The user has clicked the clear button");
    }

    //
    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit button");
    }



    //DATABASE
   final ObservableList<Table> data = FXCollections.observableArrayList(
           new Table(iNumber++, "Wilson", "Wong", "Doctor","Ginecologo", "AS"),
           new Table (iNumber++, "Augusto", "Rolando", "Nurse","Ginecologo", "AS"),
           new Table (iNumber++, "Mason", "Handy", "Doctor","Optometrista", "B3")

   );

    //sets up the tree
    public void setUpTreeView(){
        ID_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
        firstName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rFirstName"));
        lastName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rLastName"));
        title_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rTitle"));
        department_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rDepartment"));
        room_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rRoom"));

        Table_TableView.setItems(data);


    }


}
