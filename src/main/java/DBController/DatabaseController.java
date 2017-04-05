package DBController;
import controllers.MapController;
import controllers.Node;
import controllers.Edge;
import java.sql.*;
import java.util.ArrayList;

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

    public ResultSet getTableSet(String table){
        String sqlString = "Select * FROM " + table;

        ResultSet resultSet = null;
        try {
            Statement stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sqlString);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    // creates a new node in the database
    public boolean newNode(int x, int y, int floor, boolean ishidden, boolean enabled,
                           String type, String name, String roomnum){
        System.out.println(
                String.format(
                        "Adding node. x: %s, y: %s, hidden: %s, name: %s, floor: %s",
                            x, y, ishidden, name, floor));
        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM)" +
                    " values (?, ?, ?, ?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, floor);
            preparedStatement.setBoolean(4, ishidden);
            preparedStatement.setBoolean(5, enabled);
            preparedStatement.setString(6, type);
            preparedStatement.setString(7, name);
            preparedStatement.setString(8, roomnum);
            // execute prepared statement
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // finds the node with the given info and edits it
    // first deletes the nodes, then creates another one with the given information
    public boolean EditNode(int x, int y, int floor, boolean ishidden, boolean enabled,
                         String type, String name, String roomnum){
        // first we delete the node, because we don't want to change its primary keys
        if(!deleteNode(x, y, floor)){
            return false;
        }
        // then we create a new node with the old's one info
        if(!newNode(x, y, floor, ishidden, enabled, type, name, roomnum)){
            return false;
        }

        return true;
    }

    public ResultSet getNode(int x, int y, int floor){
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM NODE WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, floor);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public boolean closeResultSet(ResultSet resultSet){
        try{
            resultSet.close();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //delete node given its position
    public boolean deleteNode(int x, int y, int floor){
        try {
            // SQL statement with "?" to be filled later
            String query = "DELETE FROM NODE WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            // prepare statement by replacing each "?" with a variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, floor);
            // run statement and query
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // creates a new edge in the database
    public boolean newEdge(int x1, int y1, int floor1, int x2, int y2, int floor2){
        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2)" +
                    " values (?, ?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, x1);
            preparedStatement.setInt(2, y1);
            preparedStatement.setInt(3, floor1);
            preparedStatement.setInt(4, x2);
            preparedStatement.setInt(5, y2);
            preparedStatement.setInt(6, floor2);
            // execute prepared statement
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //delete edge between the two given node positions
    public boolean deleteEdge(int x1, int y1, int floor1, int x2, int y2, int floor2) {
        try {
            // SQL statement with "?" to be filled later
            String sqlString = "DELETE FROM EDGE WHERE XPOS1 = ? AND YPOS1 = ?" +
                    "AND FLOOR1 = ? AND XPOS2 = ? AND YPOS2 = ? AND FLOOR2 = ?";
            // prepare statement by replacing each "?" with a variable
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setInt(1, x1);
            preparedStatement.setInt(2, y1);
            preparedStatement.setInt(3, floor1);
            preparedStatement.setInt(4, x2);
            preparedStatement.setInt(5, y2);
            preparedStatement.setInt(6, floor2);
            // run statement and query
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean newProfessional(String ID, int x, int y, int floor,
                                   String firstName, String lastName, String type){
        try{
            // sql statement with "?" to be filled later
            String query = "INSERT INTO PROFESSIONAL (ID, XPOS, YPOS, FLOOR, FIRSTNAME, LASTNAME, TYPE)" +
                    " values (?, ?, ?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, ID);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setInt(4, floor);
            preparedStatement.setString(5, firstName);
            preparedStatement.setString(6, lastName);
            preparedStatement.setString(7, type);
            // execute prepared statement
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // finds the node with the given info and edits it
    // first deletes the nodes, then creates another one with the given information
    public boolean EditProfessional(String ID, int x, int y, int floor,
                                    String firstName, String lastName, String type){
        // first we delete the node, because we don't want to change its primary keys
        if(!deleteProfessional(ID)){
            return false;
        }
        // then we create a new node with the old's one info
        if(!newProfessional(ID, x, y, floor, firstName, lastName, type)){
            return false;
        }

        return true;
    }



    public ResultSet getProfessional(String ID){
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM PROFESSIONAL WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, ID);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public ResultSet getProfessional(String firstName, String lastName){
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM PROFESSIONAL WHERE FIRSTNAME = ? AND LASTNAME = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public boolean deleteProfessional(String ID){
        try{
            String sqlString = "DELETE FROM PROFESSIONAL WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setString(1, ID);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
