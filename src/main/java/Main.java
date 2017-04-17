import DBController.DatabaseController;
import controllers.CollectionOfNodes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.SQLException;
import com.guigarage.flatterfx.FlatterFX;

public class Main extends Application {

    CollectionOfNodes collectionOfNodes;




    @Override

    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/views/patientMenuStartView.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/views/patientMenuStartView.fxml"));

        primaryStage.setTitle("Iteration 2 Expanded Application");
        //primaryStage.setFullScreen(true);
        //primaryStage.setMaximized(true);
        primaryStage.setScene(new Scene(root, 1274, 710));
        primaryStage.setResizable(true);

        primaryStage.show();
        FlatterFX.style();


        DatabaseController databaseController = DatabaseController.getInstance();
        databaseController.setDbName("./FaulknerDB");
        databaseController.startDB();

        root.getStylesheets().add("/css/styles.css");

    }

    public static void main(String[] args) throws SQLException{

        launch(args);
    }
}
