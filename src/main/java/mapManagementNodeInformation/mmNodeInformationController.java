package mapManagementNodeInformation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.scene.control.Accordion;
import javafx.scene.control.TreeView;



/**
 * Created by AugustoR on 3/31/17.
 */
public class mmNodeInformationController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label currentAdmin_Label;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private TextField name_TextField;

    @FXML
    private TextField room_TextField;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private TreeView<String> directory_TreeView;





    public void cancelButton_Clicked(){
        System.out.println("The user has clicked the cancel Button");

    }

    public void submitButton_Clicked(){
        System.out.println("The user has clicked the submit Button");

    }

    public void emergencyButton_Clicked(){
        switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }


    public void mainMenuButton_Clicked(){
        System.out.println("The user has clicked the sign out Button");
        switch_screen(backgroundAnchorPane, "/views/adminMenuStartView.fxml");

    }

    //Creates the directory of the tree view
    public void createDirectoryTreeView(){
        TreeItem<String> root,doctors,nurses;

        root = new TreeItem<>("List of Directories");
        root.setExpanded(true);

        doctors = makeBranch("Doctor's", root);
        makeBranch("Doctor A", doctors);
        makeBranch("Doctor B", doctors);
        makeBranch("Doctor C", doctors);
        doctors.setExpanded(false);

        nurses = makeBranch("Nurses'", root);
        makeBranch("Nurse A", nurses);
        makeBranch("Nurse B", nurses);
        nurses.setExpanded(false);

        directory_TreeView.setRoot(root);

        directory_TreeView.getSelectionModel().selectedItemProperty()
                .addListener((v, oldValue, newValue) ->{
                    if(newValue != null){
                        System.out.println(newValue.getValue());
                    }
                });


    }

    //Create branches
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent){
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }



}
