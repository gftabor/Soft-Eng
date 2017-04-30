package adminLoginMain;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.github.sarxos.webcam.Webcam;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONObject;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.processing.face.detection.DetectedFace;
import org.openimaj.image.processing.face.detection.HaarCascadeDetector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * Created by Griffin on 4/30/2017.
 */


public class facialRecognition {
    JSONObject result = null;
    private static final ImageView imageView = new ImageView();
    private static boolean isRunning = true;
    private static boolean facialOn = false;
    private static String currentID;

    HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M ", true, true);
    Webcam webcam;

    HBox imageStrip = new HBox();
    public Thread cameraThread;
    public enum state{
        LOGIN, ADD, NOTHING
    }
    public void off(){
        this.nextState = state.NOTHING;

    }
    private adminLoginMainController mainScene;
    public void scan(adminLoginMainController currentLoginScene){
        this.nextState = state.LOGIN;
        mainScene = currentLoginScene;
    }
    public String getFaceID() {
        this.nextState = state.ADD;
        synchronized(this){
            while(this.nextState ==state.ADD){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return currentID;
        }
    }
    private synchronized void finished(){
        this.nextState = state.NOTHING;
        this.notifyAll();
        try {
            httpRequests.trainIdentify(new PostParameters().setGroupName("Faukner"));
        }catch(Exception e){}


    }

    private state nextState = state.NOTHING;

    public void initFace(){
        System.out.println("FACEEEEE");
        try {
            httpRequests.groupDelete(new PostParameters().setGroupName("Faukner"));
        }catch(Exception e){System.out.println("cant delete group");}

        try {

            System.out.println(httpRequests.groupCreate(new PostParameters().setGroupName("Faukner")));

            System.out.println("\nperson/create");
            ArrayList<String> personList = new ArrayList<String>();
            //for admins
            for (controllers.Admin currentAdmin: DBController.DatabaseController.getInstance().getListOfAdmins()) {
                if(currentAdmin.getFaceId() == null)
                    continue;
                try {//might already exist
                    httpRequests.personCreate(new PostParameters().setPersonName(currentAdmin.getUserName()));
                }catch(Exception e){};
                //person/add_face
                System.out.println("\nperson/add_face");
                System.out.println(httpRequests.personAddFace(new PostParameters().setPersonName(currentAdmin.getUserName()).setFaceId(currentAdmin.getFaceId())));
                //group/add_person
                System.out.println("\ngroup/add_person");
                personList.add(currentAdmin.getUserName());
            }
            if(personList !=null && personList.size()!=0) {

                new PostParameters().setGroupName("Faukner").setPersonName(personList).getMultiPart().writeTo(System.out);
                System.out.println(httpRequests.groupAddPerson(new PostParameters().setGroupName("Faukner").setPersonName(personList)));
                //Train
                JSONObject syncRet = null;
                System.out.println("\ntrain/Identify");
                syncRet = httpRequests.trainIdentify(new PostParameters().setGroupName("Faukner"));
                System.out.println(syncRet);
                System.out.println(httpRequests.getSessionSync(syncRet.getString("session_id")));
            }
        }catch(Exception e){e.printStackTrace();}
    }

    private static facialRecognition instance = new facialRecognition();
    public static facialRecognition getInstance(){
      return instance;
    }
    private facialRecognition(){
        if(isRunning)
            webcam = Webcam.getDefault();

        cameraThread = new Thread(() -> {
            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();
            BufferedImage capture = null;
            java.util.List<BufferedImage> facesList = new ArrayList<>();
            while(isRunning) if(facialOn){
                capture = webcam.getImage();

                HaarCascadeDetector detector = new HaarCascadeDetector();
                java.util.List<DetectedFace> faces = detector.detectFaces(ImageUtilities.createFImage(capture));
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
                        if(nextState.equals(state.LOGIN)){
                            result = httpRequests.recognitionIdentify(new PostParameters().setGroupName("Faukner").setImg(temp)).getJSONArray("face").getJSONObject(0);
                            for(int i =0; i<result.getJSONArray("candidate").length(); i++){
                                String username = result.getJSONArray("candidate").getJSONObject(i).getString("person_name");
                                Double confidence = result.getJSONArray("candidate").getJSONObject(i).getDouble("confidence");
                                if(confidence> 25.0){
                                    System.out.println("logging in");
                                    mainScene.alternateLogIn(username);
                                    nextState = state.NOTHING;
                                    break;

                                }
                            }
                        }
                        if(nextState.equals(state.ADD)) {

                            result = httpRequests.detectionDetect(new PostParameters().setImg(temp));
                            System.out.println(result);
                            currentID = result.getJSONArray("face").getJSONObject(0).getString("face_id");
                            System.out.println(currentID);
                            finished();
                        }
                    } catch(FaceppParseException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            webcam.close();
        });
        if(isRunning)
            cameraThread.start();
    }
    public void stop(){
        facialOn = false;
    }
    public void start(VBox root) {
        facialOn = true;
        root.setPadding(new Insets(20));
        imageStrip.setPadding(new Insets(20));
        imageStrip.setAlignment(Pos.CENTER);

        HBox hbox = new HBox(imageView);
        hbox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(imageStrip);
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
}
