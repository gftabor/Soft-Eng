package patientMain;

/**
 * Created by AugustoR on 4/15/17.
 */
import DBController.DatabaseController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;

import java.util.ArrayList;

public class patientMainController extends controllers.AbsController{

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private Button admin_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Label start_Label;

    @FXML
    private TextField start_textField;

    @FXML
    private Label end_Label;

    @FXML
    private TextField end_TextField;

    @FXML
    private Button clear_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private ChoiceBox<String> filter_ChoiceBox;

    @FXML
    private TreeTableView<?> allInformation_TableView;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    @FXML
    private Label floor_Label;

    @FXML
    private Label c_Floor_Label;

    int c_language = 0;

    @FXML
    public void initialize(){
        //setLanguageChoices(c_language);
        setFloorChoices();
        setStartEndChoices();

    }

    //get an instance of database controller
    DatabaseController databaseController = DatabaseController.getInstance();

    public void setFloorChoices() {
        //Makes sure you only set the choices once
        //sets the choices and sets the current language as the top choice
        floor_ChoiceBox.getItems().addAll("1", "2", "3","4","5","6","7");
        floor_ChoiceBox.getSelectionModel().select(0);

        //Checks if the user has decided to change languages
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //System.out.println(newValue);

                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load floor 1

                        } else if (newValue.intValue() == 1) {
                            //Load floor 2

                        }else if (newValue.intValue() == 2) {
                            //Load floor 3

                        }else if (newValue.intValue() == 3) {
                            //Load floor 4

                        }else if (newValue.intValue() == 4) {
                            //Load floor 5

                        }else if (newValue.intValue() == 5) {
                            //Load floor 6

                        }else if (newValue.intValue() == 6) {
                            //Load floor 7

                        }
                    }

                });
    }

    public void setStartEndChoices(){

        ArrayList<String> roomNums = new ArrayList<>();
        ArrayList<String> professionals = new ArrayList<>();
        ArrayList<String> all = new ArrayList<>();

        roomNums = databaseController.getRoomList();
        professionals = databaseController.getProfessionalList();
        all.addAll(roomNums);
        all.addAll(professionals);


        TextFields.bindAutoCompletion(start_textField, all);


    }





}
