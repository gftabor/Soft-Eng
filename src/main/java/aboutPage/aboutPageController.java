package aboutPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class aboutPageController extends controllers.AbsController{

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label developed_Label;

    @FXML
    private Label SubTitle_Label;

    @FXML
    private Label Description3_Label;

    @FXML
    private Label Description4_Label;

    @FXML
    private Label Description5_Label;

    @FXML
    private Label Description1_Label;

    @FXML
    private Label Description2_Label;

    @FXML
    private Label Description6_Label;

    @FXML
    private Label Description7_Label;

    @FXML
    private Label subTitle_Label;

    @FXML
    private Label prof_Label;

    @FXML
    private Label SA_Label;

    @FXML
    private Label subTitlle2_Lable;

    @FXML
    private Label hosp_Label;

    @FXML
    private Label and_Label;

    @FXML
    private Label hospital_Label;

    @FXML
    private Button main_Menu;

    int c_language = 0;
    String loggedIn;

    private int permissionLevel;

    @FXML
    void mainMenu_Clicked() {
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        //patientMenuStart.patientMenuStartController controller = loader.getController();
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            controller.setWelcome(loggedIn);
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
            controller.setWelcome(loggedIn);
        }
        controller.setPermissionLevel(1);
        controller.loginOrOut(1,c_language);

    }


    //sets the current language given information form other screens
    public void setCurrentLanguage(int i){
        c_language = i;
    }

    //set the logging admin
    public void setAdmin(String user){
        loggedIn = user;
    }


    //Sets the english labels
    public void englishLabels(){
        developed_Label.setText("Developed By:");
        SubTitle_Label.setText("Team E \"The Gray Griffins\"");

        Description1_Label.setText("Lead Software Engineer");
        Description2_Label.setText("Assistant Lead Software Engineer");
        Description3_Label.setText("Project Manager");
        Description4_Label.setText("Product Owner");
        Description5_Label.setText("Test Engineer");
        Description6_Label.setText("Documentation Analyst");
        Description7_Label.setText("Software Engineers");

        subTitle_Label.setText("WPI CS 3733 Software Engineering");
        prof_Label.setText("Professor Wilson Wong");
        SA_Label.setText("Student Assistant Dominik Smreczak");

        subTitlle2_Lable.setText("Special Thanks To");
        hosp_Label.setText("Brigham and Women's Faulkner Hospital");
        and_Label.setText("and");
        hospital_Label.setText("Andrew Shinn");

    }

    //sets the spanish Labels
    public void spanishLabels(){
        developed_Label.setText("Desarrollado por:");
        SubTitle_Label.setText("Equipo E \"The Gray Griffins\"");

        Description1_Label.setText("Ingeniero Principal de Sistemas");
        Description2_Label.setText("Ingeniero Asistente de Sistemas");
        Description3_Label.setText("Gerente del Proyecto");
        Description4_Label.setText("Dueno del Producto");
        Description5_Label.setText("Ingeniero de Pruebas");
        Description6_Label.setText("Analista de Documentacion");
        Description7_Label.setText("Ingenieros de Sistemas");

        subTitle_Label.setText("WPI CS 3733 Software Engineering");
        prof_Label.setText("Profesor Wilson Wong");
        SA_Label.setText("Studiente Asistente Dominik Smreczak");

        subTitlle2_Lable.setText("Agradecimientos Especiales A:");
        hosp_Label.setText("Hospital Faulkner Brigham and Women");
        and_Label.setText("y");
        hospital_Label.setText("Andrew Shinn");

    }

    //Gets the permissions
    public int getPermissionLevel() {
        return permissionLevel;
    }

    //Sets the permissions
    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
        System.out.println("Setting permission level to: " + permissionLevel);

    }

}
