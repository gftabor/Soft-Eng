package main.java;

import java.util.ArrayList;

/**
 * Created by mylena on 4/1/17.
 */
public class Node {
    private int posX;
    private int posY;
    private boolean hidden;
    private String name;

    private ArrayList edges = new ArrayList<Edge>();

    public Node(int posX, int posY, boolean hidden, String name) {
        this.posX = posX;
        this.posY = posY;
        this.hidden = hidden;
        this.name = name;
    }


}
