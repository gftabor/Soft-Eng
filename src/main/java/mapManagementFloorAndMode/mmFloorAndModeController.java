package mapManagementFloorAndMode;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

import javax.xml.soap.Text;

/**
 * Created by AugustoR on 3/31/17.
 */
public class mmFloorAndModeController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button emergency_Button;

    @FXML
    private Spinner<Integer> floorChoices_Spinner;

    @FXML
    private ChoiceBox<String> mode_ChoiceBox;

    @FXML
    private ChoiceBox<String> title_ChoiceBox;

    @FXML
    private CheckBox hidden_CheckBox;

    @FXML
    private Label username_Label;

    @FXML
    private Button submit_Button;

    @FXML
    private Button mainMenu_Button;

    //Add the name and Room
    @FXML
    private TextField name_TextField;

    @FXML
    private TextField room_TextField;

    @FXML
    private Pane admin_FloorPane;


    private Button btK;
    public void emergencyButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    //submit button is clicked
    //To Do - use this to send the information of your changes to the DB to get updated
    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit Button");
        System.out.println(mode_ChoiceBox.getValue());
        name_TextField.setText("HELLO");
        room_TextField.setText("WORLD");
        

        switch(mode_ChoiceBox.getValue()) {
            case "Add":
                System.out.println("Mode = add");
                //newNode(btK.getLayoutX(), btK.getLayoutY(),hidden_CheckBox.isSelected(), name,int floor)
                break;
            case "Edit":
                System.out.println("Mode = edit");
                break;
            case "Remove":
                System.out.println("Mode = remove");
                break;
            default:
                System.out.println("Nothing selected for mode");
                break;
        }
    }

    //Change to main Menu
    public void mainMenuButton_Clicked(){

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(username_Label.getText());
    }

    public void setUserString(String user){username_Label.setText(user); }

    public void setModeChoices() {
        mode_ChoiceBox.getItems().addAll("Add", "Remove", "Edit");
        mode_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        // Do validation
                        System.out.println(newValue);
                        if(newValue.intValue()==0){
                            create_Button();
                        } else
                        {
                            backgroundAnchorPane.getChildren().remove(btK);
                        }
                    }
                });
    }
        public void setTitleChoices(){
        title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom");
    }
    public void create_Button(){
        System.out.println("checking button");
            System.out.println("make button");
            btK = new Button("ok");
            // this code drags the button
            final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());

            btK.setOnMouseDragged(e -> {
                if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                        && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                    btK.setLayoutX(e.getSceneX() - paneBounds.getMinX());
                    btK.setLayoutY(e.getSceneY() - paneBounds.getMinY());
                }
            });
            admin_FloorPane.getChildren().add(btK);
            btK.toFront();

    }

}
