package controllers;

import javafx.scene.shape.Circle;

/**
 * Created by Griffin on 4/6/2017.
 */
public abstract class mapScene extends AbsController {
    public abstract void sceneEvent(int x,int y,Circle c);
    public abstract void rightClickEvent(int x, int y, Circle c);

}
