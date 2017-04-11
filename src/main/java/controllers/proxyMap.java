package controllers;

import javafx.scene.image.ImageView;

/**
 * Created by griffincecil on 4/10/2017.
 */
//part of the proxy implementation of map images

public class proxyMap implements mapImage {

    private int mapLevel;
    private floorMap newFloorMap;

    public proxyMap(int floorNum) {
        this.mapLevel = floorNum;
    }

    @Override
    public void display(ImageView pane){
        if (newFloorMap == null) {
            newFloorMap = new floorMap(mapLevel);
        }
        newFloorMap.display(pane);
    }
}
