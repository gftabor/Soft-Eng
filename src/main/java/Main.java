import DBController.DatabaseController;
import controllers.CollectionOfNodes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;

public class Main extends Application {

    CollectionOfNodes collectionOfNodes;




    @Override

    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/views/patientMenuStartView.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/patientMenuStartView.fxml"));

        primaryStage.setTitle("Iteration 1 Minimal Application Correct");
        primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 1274, 710));
        primaryStage.setResizable(false);
        primaryStage.show();


        DatabaseController databaseController = DatabaseController.getInstance();
        databaseController.setDbName("FaulknerDB");
        databaseController.startDB();

    }

    public static void main(String[] args) throws SQLException{

        launch(args);
    }
}
