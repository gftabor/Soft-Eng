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
    private final SimpleStringProperty rType;
    private final SimpleStringProperty rRoom;

    //Admin Management Attributes ASK GRIFFIN T
    private final SimpleStringProperty rUsername;
    private final SimpleStringProperty rPassword;


    //Constructor for an admin info
    public Table (int sID, String sFirstName, String sLastName, String sTitle, String sType, String sRoom,
                  String sUsername, String sPassword){
        this.rID = new SimpleIntegerProperty(sID);
        this.rFirstName = new SimpleStringProperty(sFirstName);
        this.rLastName = new SimpleStringProperty(sLastName);
        this.rTitle = new SimpleStringProperty(sTitle);
        this.rType = new SimpleStringProperty(sType);
        this.rRoom = new SimpleStringProperty(sRoom);
        this.rUsername = new SimpleStringProperty(sUsername);
        this.rPassword = new SimpleStringProperty(sPassword);

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

    public String getrType() {
        return rType.get();
    }

    public String getrRoom() {
        return rRoom.get();
    }
    //Admin attributes
    public String getrUsername(){return rUsername.get();}

    public String getrPassword(){return rPassword.get();}


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

    public void setrType(String rType) {
        this.rType.set(rType);
    }

    public void setrRoom(String rRoom) {
        this.rRoom.set(rRoom);
    }
    //Admin attributes
    public void setrUsername(String rUsername) {
        this.rUsername.set(rUsername);
    }

    public void setrPassword(String rPassword) {
        this.rPassword.set(rPassword);
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

    public SimpleStringProperty rTypeProperty() {
        return rType;
    }

    public SimpleStringProperty rRoomProperty() {
        return rRoom;
    }
    //Admin Attributes
    public  SimpleStringProperty rUsernameProperty() {
        return rUsername;
    }

    public  SimpleStringProperty rPasswordProperty() {
        return rPassword;
    }

}
