package DBController;
import controllers.MapController;
import controllers.Node;
import controllers.Edge;
import java.sql.*;
import java.util.ArrayList;


/**
 * Created by MZ on 4/2/17.
 */
public class DatabaseController {

    private static DatabaseController databaseController = new DatabaseController();

    Connection conn;


    private DatabaseController() {}

    public static DatabaseController getInstance() {
        return databaseController;
    }

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

    public boolean closeDB(){
        try{
            conn.close();
            return true;
        } catch (SQLException e){
            System.out.println("Unable to close database");
            e.printStackTrace();
            return false;
        }

    }

    /*
=======
>>>>>>> .merge_file_a07212
    public ArrayList<Node> getNodesInFloor(int floor){
        String sqlString = "Select XPOS, YPOS, ISHIDDEN, NAME FROM NODE WHERE FLOOR = " + floor;
        ArrayList<Node> nodes = new ArrayList<>();
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
                hiddenString = rset.getString("HIDDEN");
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
    */

    public ResultSet getNodeSet(){
        String sqlString = "Select * FROM NODE";

        ResultSet rset = null;
        try {
            Statement stmt = conn.createStatement();
            rset = stmt.executeQuery(sqlString);
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rset;
    }


    public ResultSet getEdgeSet(){
        String sqlString = "Select * FROM EDGE";

        ResultSet rset = null;
        try {
            Statement stmt = conn.createStatement();
            rset = stmt.executeQuery(sqlString);
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rset;
    }

    // creates a new node in the database
    public void newNode(int x, int y, boolean hidden, String name,int floor){
        System.out.println("Adding node");
        String hiddenString;
        if (hidden) {
            hiddenString = "Y";
        } else {
            hiddenString = "N";
        }
        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO NODE (YPOS, XPOS, NAME, FLOOR, ISHIDDEN)" +
                    " values (?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, y);
            preparedStatement.setInt(2, x);
            preparedStatement.setString(3, name);
            preparedStatement.setInt(4, floor);
            preparedStatement.setString(5, hiddenString);
            // execute prepared statement
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Node added");
    }

    // creates a new edge in the database
    public void newEdge(int xPos1, int yPos1, int floor1, int xPos2, int yPos2, int floor2){
        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO EDGE (XPOS1, YPOS1, XPOS2, YPOS2, FLOOR1, FLOOR2)" +
                    " values (?, ?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, xPos1);
            preparedStatement.setInt(2, yPos1);
            preparedStatement.setInt(3, xPos2);
            preparedStatement.setInt(4, yPos2);
            preparedStatement.setInt(5, floor1);
            preparedStatement.setInt(6, floor2);
            // execute prepared statement
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // finds the node with the given info and edits it
    // first deletes the nodes, then creates another one with the given information
    public void EditNode(int yPos, int xPos, int floor, boolean hidden, String name){
        // first we delete the node, because we don't want to change its primary keys
        deleteNode(xPos, yPos, floor);
        // then we create a new node with the old's one info
        newNode(xPos, yPos, hidden, name, floor);
    }

    //delete edge between the two given node positions
    public void deleteEdge(int xPos1, int yPos1, int floor1, int xPos2, int yPos2, int floor2) {
        try {
            // SQL statement with "?" to be filled later
            String sqlString = "DELETE FROM EDGE WHERE XPOS1 = ? AND YPOS1 = ?" +
                    "AND FLOOR1 = ? AND XPOS2 = ? AND YPOS2 = ? AND FLOOR2 = ?";
            // prepare statement by replacing each "?" with a variable
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setInt(1, xPos1);
            preparedStatement.setInt(2, yPos1);
            preparedStatement.setInt(3, floor1);
            preparedStatement.setInt(4, xPos2);
            preparedStatement.setInt(5, yPos2);
            preparedStatement.setInt(6, floor2);
            // run statement and query
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //delete node given its position
    public void deleteNode(int xPos, int yPos, int floor){
        try {
            // SQL statement with "?" to be filled later
            String query = "DELETE FROM NODE WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            // prepare statement by replacing each "?" with a variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, xPos);
            preparedStatement.setInt(2, yPos);
            preparedStatement.setInt(3, floor);
            // run statement and query
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
