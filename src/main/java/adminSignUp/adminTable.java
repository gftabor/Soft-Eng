package adminSignUp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
 * Created by AugustoR on 4/18/17.
 */
public class adminTable {
    //ID fn ln t d r
    private final SimpleIntegerProperty rID;
    private final SimpleStringProperty rUsername;
    private final SimpleStringProperty rFirstName;
    private final SimpleStringProperty rLastName;
    private final SimpleStringProperty rPassword;




    //Constructor for an admin info
    public adminTable (int sID, String rUsername, String rFirstName, String rLastName, String rPassword){
        this.rID = new SimpleIntegerProperty(sID);
        this.rUsername = new SimpleStringProperty(rUsername);
        this.rFirstName = new SimpleStringProperty(rFirstName);
        this.rLastName = new SimpleStringProperty(rLastName);
        this.rPassword = new SimpleStringProperty(rPassword);


    }

    //Getters
    public int getrID() {
        return rID.get();
    }

    public String getrUsername() {
        return rUsername.get();
    }

    public String getrFirstName() {
        return rFirstName.get();
    }

    public String getrLastName() {
        return rLastName.get();
    }

    public String getrPassword() {
        return rPassword.get();
    }




    //Setters
    public void setrID(int rID) {
        this.rID.set(rID);
    }

    public void setrUsername(String rUsername) {
        this.rUsername.set(rUsername);
    }

    public void setrFirstName(String rFirstName) {
        this.rFirstName.set(rFirstName);
    }

    public void setrLastName(String rLastName) {
        this.rLastName.set(rLastName);
    }

    public void setrPassword(String rPassword) {
        this.rPassword.set(rPassword);
    }



    //Properties Might not be useful
    public SimpleIntegerProperty rIDProperty() {
        return rID;
    }

    public SimpleStringProperty rUsernameProperty() {
        return rUsername;
    }

    public SimpleStringProperty rFirstNameProperty() {
        return rFirstName;
    }

    public SimpleStringProperty rLastNameProperty() {
        return rLastName;
    }

    public SimpleStringProperty rPassword() {return rPassword;}



}
