package controllers;

/**
 * Created by Griffin on 4/10/2017.
 */
//part of the proxy implementation of map images

public class proxyMap implements mapImage {

    private int mapLevel;
    private floorMap newFloorMap;

    public proxyMap(int floorNum) {
        this.mapLevel = floorNum;
    }



    @Override
    public void display(){
        if (newFloorMap == null) {
            newFloorMap = new floorMap(mapLevel);
        }
        newFloorMap.display();
    }
}
