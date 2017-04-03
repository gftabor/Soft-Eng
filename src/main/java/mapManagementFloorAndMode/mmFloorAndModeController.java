package mapManagementFloorAndMode;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

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

    public void emergencyButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit Button");
    }

    public void mainMenuButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");
    }

    public void setUserString(String user){username_Label.setText(user); }

    public void setModeChoices(){
        mode_ChoiceBox.getItems().addAll("Add", "Remove", "Edit");
    }
    public void setTitleChoices(){
        title_ChoiceBox.getItems().addAll("Doctor's Office", "Food Service", "Restroom");
    }

    //Check the current node
    public int check_mode(){

        String c_mode = mode_ChoiceBox.getValue();
        System.out.println(c_mode);
        if(c_mode.equals("Add")){
            return 0;
        }else if(c_mode.equals("Remove")){
            return 1;
        }else if(c_mode.equals("Edit")){
            return 2;

        }else if(c_mode == null){
            return 3;

        }else{
            return 4;
        }
    }

    public void create_Button(){
        System.out.println("checking button");
        int mode = check_mode();
        if(mode == 0){
            System.out.println("make button");
            Button btK = new Button("ok");
            // this code drags the button
            btK.setOnMouseDragged(e -> {
                btK.setLayoutX(e.getSceneX());
                btK.setLayoutY(e.getSceneY());
            });
            backgroundAnchorPane.getChildren().add(btK);
        }else{

        }
    }

    public void a(){
        System.out.println("a");
    }
    public void b(){
        System.out.println("b");
    }
    public void c(){
        System.out.println("c");
    }
    public void d(){
        System.out.println("d");
    }
    public void e(){
        System.out.println("e");
    }

}
