import java.sql.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

import controllers.Node;
import controllers.CollectionOfNodes;

public class Main extends Application {

    @Override

    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/views/patientMenuStartView.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/patientMenuStartView.fxml"));

        primaryStage.setTitle("Iteration 1 Minimal Application Correct");
        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 1274, 710));
        primaryStage.setResizable(false);
        primaryStage.show();

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
        Connection conn = null;
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
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM DOCTOR";
            String offices = "SELECT FIRSTNAME, LASTNAME, NODEFIELDID FROM DOCTOR, HASOFFICEIN " +
                    "WHERE DOCTOR.DOCID = HASOFFICEIN.DOCID GROUP BY FirstName, LastName, NODEFIELDID";
            // change the ID of doctor below to add a new doctor, then uncomment the next two lines
            //String insertSql = "INSERT INTO DOCTOR VALUES (5, 'Alex', 'Ibro')";
            //stmt.execute(insertSql);
            // print the columns for every doctor in the database

            ResultSet rset = stmt.executeQuery(sql);
            while (rset.next()) {
                System.out.println("Doctor ID: " + rset.getInt("docID"));
                System.out.println("First Name: " + rset.getString("firstName"));
                System.out.println("Last Name: " + rset.getString("lastName"));
            }
            System.out.println("----------------------------------------------");
            ResultSet rset2 = stmt.executeQuery(offices);
            while (rset2.next()) {
                System.out.println("First Name: " + rset2.getString("firstName"));
                System.out.println("Last Name: " + rset2.getString("lastName"));
                System.out.println("Office Node: " + rset2.getInt("NODEFIELDID"));
            }
            rset.close();
            rset2.close();
            stmt.close();

            System.out.println("-------------------------");
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Statement stmt = conn.createStatement();
            String nodeSql = "SELECT XPOS, YPOS FROM NODE";

            ResultSet nodeRset = stmt.executeQuery(nodeSql);
            int x, y;
            Node node;
            CollectionOfNodes collectionOfNodes = new CollectionOfNodes();
            while (nodeRset.next()) {
                x = nodeRset.getInt("XPOS");
                y = nodeRset.getInt("YPOS");
                node = new Node(x, y, true, "NAME");
                collectionOfNodes.addNode(node);
            }

            nodeRset.close();
            stmt.close();

            collectionOfNodes.toString();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws SQLException{
        launch(args); }
}
