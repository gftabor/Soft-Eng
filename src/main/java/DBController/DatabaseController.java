package DBController;
import controllers.MapController;
import controllers.Node;
import controllers.Edge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.transform.Result;

public class DatabaseController {

    private static DatabaseController databaseController = new DatabaseController();
    protected String dbName;
    protected String populateSQLPath = "build/resources/main/database/testDatabaseSetup.sql";
    protected String cleanSQLPath = "build/resources/main/database/testDatabaseClear.sql";
    Connection conn;
    Statement stmt;

    public DatabaseController() {
        this.dbName = "FaulknerDB";
    }

    public static DatabaseController getInstance() {
        return databaseController;
    }


    /*******************************************************************************
     * Starting the database, checking drivers
     *
     ******************************************************************************/

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
            conn = DriverManager.getConnection("jdbc:derby:"+ dbName +";create=true;");
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

    /*******************************************************************************
     * getting tables, closing result sets
     *
     ******************************************************************************/

    public ResultSet getTableSet(String table){
        String sqlString = "Select * FROM " + table;

        ResultSet resultSet = null;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sqlString);
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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

    /*******************************************************************************
     * NODE actions
     *
     ******************************************************************************/

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

    //queries a particular node in the database
    //  input: node key information
    //  returns: ResultSet of node from the database query
    public ResultSet getNode(int x, int y, int floor){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting node. x: %s, y: %s, floor: %s",
                        x, y, floor));
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

    //delete node given its position
    public boolean deleteNode(int x, int y, int floor){
        System.out.println(
                String.format(
                        "Delete node. x: %s, y: %s, floor: %s",
                        x, y, floor));
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

    //update node info without FeelsBadMan issues
    public boolean updateNode(int pk_x, int pk_y, int pk_floor, boolean ishidden, boolean enabled,
                              String type, String name, String roomnum) {
        try {
            // sql statement with "?" to be filled later
            String query = "UPDATE NODE SET ISHIDDEN = ? , ENABLED = ? , TYPE = ? , NAME = ? , ROOMNUM = ?" +
                    " WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, ishidden);
            preparedStatement.setBoolean(2, enabled);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, roomnum);

            preparedStatement.setInt(6, pk_x);
            preparedStatement.setInt(7, pk_y);
            preparedStatement.setInt(8, pk_floor);
            //execute prepared statement

            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*******************************************************************************
     * EDGE actions
     *
     ******************************************************************************/

    /*
    To solve two way edges, the database will always query from x1 < x2 and y1 < y2
     */
    public boolean newEdge(int x1, int y1, int floor1, int x2, int y2, int floor2){
        int firstXInsert, firstYInsert, firstFloorInsert, secondXInsert, secondYInsert, secondFloorInsert;

        if(x1 < x2){
            firstXInsert = x1;
            firstYInsert = y1;
            firstFloorInsert = floor1;
            secondXInsert = x2;
            secondYInsert = y2;
            secondFloorInsert = floor2;
        }else if (x1 > x2){
            firstXInsert = x2;
            firstYInsert = y2;
            firstFloorInsert = floor2;
            secondXInsert = x1;
            secondYInsert = y1;
            secondFloorInsert = floor1;
        }else if (x1 == x2){
            if(y1 < y2){
                firstXInsert = x1;
                firstYInsert = y1;
                firstFloorInsert = floor1;
                secondXInsert = x2;
                secondYInsert = y2;
                secondFloorInsert = floor2;
            }else if (y1 > y2) {
                firstXInsert = x2;
                firstYInsert = y2;
                firstFloorInsert = floor2;
                secondXInsert = x1;
                secondYInsert = y1;
                secondFloorInsert = floor1;
            }else{
                System.out.println("Something went wrong with newEdge");
                return false;
            }
        }else{
            System.out.println("Something went wrong with newEdge");
            return false;
        }

        System.out.println(
                String.format(
                        "Adding edge. x1: %s, y1: %s, floor1: %s,\n x2: %s, y2: %s, floor2: %s",
                        firstXInsert, firstYInsert, firstFloorInsert, secondXInsert, secondYInsert, secondFloorInsert));
        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO EDGE (XPOS1, YPOS1, FLOOR1, XPOS2, YPOS2, FLOOR2)" +
                    " values (?, ?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, firstXInsert);
            preparedStatement.setInt(2, firstYInsert);
            preparedStatement.setInt(3, firstFloorInsert);
            preparedStatement.setInt(4, secondXInsert);
            preparedStatement.setInt(5, secondYInsert);
            preparedStatement.setInt(6, secondFloorInsert);
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
        int firstXInsert, firstYInsert, firstFloorInsert, secondXInsert, secondYInsert, secondFloorInsert;

        if(x1 < x2){
            firstXInsert = x1;
            firstYInsert = y1;
            firstFloorInsert = floor1;
            secondXInsert = x2;
            secondYInsert = y2;
            secondFloorInsert = floor2;
        }else if (x1 > x2){
            firstXInsert = x2;
            firstYInsert = y2;
            firstFloorInsert = floor2;
            secondXInsert = x1;
            secondYInsert = y1;
            secondFloorInsert = floor1;
        }else if (x1 == x2){
            if(y1 < y2){
                firstXInsert = x1;
                firstYInsert = y1;
                firstFloorInsert = floor1;
                secondXInsert = x2;
                secondYInsert = y2;
                secondFloorInsert = floor2;
            }else if (y1 > y2) {
                firstXInsert = x2;
                firstYInsert = y2;
                firstFloorInsert = floor2;
                secondXInsert = x1;
                secondYInsert = y1;
                secondFloorInsert = floor1;
            }else{
                System.out.println("Something went wrong with deleteEdge");
                return false;
            }
        }else{
            System.out.println("Something went wrong with deleteEdge");
            return false;
        }
        System.out.println(
                String.format(
                        "Delete edge. x1: %s, y1: %s, floor1: %s,\n x2: %s, y2: %s, floor2: %s",
                        firstXInsert, firstYInsert, firstFloorInsert, secondXInsert, secondYInsert, secondFloorInsert));
        try {
            // SQL statement with "?" to be filled later
            String sqlString = "DELETE FROM EDGE WHERE XPOS1 = ? AND YPOS1 = ?" +
                    "AND FLOOR1 = ? AND XPOS2 = ? AND YPOS2 = ? AND FLOOR2 = ?";
            // prepare statement by replacing each "?" with a variable
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setInt(1, firstXInsert);
            preparedStatement.setInt(2, firstYInsert);
            preparedStatement.setInt(3, firstFloorInsert);
            preparedStatement.setInt(4, secondXInsert);
            preparedStatement.setInt(5, secondYInsert);
            preparedStatement.setInt(6, secondFloorInsert);
            // run statement and query
            
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*******************************************************************************
     * PROFESSIONAL actions
     *
     ******************************************************************************/

    public boolean newProfessional(String firstName, String lastName, String type){
        System.out.println(
                String.format(
                        "Adding professional. firstName: %s, lastName: %s, type: %s",
                        firstName, lastName, type));
        try{
            // sql statement with "?" to be filled later
            String query = "INSERT INTO PROFESSIONAL (FIRSTNAME, LASTNAME, TYPE)" +
                    " values (?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, type);
            // execute prepared statement
            
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ResultSet getProfessional (String firstName, String lastName){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting professional. firstName: %s, lastName: %s",
                        firstName, lastName));
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
    public ResultSet getProfessional(String firstName, String lastName, String type){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting professional. firstName: %s, lastName: %s, type: %s",
                        firstName, lastName, type));
        try{
            String query = "SELECT * FROM PROFESSIONAL WHERE FIRSTNAME = ? AND LASTNAME = ? AND TYPE = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, type);
            // run statement and query
            
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public boolean deleteProfessional(String firstName, String lastName, String type){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Deleting professional. firstName: %s, lastName: %s, type: %s",
                        firstName, lastName, type));
        try{
            String query = "DELETE FROM PROFESSIONAL WHERE FIRSTNAME = ? AND LASTNAME = ? AND TYPE = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, type);
            // run statement and query
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteProfessional(String ID){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Deleting professional. String %s", ID));
        try{
            String query = "DELETE FROM PROFESSIONAL WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, ID);
            // run statement and query
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean EditProfessional(int ID, String firstName, String lastName, String type){
        System.out.println(
                String.format(
                        "Editing professional. firstName: %s, lastName: %s, type: %s",
                        firstName, lastName, type));
        try{
            String query = "UPDATE PROFESSIONAL SET FIRSTNAME = ?, LASTNAME = ? AND TYPE = ?" +
                    "WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, type);
            preparedStatement.setInt(4, ID);
            // run statement and query
            preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /*******************************************************************************
     * PROFESSIONAL - LOCATION actions
     *
     ******************************************************************************/

    public boolean newProfessionalLocation(int PROID, int x, int y, int floor){
        System.out.println(
                String.format(
                        "Adding professional Location. ID: %s, x: %s, y: %s, floor: %s",
                        PROID, x, y, floor));
        try{
            // sql statement with "?" to be filled later
            String query = "INSERT INTO PROLOCATION (PROID, XPOS, YPOS, FLOOR)" +
                    " values (?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, PROID);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setInt(4, floor);
            // execute prepared statement

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ResultSet getProfessionalLocation(int PROID, int x, int y, int floor){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting professional Location. ID: %s, x: %s, y: %s, floor: %s",
                        PROID, x, y, floor));
        try{
            // sql statement with "?" to be filled later
            String query = "SELECT * FROM PROLOCATION WHERE PROID = ? AND XPOS = ? AND YPOS = ? AND FLOOR = ?";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, PROID);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setInt(4, floor);
            // execute prepared statement

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public boolean deleteProfessionalLocation(int PROID, int x, int y, int floor){
        System.out.println(
                String.format(
                        "Adding professional Location. ID: %s, x: %s, y: %s, floor: %s",
                        PROID, x, y, floor));
        try{
            // sql statement with "?" to be filled later
            String query = "DELETE FROM PROLOCATION WHERE PROID = ? AND XPOS = ? AND YPOS = ? AND FLOOR = ?";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, PROID);
            preparedStatement.setInt(2, x);
            preparedStatement.setInt(3, y);
            preparedStatement.setInt(4, floor);
            // execute prepared statement

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /*******************************************************************************
     * ADMIN actions
     *
     ******************************************************************************/

    public boolean newAdmin(int ID, String firstName, String lastName, String userName, String password){
        String encrypted = BCrypt.hashpw(password, BCrypt.gensalt());

        System.out.println(
                String.format(
                        "Adding Admin. ID: %s, firstName: %s, lastName: %s, userName: %s, password: REDACTED",
                        ID, firstName, lastName, userName));
        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO ADMIN (ID, FIRSTNAME, LASTNAME, USERNAME, PASSWORD)" +
                    " values (?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, ID);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, lastName);
            preparedStatement.setString(4, userName);
            preparedStatement.setString(5, encrypted);
            // execute prepared statement

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ResultSet getAdmin(String ID){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting admin. ID: %s",
                        ID));
        try{
            String query = "SELECT * FROM ADMIN WHERE ID = ?";
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

    public String getPassword(String username){
        ResultSet resultSet = null;
        String pw;
        System.out.println(
                String.format(
                        "Getting admin password. Username: %s",
                        username));
        try{
            String query = "SELECT * FROM ADMIN WHERE USERNAME = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            // run statement and query

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        try {
            if(!resultSet.next()){
                return null;
            }
            pw = resultSet.getString("PASSWORD");
            closeResultSet(resultSet);
            return pw;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAdmin(int ID){
        System.out.println(
                String.format("Removing admin id: ?", ID)
        );
        try{
            String sqlString = "DELETE FROM ADMIN WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlString);
            preparedStatement.setInt(1, ID);

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*******************************************************************************
     * SQL GENERATION actions
     *
     ******************************************************************************/

    public boolean populateDB(){
        return readSQL(populateSQLPath);
    }

    public boolean clearDB(){
        return readSQL(cleanSQLPath);
    }

    private boolean readSQL(String path){
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try {
            FileReader fr = new FileReader(new File(path));

            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();

            String[] inst = sb.toString().split(";");

            Statement stmt = conn.createStatement();

            for (int i = 0; i < inst.length; i++) {
                if (!inst[i].trim().equals("")) {
                    stmt.executeUpdate(inst[i]);
                    System.out.println(">>" + inst[i]);
                }
            }
            stmt.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
