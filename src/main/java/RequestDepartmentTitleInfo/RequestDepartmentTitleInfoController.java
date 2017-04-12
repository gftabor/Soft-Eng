package RequestDepartmentTitleInfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by AugustoR on 4/10/17.
 */


public class RequestDepartmentTitleInfoController  extends controllers.AbsController {
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label department_Label;

    @FXML
    private TextField department_TextField;

    @FXML
    private Label title_Label;

    @FXML
    private TextField title_TextField;

    @FXML
    private Button clear_Button;

    @FXML
    private Button submit_Button;


    int c_language = 0;

    boolean empty = false;



    public void submitButton_Clicked() {
        System.out.println("Submit button is clicked");

        empty = (department_TextField.getText().equals("") || department_TextField.getText() == null
                || title_TextField.getText().equals("") || title_TextField.getText() == null);

        if(!empty) {
            FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/mmNodeInformationView.fxml");
            mapManagementNodeInformation.mmNodeInformationController controller = loader.getController();
            controller.saveToDatabase(department_TextField.getText(), title_TextField.getText());
        }else{
            //There could be information too
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Submitting information");
            alert.setContentText("Cannot submit empty information");
            alert.showAndWait();
        }
    }

    public void clearButton_Clicked(){
        department_TextField.setText("");
        title_TextField.setText("");
    }

    public void setC_language(int i){
        c_language = i;
    }





}
