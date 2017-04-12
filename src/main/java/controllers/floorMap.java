package controllers;

import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
/**
 * Created by griffincecil on 4/10/2017.
 */
//part of the proxy implementation of map images

public class floorMap implements mapImage {

    int mapLevel;
    //String newMapString;

    public floorMap(int floorNum) {
        this.mapLevel = floorNum;
        //newMapString = loadMap(floorNum);
    }

    public String loadMap(int floorNum) {
        String path;

        path = "..\\images\\cleaned" + floorNum;

        return path;
    }

    //take an image viewer and add the map to its children with correct formatting
    @Override
    public void display(ImageView pane){
        pane.setImage(new Image("/images/cleaned" + mapLevel + ".png"));
    }
}
