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

    HttpRequests httpRequests = new HttpRequests("4480afa9b8b364e30ba03819f3e9eff5", "Pz9VFT8AP3g_Pz8_dz84cRY_bz8_Pz8M ", true, true);
    Webcam webcam = Webcam.getDefault();
    HBox imageStrip = new HBox();
    public Thread cameraThread;


    private static facialRecognition instance = new facialRecognition();
    public static facialRecognition getInstance(){
      return instance;
    }
    private facialRecognition(){

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
    }
    public void stop(){
        facialOn = false;
    }
    public void start(VBox root) {
        try {
            httpRequests.trainSearch(new PostParameters().setFacesetName("Admins"));
        }catch(Exception e){System.out.println("bug");}
        facialOn = true;
        root.setPadding(new Insets(20));
        imageStrip.setPadding(new Insets(20));
        imageStrip.setAlignment(Pos.CENTER);

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
}
