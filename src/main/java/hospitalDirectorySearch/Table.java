package hospitalDirectorySearch;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by AugustoR on 4/6/17.
 */
public class Table {
    //ID fn ln t d r
    private final SimpleIntegerProperty rID;
    private final SimpleStringProperty rFirstName;
    private final SimpleStringProperty rLastName;
    private final SimpleStringProperty rTitle;
    private final SimpleStringProperty rDepartment;
    private final SimpleStringProperty rRoom;

    public Table(int sID, String sFirstName, String sLastName, String sTitle, String sDepartment, String sRoom){
        this.rID = new SimpleIntegerProperty(sID);
        this.rFirstName = new SimpleStringProperty(sFirstName);
        this.rLastName = new SimpleStringProperty(sLastName);
        this.rTitle = new SimpleStringProperty(sTitle);
        this.rDepartment = new SimpleStringProperty(sDepartment);
        this.rRoom = new SimpleStringProperty(sRoom);

    }

    //Getters
    public int getrID() {
        return rID.get();
    }

    public String getrFirstName() {
        return rFirstName.get();
    }

    public String getrLastName() {
        return rLastName.get();
    }

    public String getrTitle() {
        return rTitle.get();
    }

    public String getrDepartment() {
        return rDepartment.get();
    }

    public String getrRoom() {
        return rRoom.get();
    }


    //Setters
    public void setrID(int rID) {
        this.rID.set(rID);
    }

    public void setrFirstName(String rFirstName) {
        this.rFirstName.set(rFirstName);
    }

    public void setrLastName(String rLastName) {
        this.rLastName.set(rLastName);
    }

    public void setrTitle(String rTitle) {
        this.rTitle.set(rTitle);
    }

    public void setrDepartment(String rDepartment) {
        this.rDepartment.set(rDepartment);
    }

    public void setrRoom(String rRoom) {
        this.rRoom.set(rRoom);
    }

    //Properties Might not be useful
    public SimpleIntegerProperty rIDProperty() {
        return rID;
    }

    public SimpleStringProperty rFirstNameProperty() {
        return rFirstName;
    }

    public SimpleStringProperty rLastNameProperty() {
        return rLastName;
    }


    public SimpleStringProperty rTitleProperty() {
        return rTitle;
    }

    public SimpleStringProperty rDepartmentProperty() {
        return rDepartment;
    }

    public SimpleStringProperty rRoomProperty() {
        return rRoom;
    }
}
