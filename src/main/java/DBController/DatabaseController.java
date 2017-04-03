package DBController;
import controllers.Node;

import java.sql.*;
import java.util.ArrayList;


/**
 * Created by MZ on 4/2/17.
 */
public class DatabaseController {

    Connection conn;

    public boolean startDB() {

        //check for database driver
        if(!checkDatabaseDriver()){
            return false;
        }
        conn = getDatabaseConnection();

        if(conn == null){
            return false;
        }
        return true;
    }

    private boolean checkDatabaseDriver(){
        System.out.println("Registering DB Driver");
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            return true;
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
            e.printStackTrace();
            return false;
        }
    }

    private Connection getDatabaseConnection(){
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:derby:FaulknerDB");
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
            conn = null;
        }
        return conn;
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
