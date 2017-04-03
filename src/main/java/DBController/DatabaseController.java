package DBController;
import controllers.Node;

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
        }
    }

    public ArrayList<Node> getNodesInFloor(int floor){
        String sqlString = "Select XPOS, YPOS, `HIDDEN?`, NAME FROM NODE WHERE FLOOR = " + floor;
        ArrayList nodes = new ArrayList();
        int xPos;
        int yPos;
        String hiddenString;
        boolean hidden;
        String name;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(sqlString);
            while (rset.next()){
                xPos = rset.getInt("XPOS");
                yPos = rset.getInt("YPOS");
                hiddenString = rset.getString("HIDDEN?");
                hidden = hiddenString.equals("Y");
                name = rset.getString("name");
                nodes.add(new Node(xPos, yPos, hidden, name, floor));
            }
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return nodes;
    }

    // creates a new node in the database
    public void newNode(int x, int y, boolean hidden, String name,int floor){
        String hiddenString;
        if (hidden) {
            hiddenString = "Y";
        } else {
            hiddenString = "N";
        }
        try {
            String query = "INSERT INTO NODE (YPOS, XPOS, NAME, FLOOR, `HIDDEN?`)" +
                    " values (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, y);
            preparedStatement.setInt(2, x);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, floor);
            preparedStatement.setString(5, hiddenString);
            Statement stmt = conn.createStatement();
            preparedStatement.execute(query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // creates a new edge in the database
    public void newEdge(int xPos1, int yPos1, int floor1, int xPos2, int yPos2, int floor2){
        try {
            String query = "INSERT INTO EDGE (XPOS1, YPOS1, XPOS2, YPOS2, FLOOR1, FLOOR2)" +
                    " values (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, xPos1);
            preparedStatement.setInt(2, yPos1);
            preparedStatement.setInt(3, xPos2);
            preparedStatement.setInt(4, yPos2);
            preparedStatement.setInt(5, floor1);
            preparedStatement.setInt(6, floor2);
            Statement stmt = conn.createStatement();
            preparedStatement.execute(query);
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
            String sqlString = "DELETE FROM EDGE WHERE XPOS1 = ? AND YPOS1 = ?" +
                    "AND FLOOR1 = ? AND XPOS2 = ? AND YPOS2 = ? AND FLOOR2 = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setInt(1, xPos1);
            preparedStatement.setInt(2, yPos1);
            preparedStatement.setInt(3, floor1);
            preparedStatement.setInt(4, xPos2);
            preparedStatement.setInt(5, yPos2);
            preparedStatement.setInt(6, floor2);
            Statement stmt = conn.createStatement();
            preparedStatement.execute(sqlString);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //delete node given its position
    public void deleteNode(int xPos, int yPos, int floor){
        try {
            String query = "DELETE FROM NODE WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, xPos);
            preparedStatement.setInt(2, yPos);
            preparedStatement.setInt(3, floor);
            Statement stmt = conn.createStatement();
            preparedStatement.execute(query);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
