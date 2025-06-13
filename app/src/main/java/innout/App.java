package innout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Memuat file FXML login
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        AnchorPane root = loader.load();

        // Menyiapkan stage
        // primaryStage.setFullScreen(true);
        primaryStage.setTitle("Login System");
        primaryStage.setScene(new Scene(root, 400, 300));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
