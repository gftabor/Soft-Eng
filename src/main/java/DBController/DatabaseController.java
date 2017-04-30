package DBController;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import com.sun.org.apache.regexp.internal.RE;
import controllers.Admin;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.transform.Result;

public class DatabaseController {

    private static DatabaseController databaseController = new DatabaseController();
    public static DatabaseController getInstance() {
        return databaseController;
    }

    protected String dbName;
    protected String buildTablesPath = "/database/buildTables.sql";
    protected String populateSQLPath = "/database/mainDatabasePopulate.sql";
    protected String cleanSQLPath = "/database/dropTables.sql";
    protected Connection conn;
    Statement stmt;

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /*******************************************************************************
     * Starting the database, checking drivers
     *
     ******************************************************************************/

    public boolean startDB() {
        //check for database driver
        if (!checkDatabaseDriver()) {
            return false;
        }
        return getDatabaseConnection();
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

    public boolean getDatabaseConnection(){
        File file = new File(dbName);

        try {
            if (file.exists()) {
                System.out.println(dbName + " does exist");
                conn = DriverManager.getConnection("jdbc:derby:"+ dbName +";");
            }else{
                System.out.println(dbName + " does not exist, creating");
                conn = DriverManager.getConnection("jdbc:derby:"+ dbName +";create=true;");
                buildTables();
                if(dbName != "testDB"){
                    System.out.println("Populating Database");
                    populateDB();
                }
            }
        } catch (SQLException e) {
            System.out.println("Database connection failed.");
            e.printStackTrace();
            conn = null;
        }

        if(conn == null){
            System.out.println("THERE IS NO DATABASE CONNECTION");
        }
        return true;
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
                           String type, String name, String roomnum, int permissionLevel){
        System.out.println(
                String.format(
                        "Adding node. x: %s, y: %s, hidden: %s, name: %s, floor: %s, permissions: %s",
                            x, y, ishidden, name, floor, permissionLevel));

        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO NODE (XPOS, YPOS, FLOOR, ISHIDDEN, ENABLED, TYPE, NAME, ROOMNUM, PERMISSIONS)" +
                    " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            preparedStatement.setInt(9, permissionLevel);
            // execute prepared statement
            
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    // finds the node with the given info and edits it
//    // first deletes the nodes, then creates another one with the given information
//    public boolean EditNode(int x, int y, int floor, boolean ishidden, boolean enabled,
//                         String type, String name, String roomnum){
//        // first we delete the node, because we don't want to change its primary keys
//        if(!deleteNode(x, y, floor)){
//            return false;
//        }
//        // then we create a new node with the old's one info
//        if(!newNode(x, y, floor, ishidden, enabled, type, name, roomnum)){
//            return false;
//        }
//
//        return true;
//    }

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

    public boolean transferNodeLoc(int x, int y, int floor, int dest_x, int dest_y, int dest_floor){
        System.out.println("UPDATE NODE DEPENDENCIES");
        //update PROLOCATION
        try {
            // SQL statement with "?" to be filled later
            String query = "UPDATE PROLOCATION SET XPOS = ? , YPOS = ? , FLOOR = ? " +
                    "WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            // prepare statement by replacing each "?" with a variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, dest_x);
            preparedStatement.setInt(2, dest_y);
            preparedStatement.setInt(3, dest_floor);
            preparedStatement.setInt(4, x);
            preparedStatement.setInt(5, y);
            preparedStatement.setInt(6, floor);
            // run statement and query

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        //update SERVICE
        try {
            // SQL statement with "?" to be filled later
            String query = "UPDATE SERVICE SET XPOS = ? , YPOS = ? , FLOOR = ? " +
                    "WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            // prepare statement by replacing each "?" with a variable
            PreparedStatement preparedStatement1 = conn.prepareStatement(query);
            preparedStatement1.setInt(1, dest_x);
            preparedStatement1.setInt(2, dest_y);
            preparedStatement1.setInt(3, dest_floor);
            preparedStatement1.setInt(4, x);
            preparedStatement1.setInt(5, y);
            preparedStatement1.setInt(6, floor);
            // run statement and query

            preparedStatement1.execute();
            preparedStatement1.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("updated node location dependencies successfully");
        return true;
    }

    //update node info without FeelsBadMan issues
    public boolean updateNode(int pk_x, int pk_y, int pk_floor, boolean ishidden, boolean enabled,
                              String type, String name, String roomnum, int permissionLevel) {
        try {
            // sql statement with "?" to be filled later
            String query = "UPDATE NODE SET ISHIDDEN = ? , ENABLED = ? , TYPE = ? , NAME = ? , ROOMNUM = ? , PERMISSIONS = ?" +
                    " WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setBoolean(1, ishidden);
            preparedStatement.setBoolean(2, enabled);
            preparedStatement.setString(3, type);
            preparedStatement.setString(4, name);
            preparedStatement.setString(5, roomnum);
            preparedStatement.setInt(6, permissionLevel);

            preparedStatement.setInt(7, pk_x);
            preparedStatement.setInt(8, pk_y);
            preparedStatement.setInt(9, pk_floor);
            //execute prepared statement

            preparedStatement.execute();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // gets node with given room name (room names are unique)
    public ResultSet getNodeWithName(String roomName){
        ResultSet resultSet = null;
        System.out.println("Getting node. room name: " + roomName);
        try{
            String query = "SELECT * FROM NODE WHERE ROOMNUM = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, roomName);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    // checks if this location is actually a node in the database
        // and maybe not just a circle
    public boolean isActualLocation(int x, int y, int floor){
        ResultSet rset;
        int thisX, thisY, thisFloor;
        try {
            // sql statement with "?" to be filled later
            String query = "SELECT XPOS, YPOS, FLOOR FROM NODE WHERE XPOS = ? AND YPOS = ? AND FLOOR = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, floor);

            //execute prepared statement
            rset = preparedStatement.executeQuery();

            while (rset.next()) {
                thisX = rset.getInt("XPOS");
                thisY = rset.getInt("YPOS");
                thisFloor = rset.getInt("FLOOR");
                if (x == thisX && y == thisY && floor == thisFloor){
                    return true;
                }
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public ArrayList<String> getNodeTypes(){
        ResultSet resultSet = null;
        String type;
        ArrayList<String> types = new ArrayList<>();
        System.out.println(
                String.format(
                        "Getting all node types as a list"));
        try{
            String query = "SELECT TYPE FROM NODE";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                type = resultSet.getString("TYPE");
                if (!types.contains(type)) {
                    types.add(type);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return types;
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

        System.out.println("x1: " + x1);
        System.out.println("x2: " + x2);
        System.out.println("y1: " + y1);
        System.out.println("y2: " + y2);
        System.out.println("f1: " + floor1);
        System.out.println("f2: " + floor2);


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
            }else if(y1 == y2 && floor1 != floor2) {
                firstXInsert = x1;
                firstYInsert = y1;
                firstFloorInsert = floor1;
                secondXInsert = x2;
                secondYInsert = y2;
                secondFloorInsert = floor2;
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
        System.out.println("delete from db was successfull");
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

    public boolean deleteProfessional(int id){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Deleting professional. id: %d",
                        id));
        try{
            String query = "DELETE FROM PROFESSIONAL WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
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
                        "Editing professional. id %d firstName: %s, lastName: %s, type: %s",
                        ID, firstName, lastName, type));
        try{
            String query = "UPDATE PROFESSIONAL SET FIRSTNAME = ?, LASTNAME = ?, TYPE = ?" +
                    "WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, type);
            preparedStatement.setInt(4, ID);
            // run statement and query
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ResultSet getProfessional(int id){
        System.out.println("Getting professional with ID" + id);
        ResultSet resultSet = null;

        try{
            String query = "SELECT * FROM PROFESSIONAL WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return resultSet;

    }

    public ResultSet getProsWithoutRooms(){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting all professionals"));
        try{
            String query = "SELECT ID, FIRSTNAME, LASTNAME, TYPE FROM PROFESSIONAL";
            PreparedStatement preparedStatement2 = conn.prepareStatement(query);
            // run statement and query
            resultSet = preparedStatement2.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    //returns spanish type list
    public ArrayList<String> getTitles(){
        ResultSet resultSet = null;
        String title;
        ArrayList<String> titles = new ArrayList<>();
        System.out.println(
                String.format(
                        "Getting all professional titles as a list"));
        try{
            String query = "SELECT TYPE FROM PROFESSIONAL";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                title = resultSet.getString("TYPE");
                if (!titles.contains(title)) {
                    titles.add(title);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return titles;
    }

    public ArrayList<String> getProfessionalList(){

        System.out.println("Getting all professionals, appending roomnum");

        ResultSet resultSet = null;
        ArrayList<String> professionals = new ArrayList<>();
        String professional = "";

        try{
            String query = "SELECT P.FIRSTNAME, P.LASTNAME, N.ROOMNUM " +
                    "FROM PROFESSIONAL P, NODE N, PROLOCATION PL " +
                    "WHERE P.ID = PL.PROID AND PL.XPOS = N.XPOS AND PL.YPOS = N.YPOS AND " +
                    "PL.FLOOR = N.FLOOR";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                professional = resultSet.getString("FIRSTNAME") + " " +
                        resultSet.getString("LASTNAME") + ", " +
                        resultSet.getString("ROOMNUM");
                //System.out.println("Entire professional string: " + professional);
                if (!professionals.contains(professional)) {
                    professionals.add(professional);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return professionals;

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

    public boolean EditProfessionalLocation(int PROID, int x, int y, int floor){
        System.out.println(
                String.format(
                        "Editing professional location. id %d x: %d, y: %d, floor: %d",
                        PROID, x, y, floor));
        try{
            String query = "UPDATE PROLOCATION SET XPOS = ?, YPOS = ?, FLOOR = ?" +
                    "WHERE PROID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, floor);
            preparedStatement.setInt(4, PROID);
            // run statement and query
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*******************************************************************************
     * ADMIN actions
     *
     ******************************************************************************/

    public boolean newAdmin(String firstName, String lastName, String userName,
                            String password, Boolean isAdmin, String faceId){
        String encrypted = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(
                String.format(
                        "Adding Admin. firstName: %s, lastName: %s, userName: %s, password: REDACTED",
                        firstName, lastName, userName));
        try {
            // sql statement with "?" to be filled later
            String query = "INSERT INTO ADMIN (FIRSTNAME, LASTNAME, USERNAME, PASSWORD, PERMISSIONS)" +
                    " values (?, ?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, userName);
            preparedStatement.setString(4, encrypted);
            preparedStatement.setString(5, faceId);
            if(isAdmin) {
                preparedStatement.setInt(5, 2);
            }else{
                preparedStatement.setInt(5, 1);
            }
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
    public int getPermission(String username){
        ResultSet resultSet = null;
        int permissions;
        try{
            String query = "SELECT PERMISSIONS FROM ADMIN WHERE USERNAME = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);
            // run statement and query

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
        try {
            if(!resultSet.next()){
                return 0;
            }
            permissions = resultSet.getInt("PERMISSIONS");
            closeResultSet(resultSet);
            return permissions;

        } catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
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

    public boolean editAdmin(int ID, String firstName, String lastName, String userName, Boolean isAdmin){


        System.out.println(
                String.format(
                        "Editing Admin. ID: %d, firstName: %s, lastName: %s, userName: %s",
                        ID, firstName, lastName, userName));
        try {
            // sql statement with "?" to be filled later
            String query = "UPDATE ADMIN SET FIRSTNAME = ?, LASTNAME = ?, USERNAME = ?, PERMISSIONS = ? WHERE ID = ?";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, userName);

            if(isAdmin) {
                preparedStatement.setInt(4, 2);
            }else{
                preparedStatement.setInt(4, 1);
            }
            preparedStatement.setInt(5, ID);
            // execute prepared statement

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean editAdminPassword(int ID, String password){
        String encrypted = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(
                String.format(
                        "Editing Admin Password: ID: %d", ID
                ));
        try {
            // sql statement with "?" to be filled later
            String query = "UPDATE ADMIN SET PASSWORD = ? WHERE ID = ?";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, encrypted);
            preparedStatement.setInt(2, ID);
            // execute prepared statement

            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

    public ArrayList<Admin> getListOfAdmins(){
        ArrayList<Admin> admins = new ArrayList<>();
        int id = 99999, permissions = 0;
        String firstName = "", lastName = "", userName = "", password = "", faceId = "";

        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting all admins"));
        try{
            String query = "SELECT * FROM ADMIN";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                id = resultSet.getInt("ID");
                firstName = resultSet.getString("FIRSTNAME");
                lastName = resultSet.getString("LASTNAME");
                userName = resultSet.getString("USERNAME");
                password = resultSet.getString("PASSWORD");
                permissions = resultSet.getInt("PERMISSIONS");
                faceId = resultSet.getString("FACE_ID");

                admins.add(new Admin(id, firstName, lastName, userName, password, faceId, permissions));
            }
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return admins;
    }

    /*******************************************************************************
     * SQL GENERATION actions
     *
     ******************************************************************************/

    public boolean buildTables(){
        return readSQL(buildTablesPath);
    }

    public boolean populateDB(){
        return readSQL(populateSQLPath);
    }

    public boolean clearDB(){
        return readSQL(cleanSQLPath);
    }

    private boolean readSQL(String path){
        System.out.println("Database being generated from SQL...");
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try {
            InputStream file = getClass().getResourceAsStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));


            while ((s = reader.readLine()) != null) {
                sb.append(s);
            }
            reader.close();
            file.close();

            String[] inst = sb.toString().split(";");

            stmt = conn.createStatement();

            for (int i = 0; i < inst.length; i++) {
                if (!inst[i].trim().equals("")) {
                    stmt.executeUpdate(inst[i]);
                    //System.out.println(">>" + inst[i]);
                }
            }
            stmt.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /*******************************************************************************
     * ADMIN actions
     *
     ******************************************************************************/

    public boolean newService(String name, String type, int x, int y, int floor){
        System.out.println(
                String.format(
                        "Adding service. name: %s, type: %s, x: %d, y: %d, floor: %d",
                        name, type, x, y, floor));
        try{
            // sql statement with "?" to be filled later
            String query = "INSERT INTO SERVICE (NAME, TYPE, XPOS, YPOS, FLOOR)" +
                    " values (?, ?, ?, ?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, x);
            preparedStatement.setInt(4, y);
            preparedStatement.setInt(5, floor);
            // execute prepared statement
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ResultSet getService(String name, String type, int x, int y, int floor){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting service. name: %s, type: %s, x: %d, y: %d, floor: %d",
                        name, type, x, y, floor));
        try{
            String query = "SELECT * FROM SERVICE WHERE NAME = ? AND TYPE = ? AND XPOS = ?" +
                    "AND YPOS = ? AND FLOOR = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, x);
            preparedStatement.setInt(4, y);
            preparedStatement.setInt(5, floor);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public boolean deleteService(String name, String type, int x, int y, int floor){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Deleting service. name: %s, type: %s, x: %s, y: %d, floor: %d",
                        name, type, x, y, floor));
        try{
            String query = "DELETE FROM SERVICE WHERE NAME = ? AND TYPE = ?" +
                    "AND XPOS = ? AND YPOS = ? AND FLOOR = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, type);
            preparedStatement.setInt(3, x);
            preparedStatement.setInt(4, y);
            preparedStatement.setInt(5, floor);
            // run statement and query
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ResultSet getRoomNames(){
        System.out.println("Getting room names");

        ResultSet resultSet = null;
        try{
            String query = "SELECT NAME, ROOMNUM FROM NODE";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            //preparedStatement.setString(1, "%"+roomName);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public ResultSet getFilteredRoomNames(int permissionLevel){
        System.out.println("Getting room names");

        ResultSet resultSet = null;
        try{
            String query = "SELECT NAME, ROOMNUM FROM NODE " +
                    "WHERE ISHIDDEN = FALSE AND TYPE <> 'Stair' AND TYPE <> 'Elevator'" +
                    "AND PERMISSIONS <= " + permissionLevel;
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            //preparedStatement.setString(1, "%"+roomName);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    /*******************************************************************************
     * DECODELANGUAGE
     *
     ******************************************************************************/
    public String getSpanish(String english){
        String spanish;
        System.out.println("Finding spanish correspondent for " + english);

        ResultSet resultSet = null;
        try{
            String query = "SELECT SPANISH FROM DECODELANGUAGE WHERE ENGLISH = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, english);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            spanish = resultSet.getString("SPANISH");
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return spanish;

    }

    public String getEnglish(String spanish){
        String english;
        System.out.println("Finding english correspondent for " + spanish);

        ResultSet resultSet = null;
        try{
            String query = "SELECT ENGLISH FROM DECODELANGUAGE WHERE SPANISH = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, spanish);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            english = resultSet.getString("ENGLISH");
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }

        return english;
    }

    public boolean addInTranslation(String english, String spanish){
        System.out.println(
                String.format(
                        "Adding translation. english: %s, spanish: %s",
                        english, spanish));
        try{
            // sql statement with "?" to be filled later
            String query = "INSERT INTO DECODELANGUAGE (ENGLISH, SPANISH)" +
                    " values (?, ?)";
            // prepare statement by replacing "?" with corresponding variable
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, english);
            preparedStatement.setString(2, spanish);
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
     * Combinations
     *
     ******************************************************************************/

    public ResultSet getProRoomNums(){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting all professional room numbers"));
        try{
            String query = "SELECT P.ID, P.FIRSTNAME, P.LASTNAME, P.TYPE, N.ROOMNUM FROM PROFESSIONAL P, PROLOCATION PL, NODE N WHERE " +
                    "PL.PROID = P.ID AND N.XPOS = PL.XPOS AND N.YPOS = PL.YPOS AND " +
                    "N.FLOOR = PL.FLOOR";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public ResultSet getDepartmentNames(){
        ResultSet resultSet = null;
        System.out.println(
                String.format(
                        "Getting all professional english departments"));
        try{
            String query = "SELECT DEPARTMENT FROM PROFESSIONAL";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

//    public ResultSet getSpanishDepartmentNames(){
//        ResultSet resultSet = null;
//        System.out.println(
//                String.format(
//                        "Getting all professional spanish departments"));
//        try{
//            String query = "SELECT SPDEPARTMENT FROM PROFESSIONAL";
//            PreparedStatement preparedStatement = conn.prepareStatement(query);
//            // run statement and query
//            resultSet = preparedStatement.executeQuery();
//        } catch (SQLException e){
//            e.printStackTrace();
//            return null;
//        }
//        return resultSet;
//    }

    public ResultSet getPosForRoom(String room){
        ResultSet resultSet = null;

        System.out.println("Getting position for room num: "+ room);
        try{
            String query = "SELECT XPOS, YPOS, FLOOR FROM NODE WHERE ROOMNUM = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, room);
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return resultSet;
    }

    public ArrayList<String> getRooms(){
        ArrayList<String> rooms = new ArrayList<>();
        String roomNum;
        ResultSet rset = databaseController.getRoomNames();
        try {
            while (rset.next()) {
                roomNum = rset.getString("ROOMNUM");
                if (!rooms.contains(roomNum)) {
                    rooms.add(roomNum);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public ArrayList<String> getFilteredRooms(int permissionLevel){
        ArrayList<String> rooms = new ArrayList<>();
        String roomNum;
        ResultSet rset = databaseController.getFilteredRoomNames(permissionLevel);
        try {
            while (rset.next()) {
                roomNum = rset.getString("ROOMNUM");
                if (!rooms.contains(roomNum)) {
                    rooms.add(roomNum);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public ArrayList<String> getRoomList() {
        ArrayList<String> rooms = new ArrayList<>();
        String roomName, roomNum;
        String room;
        ResultSet rset = databaseController.getRoomNames();
        try {
            while (rset.next()) {
                roomName = rset.getString("NAME");
                roomNum = rset.getString("ROOMNUM");
                if (!rooms.contains(roomNum)) {
                    room = "" + roomName + ", " + roomNum;
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public ArrayList<String> getFilteredRoomList(int permissionLevel) {
        ArrayList<String> rooms = new ArrayList<>();
        String roomName, roomNum;
        String room;
        ResultSet rset = databaseController.getFilteredRoomNames(permissionLevel);
        try {
            while (rset.next()) {
                roomName = rset.getString("NAME");
                roomNum = rset.getString("ROOMNUM");
                if (!rooms.contains(roomNum)) {
                    room = "" + roomName + ", " + roomNum;
                    rooms.add(room);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }
    /*******************************************************************************
     * Misc.
     *
     ******************************************************************************/

    public int GetKioskFloor(){
        ResultSet resultSet;

        System.out.println("Getting kiosk floor.");
        try{
            String query = "SELECT * FROM NODE WHERE NAME = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, "Kiosk");
            // run statement and query
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }

        try {
            while(resultSet.next()){
                System.out.println("Kiosk Floor: " + resultSet.getString("FLOOR"));
                return resultSet.getInt("FLOOR");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
