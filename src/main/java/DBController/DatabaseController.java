package DBController;
import controllers.Node;
import controllers.Edge;
import java.sql.*;
import java.util.ArrayList;


/**
 * Created by MZ on 4/2/17.
 */
public class DatabaseController {

    Connection conn;

    public void startDB() {
        System.out.println("-------- Embedded Java DB Connection Testing ------");
        System.out.println("-------- Step 1: Registering Driver ------");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Driver? Did you follow the execution steps. ");
            System.out.println("");
            System.out.println("");
            e.printStackTrace();
            return;
        }

        System.out.println("Driver Registered Successfully !");

        System.out.println("-------- Step 2: Building a Connection ------");

        try {
            conn = DriverManager.getConnection("jdbc:derby:FaulknerDB");
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return;
        }
        if (conn != null) {
            System.out.println("You made it. Connection is successful. Take control of your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
        try {

            String sql = "SELECT * FROM DOCTOR";
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            while (rset.next()) {
                System.out.println("Doctor ID: " + rset.getInt("docID"));
                System.out.println("First Name: " + rset.getString("firstName"));
                System.out.println("Last Name: " + rset.getString("lastName"));
            }
            System.out.println("----------------------------------------------");
            rset.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Node> getNodesInFloor(int floor){
        String sqlString = "Select XPOS, YPOS, `HIDDEN?`, NAME FROM NODE WHERE FLOOR = " + floor;
        ArrayList nodes = new ArrayList();
        int xPos;
        int yPos;
        int nodeFloor;
        String hiddenString;
        boolean hidden = false;
        String name;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sqlString);
            while (rset.next()){
                xPos = rset.getInt("XPOS");
                yPos = rset.getInt("YPOS");
                hiddenString = rset.getString("HIDDEN?");
                if (hiddenString.equals('Y')){
                    hidden = true;
                } else {
                    hidden = false;
                }
                name = rset.getString("name");
                nodes.add(new Node(xPos, yPos, hidden, name, floor));
            }
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nodes;
    }

    // creates a new node in the database
    public void newNode(int x, int y, boolean hidden, String name,int floor){
        String hiddenString;
        if (hidden == true) {
            hiddenString = "Y";
        } else {
            hiddenString = "N";
        }
        try {
            String sqlString = "INSERT INTO NODE VALUES (y, x, name, floor, hiddenString)";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlString);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // creates a new edge in the database
    public void newEdge(int xPos1, int yPos1, int floor1, int xPos2, int yPos2, int floor2){
        try {
            String sqlString = "INSERT INTO EDGE VALUES (xPos1, yPos1, xPos2, yPos2, floor1, floor2)";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlString);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // finds the node with the given info and edits it
    public void EditNode(int yPos, int xPos, int floor, boolean hidden, String name){
        
    }

    //delete edge between the two given node positions
    public void deleteEdge(int xPos1, int yPos1, int floor1, int xPos2, int yPos2, int floor2) {
        try {
            String sqlString = "DELETE FROM EDGE WHERE XPOS1 = " + xPos1 +
                    "AND YPOS1 = " + yPos1 + "AND FLOOR1 = " + floor1 + "AND XPOS2 = "
                    + xPos2 + "AND YPOS2 = " + yPos2 + "AND FLOOR2 = " + floor2;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlString);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //delete node given its position
    public void deleteNode(int xPos, int yPos, int floor){
        try {
            String sqlString = "DELETE FROM NODE WHERE XPOS = " + xPos +
                    "AND YPOS= " + yPos + "AND FLOOR = " + floor;
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlString);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
