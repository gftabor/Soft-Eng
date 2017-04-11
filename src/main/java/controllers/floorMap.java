package controllers;

/**
 * Created by griffincecil on 4/10/2017.
 */
//part of the proxy implementation of map images

public class floorMap implements mapImage {

    int mapLevel;

    public floorMap(int floorNum) {
        this.mapLevel = floorNum;
    }

    public void loadMap() {

    }

    @Override
    public void display(){}
}
