package main.java;

import java.util.ArrayList;

/**
 * Created by mylena on 4/1/17.
 */
public class Node {
    private int posX;
    private int posY;
    private boolean isHidden;
    private boolean isEnabled;
    private String name;

    private ArrayList edges = new ArrayList<Edge>();

    public Node(int posX, int posY, boolean hidden, boolean enabled, String name) {
        this.posX = posX;
        this.posY = posY;
        this.isHidden = hidden;
        this.isEnabled = enabled;
        this.name = name;
    }


}
