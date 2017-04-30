package adminLoginMain;

import NewMainMapManagement.NewMainMapManagementController;
import com.facepp.error.FaceppParseException;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.util.ImageUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.colour.Transforms;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import org.json.JSONObject;

/**
 * Created by AugustoR on 4/1/17.
 */


public class adminLoginMainController extends controllers.AbsController{
    @FXML
    private TextField username_TextField;

    @FXML
    private PasswordField password_PasswordField;

    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Button logIn_Button;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Label invalidLogInputs;

    @FXML
    private Label adminTitle_Label;

    @FXML
    private Label languageTitle_Label;

    @FXML
    private VBox root;

    @FXML
    private ChoiceBox<String> languageChoices_ChoiceBox;

    int c_language;
    HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M ", true, true);
    JSONObject result = null;

    private static final ImageView imageView = new ImageView();
    private static boolean isRunning = true;

    public void initialize() {
        try {
            httpRequests.trainSearch(new PostParameters().setFacesetName("Admins"));
        }catch(Exception e){System.out.println("bug");}
        VBox root = new VBox();
        root.setPadding(new Insets(20));

        HBox imageStrip = new HBox();
        imageStrip.setPadding(new Insets(20));
        imageStrip.setAlignment(Pos.CENTER);

        Thread cameraThread = new Thread(() -> {
            Webcam webcam = Webcam.getDefault();
            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();

            BufferedImage capture = null;
            List<BufferedImage> facesList = new ArrayList<>();

            while(isRunning) {
                capture = webcam.getImage();

               HaarCascadeDetector detector;
                detector = new HaarCascadeDetector();
                List<DetectedFace> faces = detector.detectFaces(ImageUtilities.createFImage(capture));
                facesList.clear();
                for(DetectedFace face : faces) {
                    facesList.add(ImageUtilities.createBufferedImage(face.getFacePatch()));
                }

                System.out.println("Detected " + faces.size() + " faces");
                final BufferedImage finalCapture = capture;
                Platform.runLater(() -> {
                    imageView.setImage(new javafx.scene.image.Image(new ByteArrayInputStream(convertBufferedImage(finalCapture))));
                    imageStrip.getChildren().clear();
                    for(BufferedImage img : facesList) {
                        byte[] temp = convertBufferedImage(img);
                        ImageView imgView = new ImageView(new javafx.scene.image.Image(new ByteArrayInputStream(temp)));
                        imageStrip.getChildren().add(imgView);
                    }
                });
                for(BufferedImage img : facesList) {
                    byte[] temp = convertBufferedImage(img);
                    try {
                        result = httpRequests.detectionDetect(new PostParameters().setImg(temp));
                        System.out.println(result.getJSONArray("face").getJSONObject(0).getString("face_id"));
                        System.out.println("searching");
                        result = httpRequests.recognitionSearch(new PostParameters().setFacesetName("Admins").
                                setKeyFaceId(result.getJSONArray("face").getJSONObject(0).getString("face_id")));

                        System.out.println("David "+result.getJSONArray("candidate").getJSONObject(0).getDouble("similarity"));
                        System.out.println("Griffin "+result.getJSONArray("candidate").getJSONObject(1).getDouble("similarity"));


                    } catch(FaceppParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        System.out.println("exception");
                    }
                }
            }
            webcam.close();
        });
        cameraThread.start();

        HBox hbox = new HBox(imageView);
        hbox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(hbox, imageStrip);
    }

    private byte[] convertBufferedImage(BufferedImage bufImage) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufImage, "PNG", os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    //logs the user in
    public void logInButton_Clicked(){
        AdminLoginManager loginManage = new AdminLoginManager();
        String username = username_TextField.getText();
        String password = password_PasswordField.getText();

        System.out.println("The user has clicked the log in Button");
        System.out.println(username);
        System.out.println(password);

        if(loginManage.verifyCredentials(username, password) == 1){
            System.out.println("Correct Password");

            //LOG IN ADMIN afl;kdsf;aldskf
            //*************************************************
            if(loginManage.getPermissions(username) == 2){
                System.out.println("Logging in Admin");
                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewMainMapManagementView.fxml");
                NewMainMapManagement.NewMainMapManagementController controller = loader.getController();
                controller.setC_language(c_language);
                //Set the correct username for the next scene
                //set up english labels
                if(c_language == 0){
                    controller.englishButtons_Labels();

                    //set up spanish labels
                }else if(c_language == 1){
                    controller.spanishButtons_Labels();
                }
                controller.setUserString("Admin: " + username);
                controller.setPermissionLevel(2);



                //LOG IN EMPLOYEE
                //*************************************************
            }else if(loginManage.getPermissions(username) == 1){
                System.out.println("Logging in Employee");
                FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
                //patientMenuStart.patientMenuStartController controller = loader.getController();
                NewIntroUI.NewIntroUIController controller = loader.getController();
                //sets the current language
                controller.setCurrentLanguage(c_language);
                //set up english labels
                if(c_language == 0){
                    controller.englishButtons_Labels();
                    controller.setWelcome("Employee: " + username);
                    //set up spanish labels
                }else if(c_language == 1){
                    controller.spanishButtons_Labels();
                    controller.setWelcome("Empleado: " + username);
                }
                controller.setPermissionLevel(1);

            }else{
                System.out.println("Logging in Regular User. What??");
            }




            //Check is the username/passwords inputs are empty
        }else if(username.equals("")){
            invalidLogInputs.setText("Enter your username.");

        }else if(password.equals("")){
            invalidLogInputs.setText("Enter your password.");

        }else{//Incorrect inputs
            invalidLogInputs.setText("Incorrect credentials, try again.");

        }


    }

    //Switches screen to the patient menu
    public void mainMenuButton_Clicked(){
        System.out.println("The user has clicked the main menu Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        //patientMenuStart.patientMenuStartController controller = loader.getController();
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

        //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }


        //Set the permissions
        controller.setPermissionLevel(0);

    }

    //Sets the choiceBox from the given language
    public void setLanguageChoiceBox(int l){
        //Check if english
          languageChoices_ChoiceBox.getItems().addAll("English", "Espanol");
          languageChoices_ChoiceBox.getSelectionModel().select(l);


        //Checks if the user has decided to change languages
        languageChoices_ChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        //Checks if the user wants english language
                        if (newValue.intValue() == 0) {
                            //Load English View
                            englishButtons_Labels();

                        } else if (newValue.intValue() == 1) {
                            //Load Spanish View
                            spanishButtons_Labels();
                            System.out.println("");
                        }
                    }

                });


    }

    //sets the current language to the given language
    public void setC_language(int i){
        c_language = i;
    }

    //Changes the buttons and labels to english
    public void englishButtons_Labels(){
        setC_language(0);
        //Buttons
        logIn_Button.setText("Login");
        mainMenu_Button.setText("Main Menu");

        //Labels
        adminTitle_Label.setText("Admin Login");
        languageTitle_Label.setText("Choose your language");

        //text fields
        username_TextField.setPromptText("username");
        password_PasswordField.setPromptText("password");
    }

    //Changes the buttons and labels to spanish
    public void spanishButtons_Labels(){
        setC_language(1);
        //Buttons
        logIn_Button.setText("Iniciar Sesión");
        mainMenu_Button.setText("Menu Principal");

        //Labels
        adminTitle_Label.setText("Administrador");
        languageTitle_Label.setText("Escoge tu lenguaje");

        //text fields
        username_TextField.setPromptText("usuario");
        password_PasswordField.setPromptText("contraseña");
    }



}
