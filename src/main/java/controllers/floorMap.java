package controllers;

/**
 * Created by Griffin on 4/10/2017.
 */
//part of the proxy implementation of map images

public class floorMap implements mapImage {

    int mapLevel;

    public floorMap(int floorNum) {
        this.mapLevel = floorNum;
    }

    @Override
    public void display(){}
}
