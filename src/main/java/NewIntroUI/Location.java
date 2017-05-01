package NewIntroUI;

/**
 * Created by MZ on 4/29/17.
 */
public class Location {

    public double weight = 0;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    //int xpos, ypos, floor, permissions;
    int floor;
    private int permissions;
    private String type, name, roomNum, associatedProFirst, associatedProLast, associatedProTitle;

    public Location(String type, String name, String roomNum, String associateProFirst,
                    String associatedProLast,String associatedProTitle, int permissions, int floor) {
        this.associatedProFirst = associateProFirst;
        this.associatedProLast = associatedProLast;
        this.associatedProTitle = associatedProTitle;
        this.type = type;
        this.name = name;
        this.roomNum = roomNum;
        this.permissions = permissions;
        this.floor = floor;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getAssociatedProFirst() {
        return associatedProFirst;
    }

    public void setAssociatedProFirst(String associatedProFirst) {
        this.associatedProFirst = associatedProFirst;
    }

    public String getAssociatedProLast() {
        return associatedProLast;
    }

    public void setAssociatedProLast(String associatedProLast) {
        this.associatedProLast = associatedProLast;
    }

    public String getAssociatedProTitle() {
        return associatedProTitle;
    }

    public void setAssociatedProTitle(String associatedProTitle) {
        this.associatedProTitle = associatedProTitle;
    }
}
