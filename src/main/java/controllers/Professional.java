package controllers;

/**
 * Created by MZ on 4/4/17.
 */
public class Professional {

    private String firstName;
    private String lastName;
    private String type;
    private String professionalID;

    public Professional(String firstName, String lastName, String type, String professionalID){
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.professionalID = professionalID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfessionalID() {
        return professionalID;
    }

    public void setProfessionalID(String professionalID) {
        this.professionalID = professionalID;
    }




}
