package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;

/**
 * Created by AugustoR on 3/31/17.
 */
public abstract class AbsController {


    //enables screen switching
    public FXMLLoader switch_screen(AnchorPane BGCurrentanchor, String viewPath){

        try {//Try do this
            AnchorPane pane;
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            pane = loader.load();

            BGCurrentanchor.getChildren().setAll(pane);
            setAnchorPane(pane);

            return loader;

        }catch(IOException e) { //Did not load screen
            System.out.println("Error loading the screen");
            //There could be information too
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error loading Screen");
            alert.setContentText("Cannot load screen");
            alert.showAndWait();


            e.printStackTrace();
            return null;
        }

    }

    //initializes the anchor pane
    public void setAnchorPane (AnchorPane pane){
        AnchorPane.setTopAnchor(pane,0.0);
        AnchorPane.setLeftAnchor(pane,0.0);
        AnchorPane.setRightAnchor(pane,0.0);
        AnchorPane.setBottomAnchor(pane,0.0);
    }


}
