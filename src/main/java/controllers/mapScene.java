package controllers;

import javafx.scene.shape.Circle;

/**
 * Created by Griffin on 4/6/2017.
 */
public abstract class mapScene extends AbsController {
    public abstract void sceneEvent(int x,int y,Circle c);
    public abstract void rightClickEvent(int x, int y, Circle c, int mode);
    public abstract void edgeClickRemove(int x1, int y1, int x2, int y2);
    public abstract void showMultifloorMenu(int x, int y, Circle c);


    //public abstract void doubleClickEvent(int x, int y, Circle c, int mode);

}
