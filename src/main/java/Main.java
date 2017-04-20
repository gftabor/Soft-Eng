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

        DatabaseController databaseController = DatabaseController.getInstance();
        databaseController.setDbName("./FaulknerDB");
        databaseController.startDB();

        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/views/patientMenuStartView.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/patientMainView.fxml"));

        primaryStage.setTitle("Iteration 3 Professional Prototype");
        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 1366, 768));
        primaryStage.setMinWidth(1366);
        primaryStage.setMinHeight(768);
        primaryStage.setResizable(true);

        primaryStage.show();

        root.getStylesheets().add("/css/styles.css");
        root.getStylesheets().add("/css/bootstrap3.css");
    }

    public static void main(String[] args) throws SQLException{

        launch(args);
    }
}
