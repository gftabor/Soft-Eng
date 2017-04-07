package hospitalDirectorySearch;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;


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


    int iNumber = 1;
    //Flag to check if the user has selected a first and second choice
    int flag = 0;


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
   ObservableList<Table> data = FXCollections.observableArrayList(
           new Table(iNumber++, "Wilson", "Wong", "Doctor","Ginecologo", "AS"),
           new Table (iNumber++, "Augusto", "Rolando", "Nurse","Ginecologo", "AS"),
           new Table (iNumber++, "Mason", "Handy", "Doctor","Optometrista", "B3")



   );

    /*@FXML
    public void clickItem(MouseEvent event)
    {
        if (event.getClickCount() == 2) //Checking double click
        {
            System.out.println(Table_TableView.getSelectionModel().getSelectedItem().getrFirstName());
            System.out.println(Table_TableView.getSelectionModel().getSelectedItem().getrLastName());
            System.out.println(Table_TableView.getSelectionModel().getSelectedItem().getrID());
            error_Label.setText(Table_TableView.getSelectionModel().getSelectedItem().getrFirstName());
        }
    }*/

    //sets up the tree
    public void setUpTreeView(){
        ID_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, Integer>("rID"));
        firstName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rFirstName"));
        lastName_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rLastName"));
        title_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rTitle"));
        department_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rDepartment"));
        room_TableColumn.setCellValueFactory(new PropertyValueFactory<Table, String>("rRoom"));

        Table_TableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() > 1) {
                    if(flag == 0) {
                        from_TextField.setText(Table_TableView.getSelectionModel().getSelectedItem().getrFirstName());
                        search_TextField.setText("");
                        flag++;
                    }else if(flag == 1){
                        to_TextField.setText(Table_TableView.getSelectionModel().getSelectedItem().getrFirstName());
                        search_TextField.setText("");
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
                        return true;

                    }else if(Table.getrLastName().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(Table.getrDepartment().toLowerCase().contains(lowerCaseFilter)){
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


}
